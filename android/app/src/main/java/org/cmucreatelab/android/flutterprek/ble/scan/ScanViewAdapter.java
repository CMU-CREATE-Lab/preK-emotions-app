package org.cmucreatelab.android.flutterprek.ble.scan;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

import java.util.List;

public class ScanViewAdapter extends RecyclerView.Adapter<ScanViewAdapter.ViewHolder> {

    private final List<BluetoothDevice> values;
    private final OnListInteractionListener listener;

    public interface OnListInteractionListener {
        void onItemSelected(BluetoothDevice item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textViewDeviceSsid;
        public BluetoothDevice item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewDeviceSsid = view.findViewById(R.id.textViewDeviceSsid);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scan_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = values.get(position);
        holder.textViewDeviceSsid.setText(holder.item.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(holder.item);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return values.size();
    }


    public ScanViewAdapter(List<BluetoothDevice> items, OnListInteractionListener listener) {
        values = items;
        this.listener = listener;
    }


    public void clearList() {
        values.clear();
        notifyDataSetChanged();
    }


    public void addToList(BluetoothDevice device) {
        if (device.getName() != null) {
            if (values.contains(device)) {
                Log.w(Constants.LOG_TAG, "ignoring device "+device.getName()+" since list already contains it");
                return;
            }
            values.add(device);
            notifyDataSetChanged();
        } else {
            Log.d(Constants.LOG_TAG, "ignoring device "+device.getName()+" (null name).");
        }
    }

}
