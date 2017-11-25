package org.pauni.gnomeconnect.network;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONObject;
import org.pauni.gnomeconnect.R;
import org.pauni.gnomeconnect.models.Computer;
import org.pauni.gnomeconnect.models.ConnectedComputer;
import org.pauni.gnomeconnect.models.Specs;
import org.pauni.gnomeconnect.models.Packet;
import org.pauni.gnomeconnect.models.PairRequest;
import org.pauni.gnomeconnect.models.Prefs;



/**
 *  This class is for pairing phone with desktop
 */

public class GnomeLover {
    private Computer lovedOne;
    private Activity activity; // used to perform actions on UI thread
    private ImageView iv_statusIcon;
    private TextView  tv_statusText;


    // States during and after pairing
    private final int STATE_WAITING   = 0;
    private final int STATE_CONNECTED = 1;
    private final int STATE_FAILED    = 2;


    /*
     *      CONSTRUCTOR
     */
    public GnomeLover(Activity activity, View clickedView) {
        this.activity = activity;
        iv_statusIcon = clickedView.findViewById(R.id.iv_statusIcon);
        tv_statusText = clickedView.findViewById(R.id.tv_statusText);

        setState(STATE_WAITING);
    }



    /*
     *      PUBLIC METHODS
     */
    public void setLovedOne(Computer lovedOne) {
        // set the lovedOne if default constructor was used
        this.lovedOne = lovedOne;
    }



    // to create a variable here is a bad solution, but I don't want any threading
    // or asynctask in my activity.
    public void sendPairRequest() {
        // ask the current lovedOne to pair. If he accepts, this method saves
        // the lovedOne to the connected computers and returns true.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GnomeConnectClient client = new GnomeConnectClient(lovedOne.getIpAddress());
                    String type;
                    Packet output;
                    output = new Packet(new PairRequest());
                    client.send(output);
                    Packet input = client.getInput();
                    client.close();

                    type = input.getPayload().getString(Specs.PAYLOAD_TYPE);
                    if (type.equals(Specs.TYPE_PAIRREQUEST)) {
                        handlePairResponse(input);
                    } else {
                        setState(STATE_FAILED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setState(STATE_FAILED);
                }
            }
        }).start();

    }


    /*
     *      PRIVATE METHODS
     */
    private void handlePairResponse(Packet packet) {
        JSONObject data     = packet.getData();

        Computer computer   = Computer.fromJson(data);
        if (computer != null) {

            // Adding the information that was in the packet header
            // (or not even there) and not in the pair-request itself
            computer.setName        (packet.getHostname());
            computer.setFingerprint (packet.getFingerprint());
            computer.setVersion     (packet.getVersion());
            computer.setIpAddress   (lovedOne.getIpAddress());

            if (saveComputer(computer)) {
                setState(STATE_CONNECTED);
            } else
                setState(STATE_FAILED);
        }
    }

    private boolean saveComputer(Computer computer) {
        ConnectedComputer connectedComputer = new ConnectedComputer(computer);
        // if some values are missing, don't save the computer
        return Prefs.saveComputerConnection(connectedComputer);
    }

    private void setState(final int state) {
        // show the user the current state of connection during and after pairing
        // by showing certain texts and icons in the respective computer-list-item.
        final int connected = R.drawable.ic_check_circle_black_24dp;
        final int waiting   = R.drawable.ic_clock_black_24dp;
        final int failed    = R.drawable.ic_error_outline_black_24dp;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case STATE_WAITING:
                        iv_statusIcon.setImageDrawable(activity.getDrawable(waiting));
                        break;
                    case STATE_CONNECTED:
                        iv_statusIcon.setImageDrawable(activity.getDrawable(connected));
                        break;
                    case STATE_FAILED:
                        iv_statusIcon.setImageDrawable(activity.getDrawable(failed));
                        break;
                }
            }
        });


    }

}
