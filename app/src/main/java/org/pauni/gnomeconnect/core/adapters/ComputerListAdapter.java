package org.pauni.gnomeconnect.core.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.pauni.gnomeconnect.R;
import org.pauni.gnomeconnect.core.models.Computer;
import java.util.ArrayList;

/**
 *      This adapter is for a list that displays computers
 *      running GNOME Connect Desktop in the same (W)LAN as
 *      the phone.
 */

public class ComputerListAdapter  extends ArrayAdapter<Computer> {
    public ComputerListAdapter(Context context, ArrayList<Computer> computers) {
        super(context, 0, computers);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get computer item for current pos.
        Computer computer = getItem(position);

        // Dunno why this would happen, but I don't wanna risk it
        if (computer == null) {
            TextView tv_emergency = convertView.findViewById(R.id.tv_computerName);
            tv_emergency.setText("ERROR! getItem(position) returned null. That should not have happened.\nPlease consult the Developer");             // this way I know if it happens anyway
            return convertView;
        }

        // If view is being recycled, reuse it. If not inflate new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_computer, parent, false);
        }

        // Create the TextViews and set their texts
        TextView tv_computerName = convertView.findViewById(R.id.tv_computerName);
        TextView tv_computerOS   = convertView.findViewById(R.id.tv_computerOS);

        tv_computerName.setText(computer.getDevicename());
        tv_computerOS.setText(computer.getOs());

        return convertView;
    }
}
