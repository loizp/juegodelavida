package com.zambrix.juegodelavida;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by loizp on 05/03/18.
 */

public class GridAdapter extends BaseAdapter {

    ArrayList<Boolean> items;
    Context context;

    public GridAdapter(ArrayList<Boolean> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.item,null);
        }

        TextView txtview = (TextView) convertView.findViewById(R.id.txtview);
        txtview.setText("       ");

        if(items.get(position))
            txtview.setBackgroundColor(Color.rgb(33,150,243));

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
