package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class AbstractListAdapter<T> extends BaseAdapter {


    @Override
    public int getCount() {
        return getList().size();
    }


    @Override
    public Object getItem(int position) {
        return getList().get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * The list data structure that the adapter is based on.
     * @return {@link List} object.
     */
    public abstract List<T> getList();


    /**
     * From {@link BaseAdapter}, construct the view at the given position. Note that views might be recycled.
     * @param position
     * @param convertView
     * @param parent
     * @return the View to be displayed for the given position.
     */
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
