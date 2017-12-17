package org.pauni.gnomeconnect.core.network;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONObject;
import org.pauni.gnomeconnect.R;
import org.pauni.gnomeconnect.core.models.Computer;
import org.pauni.gnomeconnect.core.models.ConnectedComputer;
import org.pauni.gnomeconnect.core.models.Packet.GCPackage;
import org.pauni.gnomeconnect.core.models.Specs;
import org.pauni.gnomeconnect.core.models.PairRequest;
import org.pauni.gnomeconnect.core.models.Prefs;



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
                    GCClient client = new GCClient(lovedOne.getIpAddress());

                    client.send(new GCPackage(new PairRequest()));
                    GCPackage response = client.getInput();
                    client.close();
                    handlePairResponse(response);
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
    private void handlePairResponse(GCPackage gcPackage) {
        Computer computer = new Computer(gcPackage.getData());

        // Adding the information that was in the GCPackage header
        // (or not even there) and not in the pair-request itself
        computer.setFingerprint (gcPackage.getFingerprint());
        computer.setVersion     (gcPackage.getVersion());
        computer.setIpAddress   (lovedOne.getIpAddress());

        if (saveComputer(computer)) {
            setState(STATE_CONNECTED);
        } else
            setState(STATE_FAILED);
    }

    private boolean saveComputer(Computer computer) {
        try {
            // if some values are missing, don't save the computer
            return Prefs.saveComputerConnection(new ConnectedComputer(computer));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

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
