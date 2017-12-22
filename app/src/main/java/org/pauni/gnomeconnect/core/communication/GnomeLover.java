package org.pauni.gnomeconnect.core.communication;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.pauni.gnomeconnect.R;
import org.pauni.gnomeconnect.core.encryption.Encryption;
import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.models.Computer;
import org.pauni.gnomeconnect.core.models.ConnectedComputer;
import org.pauni.gnomeconnect.core.models.Pairing;
import org.pauni.gnomeconnect.core.models.Prefs;


/**
 *  This class is for pairing phone with desktop
 */

public class GnomeLover implements Protocol {
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

        setState(STATE_WAITING, null);
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
    public void startPairing() {
        // ask the current lovedOne to pair. If he accepts, this method saves
        // the lovedOne to the connected computers and returns true.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Computer computer = new Computer();

                    GCClient client = new GCClient(lovedOne.getIpAddress());

                    client.send(Pairing.Step1()); // send request

                    int step = client.getInput().getData().getInt(Keys.Pairing.STEP);

                    if (step == Values.Pairing.STEP_1) {
                        String publicKey = client.getInput().getData().getString(Keys.Pairing.DATA);

                        client.send(Pairing.Step2());

                        step = client.getInput().getData().getInt(Keys.Pairing.STEP);
                    }

                    if (step == Values.Pairing.STEP_2) {
                        String publicKey = client.getInput().getData().getString(Keys.Pairing.DATA);

                        client.send(Pairing.Step3());

                        step = client.getInput().getData().getInt(Keys.Pairing.STEP);
                    }

                    if (step == Values.Pairing.STEP_3) {
                        // ask user if he wants to connect...

                        //client.send(Pairing.Step4());

                    }

                    String sharedSecretHalf1 = "random";
                    if (step == Values.Pairing.STEP_4) {
                        boolean accepted = client.getInput().getData().getBoolean(Keys.Pairing.DATA);

                        if (accepted) {
                            client.send(Pairing.Step5(sharedSecretHalf1));
                        } else {
                            return;
                        }

                        step = client.getInput().getData().getInt(Keys.Pairing.STEP);
                    }

                    if (step == Values.Pairing.STEP_5) {
                        String sharedSecretHalf2 = client.getInput().getData().getString(Keys.Pairing.DATA);
                        client.send(Pairing.Step6(sharedSecretHalf2));
                        Encryption.createSharedSecret(sharedSecretHalf1+sharedSecretHalf2);
                    }




                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    setState(STATE_FAILED, null);
                }
            }
        }).start();

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

    private void setState(final int state, final  String msg) {
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
