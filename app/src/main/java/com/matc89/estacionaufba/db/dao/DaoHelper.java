package com.matc89.estacionaufba.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedri on 29/06/2017.
 */

public abstract class DaoHelper<T> {
    protected static final String TAG = "DAO";

    private SQLiteDatabase db;

    public DaoHelper(SQLiteDatabase db) {
        this.db = db;
    }

    protected abstract T cursorToEntity(Cursor cursor);

    protected abstract T fetchById(long id);

    protected long insert(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    protected int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(tableName, values, selection, selectionArgs);
    }

    protected int delete(String tableName, String selection, String[] selectionArgs) {
        return db.delete(tableName, selection, selectionArgs);
    }

    protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String
            sortOrder) {
        return db.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);
    }

    protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String
            sortOrder, String limit) {

        return db.query(tableName, columns, selection, selectionArgs, null, null, sortOrder, limit);
    }

    protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String
            groupBy, String having, String orderBy, String limit) {

        return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    protected Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    protected T cursorEntityResolver(Cursor cursor) {
        List<T> entityList = cursorListResolver(cursor);
        if (entityList.isEmpty()) {
            return null;
        }
        return entityList.get(0);
    }

    protected List<T> cursorListResolver(Cursor cursor) {
        List<T> entityList = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entityList.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return entityList;
    }


}
