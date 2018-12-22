package org.cmucreatelab.android.flutterprek.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.ArrayList;
import java.util.List;

public class ClassroomIndexAdapter extends BaseAdapter {

    private final ArrayList<String> classroomNames;

    public ClassroomIndexAdapter(ArrayList<String> classroomNames) {
        this.classroomNames = classroomNames;
    }

    @Override
    public int getCount() {
        return classroomNames.size();
    }

    @Override
    public Object getItem(int position) {
        return classroomNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, parent, false);
        } else {
            result = convertView;
        }
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(classroomNames.get(position));

        return result;
    }

}

//public class ClassroomIndexAdapter extends RecyclerView.Adapter<ClassroomIndexAdapter.ViewHolder> {
//
//    private final List<Classroom> mValues;
//    private final OnListFragmentInteractionListener mListener;
//    private final Context appContext;
//
//
//    public void clearList() {
//        mValues.clear();
//        notifyDataSetChanged();
//    }
//
//
////    public void addToList(BluetoothDevice device) {
////        if (device.getName() != null && device.getName().contains("FL")) {
////            if (mValues.contains(device)) {
////                Log.w(Constants.LOG_TAG, "ignoring device "+device.getName()+" since list already contains it");
////                return;
////            }
////            mValues.add(device);
////            notifyDataSetChanged();
////        } else {
////            Log.d(Constants.LOG_TAG, "ignoring device "+device.getName()+" since it does not contain 'FL'.");
////        }
////    }
//
//
//    public ClassroomIndexAdapter(Context appContext, List<Classroom> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//        this.appContext = appContext;
//    }
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.grid_view_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        //holder.mIdView.setText(holder.mItem.getName());
//        holder.mIdView.setText(holder.mItem.getName());
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mIdView;
//        public Classroom mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.text1);
//        }
//
////        @Override
////        public String toString() {
////            return super.toString() + " '" + mContentView.getText() + "'";
////        }
//    }
//}
