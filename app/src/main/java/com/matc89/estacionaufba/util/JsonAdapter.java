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
import java.util.concurrent.ExecutionException;

import static com.matc89.estacionaufba.enums.JsonType.BRANDS;
import static com.matc89.estacionaufba.enums.JsonType.VEHICLES;

/**
 * Created by icaroerasmo on 27/08/17.
 */

public class JsonAdapter extends BaseAdapter implements SpinnerAdapter{

    Map<Integer, String> elements;
    Activity activity;

    public JsonAdapter(Activity activity, Map<Integer, String> elements){
        this.elements = elements;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(final int position) {

        int a = 0;

        for(Integer i : elements.keySet()){
            if(a == position){
                return i;
            }
            a++;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View spinView;
        if( convertView == null ){
            LayoutInflater inflater = activity.getLayoutInflater();
            spinView = inflater.inflate(R.layout.spin_layout, null);
        } else {
            spinView = convertView;
        }
        TextView t1 = (TextView) spinView.findViewById(R.id.field1);
        t1.setText(getElementIdByPosition(position));
        return spinView;
    }

    private String getElementIdByPosition(int pos){

            int i = 0;

            for(String element : elements.values()){
                if(i == pos){
                    return element;
                }
                i++;
            }

        return null;
    }
}
