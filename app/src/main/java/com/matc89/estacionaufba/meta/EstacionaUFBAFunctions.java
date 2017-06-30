package com.matc89.estacionaufba.meta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tedri on 29/06/2017.
 */

public class EstacionaUFBAFunctions {

    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM hh:mm");

    //Retorna a data atual no formato "yyyy-MM-dd HH:mm:ss"
    public static String getCurrentDateTime() {
        return dbDateFormat.format(new Date());
    }

    public static String getFormattedDate(Date date) {
        return shortDateFormat.format(date);
    }

    public static String getFormattedDate(String dateStr) {
        try {
            return getFormattedDate(dbDateFormat.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Retorna o dia de uma data em YYYY-MM-DD
    public static int getYearOfDate(String date) {
        String[] splittedDate = date.split("-");
        return Integer.parseInt(splittedDate[0]);
    }

    //Retorna o dia de uma data em YYYY-MM-DD
    public static int getMonthOfDate(String date) {
        String[] splittedDate = date.split("-");
        return Integer.parseInt(splittedDate[1]) - 1;
    }

    //Retorna o dia de uma data em YYYY-MM-DD
    public static int getDayOfDate(String date) {
        String[] splittedDate = date.split("-");
        return Integer.parseInt(splittedDate[2]);
    }
}
