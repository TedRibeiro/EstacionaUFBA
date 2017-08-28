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
    String COLUMN_MARCA_CARRO = "marca_carro";
    String COLUMN_MODELO_CARRO = "modelo_carro";
    String COLUMN_LOCAL = "local";
    String COLUMN_LATITUDE = "latitude";
    String COLUMN_LONGITUDE = "longitude";
    String COLUMN_STATUS = "status";
    String COLUMN_USER_ID = "user_id";

    String COLUMN_DATE_CREATED = "date_created";
    String COLUMN_DATE_UPDATED = "date_updated";

    String COLUMN_PHOTO_PATH = "photo_path";

    String[] OCORRENCIA_COLUMNS = new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_DESCRICAO, COLUMN_PLACA_CARRO,
            COLUMN_MARCA_CARRO, COLUMN_MODELO_CARRO, COLUMN_LOCAL, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_STATUS, COLUMN_USER_ID,
            COLUMN_DATE_CREATED, COLUMN_DATE_UPDATED, COLUMN_PHOTO_PATH};

    String CREATE_OCORRENCIA_TABLE = "create table " + OCORRENCIA_TABLE + "( " + COLUMN_ID + " integer primary key autoincrement," +
            " " + COLUMN_TITULO + " varchar(20) not null, " + COLUMN_DESCRICAO + " text not null, " + COLUMN_PLACA_CARRO +
            " varchar(20) not null, " + COLUMN_MARCA_CARRO + " text not null, " + COLUMN_MODELO_CARRO + " varchar(50) not null, " + COLUMN_LOCAL + " varchar(30) not null, " +
            COLUMN_LATITUDE + " real not null, " + COLUMN_LONGITUDE + " real not null, " + COLUMN_STATUS + " int not null, " +
            COLUMN_DATE_CREATED + " datetime not null, " + COLUMN_DATE_UPDATED + " datetime, " + COLUMN_USER_ID + " int not null, " +
    COLUMN_PHOTO_PATH + " text " +  ");";

}

