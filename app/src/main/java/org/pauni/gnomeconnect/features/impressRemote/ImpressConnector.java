package org.pauni.gnomeconnect.features.impressRemote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.pauni.gnomeconnect.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *  This class provides the functionality to connect
 *  to a running LibreOffice program and save the
 *  connection permanently
 */

public class ImpressConnector extends Activity {
    private static final int BLOCKING_TIMEOUT_IN_SECONDS = 5;
    DatagramSocket socket;
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> servers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impress_connector);

        servers = new ArrayList<>();
        listView = findViewById(R.id.lv_impress);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_expandable_list_item_1, servers );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendPairingRequest();
            }
        });


        startThread();
    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSearching();
                listenForSearchResult();
            }
        }).start();
    }

    private void startSearching() {
        try  {
            String msg = ImpressProtocol.Commands
                    .prepareCommand(ImpressProtocol.Commands.SEARCH_SERVERS);

            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length());
            packet.setAddress(InetAddress.getByName(ImpressProtocol.Addresses.SERVER_SEARCH));
            packet.setPort(ImpressProtocol.Ports.SERVER_SEARCH);

            socket = new DatagramSocket();
            socket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(BLOCKING_TIMEOUT_IN_SECONDS));
            socket.send(packet);




            Log.i("ImpressConnector", "Search command sent!");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForSearchResult() {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String searchResult = new String(packet.getData(), ImpressProtocol.CHARSET);
            Scanner scanner = new Scanner(searchResult);

            String message = scanner.nextLine();

            if (!ImpressProtocol.Messages.ADVERTISE.equals(message)) {
                return;
            }

            String serverHostname = scanner.nextLine();
            serverAddress = packet.getAddress().getHostAddress();

            servers.add(serverHostname);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String serverAddress;
    private void sendPairingRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket client = new Socket(serverAddress, ImpressProtocol.Ports.CLIENT_CONNECTION);
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out.println(ImpressProtocol.Commands.prepareCommand(
                            ImpressProtocol.Commands.PAIR_WITH_SERVER, "Huawei Honor 5X", "1234"));

                    String input;
                    while ((input = in.readLine()) != null) {
                        Log.i("ImpressConnector", "input=" + input);
                        if (input.equals(ImpressProtocol.Messages.PAIRED)) {
                            client.close();
                            in.close();
                            out.close();
                            finish();
                            startActivity(new Intent(ImpressConnector.this, ImpressRemote.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
