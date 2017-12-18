package org.pauni.gnomeconnect.core.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.pauni.gnomeconnect.R;
import org.pauni.gnomeconnect.core.adapters.ComputerListAdapter;
import org.pauni.gnomeconnect.core.models.Prefs;
import org.pauni.gnomeconnect.core.service.GCBackgroundService;
import org.pauni.gnomeconnect.core.service.ReportService;


public class MainActivity extends AppCompatActivity {

    ComputerListAdapter adapter;
    Toolbar toolbar;
    ListView lv_connectedComputers;
    FloatingActionButton fab_addComputer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Prefs(this);

        initViews();
        regListeners();
        setSupportActionBar(toolbar);

        startService(new Intent(this, GCBackgroundService.class));
        updateList();
    }

    @Override
    public void onBackPressed() {
        // code below will be executed on back-button press...
        Toast.makeText(this, "Disabled, to use as a trigger to test code. Sorry",
                Toast.LENGTH_SHORT).show();


        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

             case R.id.settings:
                settings();
                return true;

            case R.id.help:
                showHelp();
                return true;

            case R.id.exit:
                exit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settings() {
        // opens settings activity

    }

    private void showHelp() {
        // opens help webpage from gnomeconnect
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/")));
    }

    private void exit() {
        // closes application and all background services
        stopService(new Intent(this, ReportService.class));
        finishAndRemoveTask();
    }

    public void test(View v) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round);

        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_check_circle_black_24dp)
                        .setLargeIcon(icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setOngoing(false);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }





    private void initViews() {
        toolbar = findViewById(R.id.toolbar_main);
        lv_connectedComputers = findViewById(R.id.lv_connections);
        fab_addComputer = findViewById(R.id.fab_add_computer);
    }

    private void regListeners() {
        /*
         *      Floating Action Button "Add Computer"
         */
        fab_addComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddComputerActivity.class));
            }
        });

    }

    private void updateList() {
        adapter = new ComputerListAdapter(this, Prefs.getConnectedComputers());
        lv_connectedComputers.setAdapter (adapter);

    }
}
