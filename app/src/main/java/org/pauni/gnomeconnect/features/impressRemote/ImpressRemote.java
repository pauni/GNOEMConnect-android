package org.pauni.gnomeconnect.features.impressRemote;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.pauni.gnomeconnect.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by roni on 18.12.17.
 */

public class ImpressRemote extends Activity {
    Socket client;
    PrintWriter out;
    BufferedReader in;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impress_remoute);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Socket(ImpressConnector.serverAddress, ImpressProtocol.Ports.CLIENT_CONNECTION);
                    out = new PrintWriter(client.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out.println(ImpressProtocol.Commands.prepareCommand(
                            ImpressProtocol.Commands.PAIR_WITH_SERVER, "Huawei Honor 5X", "1234"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void next(View view) {
        send(ImpressProtocol.Commands.TRANSITION_NEXT);
    }

    public void startStop(View view) {
        Button button = (Button) view;
        if (button.getText().equals("start")) {
            button.setText("stop");
            send(ImpressProtocol.Commands.PRESENTATION_START);
        } else {
            button.setText("start");
            send(ImpressProtocol.Commands.PRESENTATION_STOP);
        }
    }

    public void previous(View view) {
        send(ImpressProtocol.Commands.TRANSITION_PREVIOUS);
    }

    private void send(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                out.println(ImpressProtocol.Commands.prepareCommand(msg));
                Log.i("send", msg);
            }
        }).start();
    }
}
