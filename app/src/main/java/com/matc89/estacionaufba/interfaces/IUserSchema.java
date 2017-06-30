package com.matc89.estacionaufba.interfaces;

/**
 * Created by tedri on 29/06/2017.
 */

public interface IUserSchema {

    String USER_TABLE = "user";

    String COLUMN_ID = "_id";
    String COLUMN_NAME = "name";
    String COLUMN_EMAIL = "email";
    String COLUMN_PASSWORD = "password";
    String COLUMN_PLACA_CARRO = "placa_carro";
    String COLUMN_STATUS = "status";

    String COLUMN_DATE_CREATED = "date_created";
    String COLUMN_DATE_UPDATED = "date_updated";

    String[] USER_COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_PLACA_CARRO,
            COLUMN_STATUS, COLUMN_DATE_CREATED, COLUMN_DATE_UPDATED};

    String CREATE_USER_TABLE = "create table " + USER_TABLE + "( " + COLUMN_ID + " integer primary key autoincrement," +
            " " + COLUMN_NAME + " varchar(50) not null, " + COLUMN_EMAIL + " varchar(50) not null, " +
            COLUMN_PASSWORD + " varchar(20) not null, " + COLUMN_PLACA_CARRO + " varchar(20) not null, " + COLUMN_STATUS + " " +
            "int not null, " + COLUMN_DATE_CREATED + " datetime not null, " + COLUMN_DATE_UPDATED + " datetime " + ")" +
            "; ";
}
