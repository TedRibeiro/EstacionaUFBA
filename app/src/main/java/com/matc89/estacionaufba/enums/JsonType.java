package com.matc89.estacionaufba.enums;

/**
 * Created by icaroerasmo on 27/08/17.
 */

public enum JsonType {
    VEHICLES("http://fipeapi.appspot.com/api/1/carros/veiculos/%d.json"),
    BRANDS("http://fipeapi.appspot.com/api/1/carros/marcas.json");

    String type;

    JsonType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
