package com.matc89.estacionaufba.enums;

/**
 * Created by icaroerasmo on 27/08/17.
 */

public enum JsonOperation {
    ID("id"), NAME("name");

    String op;

    JsonOperation(String op){
        this.op = op;
    }

    @Override
    public String toString() {
        return op;
    }
}
