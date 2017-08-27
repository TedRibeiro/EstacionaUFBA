package com.matc89.estacionaufba.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.matc89.estacionaufba.R;

import java.util.Map;

/**
 * Created by icaroerasmo on 27/08/17.
 */

public class JsonModelAdapter extends BaseAdapter implements SpinnerAdapter{

    Map<Integer, String> elements;
    Activity activity;

    Integer id;
    String value;

    public JsonModelAdapter(Activity activity, Map<Integer, String> elements){
        this.elements = elements;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public String getItem(int position) {
        loadElement(position);
        return value;
    }

    @Override
    public long getItemId(final int position) {
        loadElement(position);
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        loadElement(position);

        View spinView;
        if( convertView == null ){
            LayoutInflater inflater = activity.getLayoutInflater();
            spinView = inflater.inflate(R.layout.spin_layout_model, null);
        } else {
            spinView = convertView;
        }
        TextView t1 = (TextView) spinView.findViewById(R.id.field1);
        t1.setText(value);
        return spinView;
    }

    private void loadElement(int position){

        Integer a = 0;

        for(Integer key : elements.keySet()){
            if(position == a){
                id = key;
                value = elements.get(key);
                return;
            }
            a++;
        }
    }
}
