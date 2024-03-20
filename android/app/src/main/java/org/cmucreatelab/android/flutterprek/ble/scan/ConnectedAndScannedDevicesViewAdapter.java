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

import java.util.ArrayList;
import java.util.List;

public class ConnectedAndScannedDevicesViewAdapter extends RecyclerView.Adapter<ConnectedAndScannedDevicesViewAdapter.ViewHolder> {

    private final List<Boolean> valuesIsConnected;
    private final List<BluetoothDevice> values;
    private final OnListInteractionListener listener;

    public interface OnListInteractionListener {
        void onItemSelected(BluetoothDevice item, boolean isConnected);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textViewDeviceSsid;
        public BluetoothDevice item;
        public boolean itemIsConnected;

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
        holder.itemIsConnected = valuesIsConnected.get(position);
        String displayName = (holder.itemIsConnected) ? String.format("%s (connected)", holder.item.getName()) : holder.item.getName();
        holder.textViewDeviceSsid.setText(displayName);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(holder.item, holder.itemIsConnected);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return values.size();
    }


    public ConnectedAndScannedDevicesViewAdapter(List<BluetoothDevice> items, OnListInteractionListener listener) {
        values = items;
        this.listener = listener;
        this.valuesIsConnected = new ArrayList<>();
        for (BluetoothDevice item: values) {
            valuesIsConnected.add(false);
        }
    }


    public void clearList() {
        values.clear();
        valuesIsConnected.clear();
        notifyDataSetChanged();
    }


    public void addToList(BluetoothDevice device, boolean isConnected) {
        if (device.getName() != null) {
            if (values.contains(device)) {
                Log.w(Constants.LOG_TAG, "ignoring device "+device.getName()+" since list already contains it");
                return;
            }
            values.add(device);
            valuesIsConnected.add(isConnected);
            notifyDataSetChanged();
        } else {
            Log.d(Constants.LOG_TAG, "ignoring device "+device.getName()+" (null name).");
        }
    }

}
