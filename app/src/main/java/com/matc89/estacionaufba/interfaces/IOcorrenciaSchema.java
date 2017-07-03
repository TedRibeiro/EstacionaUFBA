package com.matc89.estacionaufba.interfaces;

/**
 * Created by tedri on 29/06/2017.
 */

public interface IOcorrenciaSchema {

    String OCORRENCIA_TABLE = "ocorrencia";

    String COLUMN_ID = "_id";
    String COLUMN_TITULO = "titulo";
    String COLUMN_DESCRICAO = "descricao";
    String COLUMN_PLACA_CARRO = "placa_carro";
    String COLUMN_MODELO_CARRO = "modelo_carro"; //provisoriamente como int
    String COLUMN_LOCAL = "local";
    String COLUMN_LATITUDE = "latitude";
    String COLUMN_LONGITUDE = "longitude";
    String COLUMN_STATUS = "status";
    String COLUMN_USER_ID = "user_id";

    String COLUMN_DATE_CREATED = "date_created";
    String COLUMN_DATE_UPDATED = "date_updated";

    String[] OCORRENCIA_COLUMNS = new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_DESCRICAO, COLUMN_PLACA_CARRO,
            COLUMN_MODELO_CARRO, COLUMN_LOCAL, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_STATUS, COLUMN_USER_ID,
            COLUMN_DATE_CREATED, COLUMN_DATE_UPDATED};

    String CREATE_OCORRENCIA_TABLE = "create table " + OCORRENCIA_TABLE + "( " + COLUMN_ID + " integer primary key autoincrement," +
            " " + COLUMN_TITULO + " varchar(20) not null, " + COLUMN_DESCRICAO + " text not null, " + COLUMN_PLACA_CARRO +
            " varchar(20) not null, " + COLUMN_MODELO_CARRO + " integer not null, " + COLUMN_LOCAL + " varchar(30) not null, " +
            COLUMN_LATITUDE + " real not null, " + COLUMN_LONGITUDE + " real not null, " + COLUMN_STATUS + " int not null, " +
            COLUMN_DATE_CREATED + " datetime not null, " + COLUMN_DATE_UPDATED + " datetime, " + COLUMN_USER_ID + " int not null " + ");";

}

