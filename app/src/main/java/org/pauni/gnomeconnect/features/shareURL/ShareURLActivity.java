package org.pauni.gnomeconnect.features.shareURL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.pauni.gnomeconnect.R;

/**
 * This receiver sends links to the pc
 */

public class ShareURLActivity extends Activity {
    boolean fullscreen = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareurl);
        // app not running -> Prefs nullpointerexc. So init Prefs also when using Shareact.


        final String SHARED_URL = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("View on PC: " + SHARED_URL.substring(7))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: send over comMgr
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setMultiChoiceItems(new CharSequence[]{"fullscreen"}, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        Log.d("checkbox", "check change");
                        fullscreen = isChecked;
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();

    }


}
