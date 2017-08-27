package com.matc89.estacionaufba.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.matc89.estacionaufba.enums.JsonOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galdi on 31/01/2017.
 */

public class LoadCarsTask extends AsyncTask<Void, Void, Map<Integer, String>>   {

    private String jsonType;

    public LoadCarsTask(String jsonType) {
        this.jsonType = jsonType;
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

    }
}
