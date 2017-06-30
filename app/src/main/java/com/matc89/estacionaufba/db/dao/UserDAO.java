package com.matc89.estacionaufba.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.matc89.estacionaufba.db.vo.User;
import com.matc89.estacionaufba.interfaces.IUserSchema;

import java.util.List;

/**
 * Created by tedri on 29/06/2017.
 */

public class UserDAO extends DaoHelper<User> implements IUserSchema {

    private Cursor mCursor;
    private ContentValues mContentValues;

    public UserDAO(SQLiteDatabase db) {
        super(db);
    }

    //Criar usuário e retornar o usuário criado
    public boolean create(User user) {
        // set values
        setContentValues(user);
        try {
            user.setId(super.insert(USER_TABLE, getContentValues()));
            return true;
        } catch (SQLiteConstraintException ex) {
            Log.w(super.TAG, ex.getMessage());
            return false;
        }
    }

    public User fetchById(long id) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = COLUMN_ID + " = ?";
        mCursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, COLUMN_ID);
        return cursorEntityResolver(mCursor);
    }

    //Verificar e-mail duplicado
    public User fetchUserByEmail(String email) {
        final String selectionArgs[] = {email};
        final String selection = COLUMN_EMAIL + " = ?";
        mCursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, COLUMN_ID);
        return cursorEntityResolver(mCursor);
    }

    //Buscar usuário por e-mail e senha
    public User fetchUserBy(String email, String password) {
        final String selectionArgs[] = {email, password};
        final String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        User user = new User();
        mCursor = super.query(USER_TABLE, USER_COLUMNS, selection, selectionArgs, COLUMN_ID);
        return cursorEntityResolver(mCursor);
    }

    //Atualizar usuário
    public boolean update(User user) {
        final String selectionArgs[] = {String.valueOf(user.getId())};
        final String selection = COLUMN_ID + " = ?";
        setContentValues(user);

        return super.update(USER_TABLE, getContentValues(), selection, selectionArgs) > 0;
    }

    //Apagar usuário
    public boolean delete(long id) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = COLUMN_ID + " = ?";
        return super.delete(USER_TABLE, selection, selectionArgs) > 0;
    }

    //Listar todos os usuários
    public List<User> list() {
        mCursor = super.query(USER_TABLE, USER_COLUMNS, null, null, COLUMN_ID);
        return cursorListResolver(mCursor);
    }

    @Override
    protected User cursorToEntity(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setNome(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setPlacaCarro(cursor.getString(4));
        user.setStatus(cursor.getLong(5));
        user.setDateCreated(cursor.getString(6));
        user.setDateUpdated(cursor.getString(7));

        return user;
    }

    private ContentValues getContentValues() {
        return mContentValues;
    }

    private void setContentValues(User user) {
        mContentValues = new ContentValues();
        mContentValues.put(COLUMN_NAME, user.getNome());
        mContentValues.put(COLUMN_EMAIL, user.getEmail());
        mContentValues.put(COLUMN_PASSWORD, user.getPassword());
        mContentValues.put(COLUMN_PLACA_CARRO, user.getPlacaCarro());
        mContentValues.put(COLUMN_STATUS, user.getStatus());
        mContentValues.put(COLUMN_DATE_CREATED, user.getDateCreated());
        mContentValues.put(COLUMN_DATE_UPDATED, user.getDateUpdated());
    }
}