package com.matc89.estacionaufba.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;

import java.util.List;

/**
 * Created by tedri on 29/06/2017.
 */

public class OcorrenciaDAO extends DaoHelper<Ocorrencia> implements IOcorrenciaSchema {

    private Cursor mCursor;
    private ContentValues mContentValues;

    public OcorrenciaDAO(SQLiteDatabase db) {
        super(db);
    }

    public boolean addOcorrencia(Ocorrencia ocorrencia) {
        setContentValues(ocorrencia);
        return super.insert(OCORRENCIA_TABLE, getContentValues()) > 0;
    }

    //Buscar ocorrencia por id
    public Ocorrencia fetchById(long id) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = COLUMN_ID + " = ?";
        Ocorrencia ocorrencia = new Ocorrencia();
        mCursor = super.query(OCORRENCIA_TABLE, OCORRENCIA_COLUMNS, selection, selectionArgs, COLUMN_ID);
        return cursorEntityResolver(mCursor);
    }

    //Atualizar ocorrência
    public boolean update(Ocorrencia ocorrencia) {
        final String selectionArgs[] = {String.valueOf(ocorrencia.getId())};
        final String selection = COLUMN_ID + " = ?";
        setContentValues(ocorrencia);
        return super.update(OCORRENCIA_TABLE, getContentValues(), selection, selectionArgs) > 0;
    }

    //Apagar ocorrência
    public boolean delete(long id) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = COLUMN_ID + " = ?";
        return super.delete(OCORRENCIA_TABLE, selection, selectionArgs) > 0;
    }

    //Listar todas as ocorrências
    public List<Ocorrencia> getAllOcorrencias() {
        mCursor = super.query(OCORRENCIA_TABLE, OCORRENCIA_COLUMNS, null, null, COLUMN_ID);
        return cursorListResolver(mCursor);
    }

    //Listar todas as ocorrências ordenadas
    public List<Ocorrencia> getOcorrenciasOrderBy(String column, String order) {
        final String orderBy = column + " " + order;
        mCursor = super.query(OCORRENCIA_TABLE, OCORRENCIA_COLUMNS, null, null, orderBy);
        return cursorListResolver(mCursor);
    }

    //Listar todas as ocorrências registradas por um usuário
    public List<Ocorrencia> getAllOcorrenciasFromUser(long idUser) {
        final String selectionArgs[] = {String.valueOf(idUser)};
        final String selection = COLUMN_USER_ID + " = ?";
        final String orderBy = COLUMN_DATE_CREATED + " DESC";
        mCursor = super.query(OCORRENCIA_TABLE, OCORRENCIA_COLUMNS, selection, selectionArgs, orderBy);
        return cursorListResolver(mCursor);
    }

    public List<Ocorrencia> list(Bundle args) {
        if (args != null) {
            long ocorrenciasFromUser = args.getLong(COLUMN_USER_ID, 0);
            if (ocorrenciasFromUser > 0) {
                return getAllOcorrenciasFromUser(ocorrenciasFromUser);
            }
        }
        return getOcorrenciasOrderBy(COLUMN_DATE_CREATED, "DESC");
    }

    protected Ocorrencia cursorToEntity(Cursor cursor) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(cursor.getLong(0));
        ocorrencia.setTitulo(cursor.getString(1));
        ocorrencia.setDescricao(cursor.getString(2));
        ocorrencia.setPlacaCarro(cursor.getString(3));
        ocorrencia.setMarcaCarro(cursor.getString(4));
        ocorrencia.setModeloCarro(cursor.getString(5));
        ocorrencia.setLocal(cursor.getString(6));
        ocorrencia.setLatitude(cursor.getDouble(7));
        ocorrencia.setLongitude(cursor.getDouble(8));
        ocorrencia.setStatus(cursor.getLong(9));
        ocorrencia.setUserId(cursor.getLong(10));
        ocorrencia.setDateCreated(cursor.getString(11));
        ocorrencia.setDateUpdated(cursor.getString(12));
        ocorrencia.setPhotoPath(cursor.getString(13));
        return ocorrencia;
    }

    protected ContentValues getContentValues() {
        return mContentValues;
    }

    private void setContentValues(Ocorrencia ocorrencia) {
        mContentValues = new ContentValues();
        mContentValues.put(COLUMN_TITULO, ocorrencia.getTitulo());
        mContentValues.put(COLUMN_DESCRICAO, ocorrencia.getDescricao());
        mContentValues.put(COLUMN_PLACA_CARRO, ocorrencia.getPlacaCarro());
        mContentValues.put(COLUMN_MARCA_CARRO, ocorrencia.getMarcaCarro());
        mContentValues.put(COLUMN_MODELO_CARRO, ocorrencia.getModeloCarro());
        mContentValues.put(COLUMN_LOCAL, ocorrencia.getLocal());
        mContentValues.put(COLUMN_LATITUDE, ocorrencia.getLatitude());
        mContentValues.put(COLUMN_LONGITUDE, ocorrencia.getLongitude());
        mContentValues.put(COLUMN_STATUS, ocorrencia.getStatus());
        mContentValues.put(COLUMN_USER_ID, ocorrencia.getUserId());
        mContentValues.put(COLUMN_DATE_CREATED, ocorrencia.getDateCreated());
        mContentValues.put(COLUMN_DATE_UPDATED, ocorrencia.getDateUpdated());
        mContentValues.put(COLUMN_PHOTO_PATH, ocorrencia.getPhotoPath());
    }

}