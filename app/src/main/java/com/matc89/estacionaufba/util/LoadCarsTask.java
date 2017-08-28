package com.matc89.estacionaufba.util;

import android.os.AsyncTask;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.matc89.estacionaufba.enums.JsonType.BRANDS;

/**
 * Created by galdi on 31/01/2017.
 */

public class LoadCarsTask extends AsyncTask<Void, Void, Map<Integer, String>>   {

    private String jsonType;
    private Spinner spin;
    private String model;
    private static boolean update = true;

    public LoadCarsTask(String jsonType) {
        this.jsonType = jsonType;
    }

    public LoadCarsTask(String jsonType, Spinner spin, String model){
        this.jsonType = jsonType;
        this.spin = spin;
        this.model = model;

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Map<Integer, String> doInBackground(Void... params) {

        WebClient client = new WebClient();
        JSONArray resposta = client.getJson(jsonType.toString());

        Map<Integer, String> list =  new LinkedHashMap<>();

        list.put(0, "Select");

        if(resposta != null){
            for(int i = 0; i < resposta.length(); i++){
                try {

                    JSONObject obj = new JSONObject(resposta.getString(i));

                    if(obj.getString("name") != null) {
                        list.put(obj.getInt("id"), obj.getString("name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }



    @Override
    protected void onPostExecute(Map<Integer, String> resposta) {
        if(spin != null && update){
            spin.setSelection(getPositionOfItem(spin, model));
            update = false;
        }
    }

    private Integer getPositionOfItem(Spinner spinner, String value){

        for(int i = 0; i < spinner.getCount(); i++){

            String adptItem = (String) spinner.getItemAtPosition(i);

            if(value.startsWith(adptItem) || value.equals(adptItem)){
                return i;
            }
        }

        return null;
    }
}
