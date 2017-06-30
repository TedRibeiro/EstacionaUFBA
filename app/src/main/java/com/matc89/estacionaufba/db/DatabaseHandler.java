package com.matc89.estacionaufba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matc89.estacionaufba.db.dao.OcorrenciaDAO;
import com.matc89.estacionaufba.db.dao.UserDAO;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.interfaces.IUserSchema;

/**
 * Created by tedri on 29/06/2017.
 */

public class DatabaseHandler {
    //Informações do BD
    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "estacionaufba.db";
    private static final int DATABASE_VERSION = 1;
    public static UserDAO userDao;
    public static OcorrenciaDAO ocorrenciaDAO;
    private final Context mContext;
    private DatabaseHelper mDbHelper;

    //Construtor
    public DatabaseHandler(Context context) {
        this.mContext = context;
    }

    public DatabaseHandler open() {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        userDao = new UserDAO(db);
        ocorrenciaDAO = new OcorrenciaDAO(db);

        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //Criar tabela
        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(IUserSchema.CREATE_USER_TABLE);
            database.execSQL(IOcorrenciaSchema.CREATE_OCORRENCIA_TABLE);
        }

        //Atualizar BD/tabela
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + " which destroys all " +
                    "old data");
            db.execSQL("DROP TABLE IF EXISTS " + IUserSchema.USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + IOcorrenciaSchema.OCORRENCIA_TABLE);
            onCreate(db);
        }
    }

}