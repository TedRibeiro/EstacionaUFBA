package com.matc89.estacionaufba.db.vo;

import android.os.Bundle;

import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.meta.EstacionaUFBAFunctions;

/**
 * Created by tedri on 29/06/2017.
 */

public class Ocorrencia implements IOcorrenciaSchema{
    //Atributos que são as colunas da tabela
    private long id;
    private String titulo;
    private String descricao;
    private String placaCarro;
    private int modeloCarro; //Provisoriamente como int por conta do spinner para escolha
    private String local;
    private double latitude;
    private double longitude;
    private long status;
    private long userId;
    private String dateCreated;
    private String dateUpdated;

    public Ocorrencia() {

    }

    public Ocorrencia(long userId) { this.userId = userId; }

    public Ocorrencia(String titulo, String descricao, String placaCarro, int modeloCarro, String local,
                      double latitude, double longitude, long status, long userId,
                String dateCreated, String dateUpdated) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.placaCarro = placaCarro;
        this.modeloCarro = modeloCarro;
        this.local = local;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    /**
     * HELPER METHODS
     */
    public static Bundle toBundle(Ocorrencia ocorrencia) {
        Bundle args = new Bundle();
        args.putLong(COLUMN_ID, ocorrencia.getId());
        args.putString(COLUMN_TITULO, ocorrencia.getTitulo());
        args.putString(COLUMN_DESCRICAO, ocorrencia.getDescricao());
        args.putString(COLUMN_PLACA_CARRO, ocorrencia.getPlacaCarro());
        args.putInt(COLUMN_MODELO_CARRO, ocorrencia.getModeloCarro());
        args.putString(COLUMN_LOCAL, ocorrencia.getLocal());
        args.putDouble(COLUMN_LATITUDE, ocorrencia.getLatitude());
        args.putDouble(COLUMN_LONGITUDE, ocorrencia.getLongitude());
        args.putLong(COLUMN_STATUS, ocorrencia.getStatus());
        args.putLong(COLUMN_USER_ID, ocorrencia.getUserId());
        args.putString(COLUMN_DATE_CREATED, ocorrencia.getDateCreated());
        args.putString(COLUMN_DATE_UPDATED, ocorrencia.getDateUpdated());
        return args;
    }

    public static Ocorrencia parse(Bundle args) {
        Ocorrencia ocorrencia = new Ocorrencia();
        if (args != null) {
            ocorrencia.setId(args.getLong(COLUMN_ID));
            ocorrencia.setTitulo(args.getString(COLUMN_TITULO));
            ocorrencia.setDescricao(args.getString(COLUMN_DESCRICAO));
            ocorrencia.setPlacaCarro(args.getString(COLUMN_PLACA_CARRO));
            ocorrencia.setModeloCarro(args.getInt(COLUMN_MODELO_CARRO));
            ocorrencia.setLocal(args.getString(COLUMN_LOCAL));
            ocorrencia.setLatitude(args.getDouble(COLUMN_LATITUDE));
            ocorrencia.setLongitude(args.getDouble(COLUMN_LONGITUDE));
            ocorrencia.setStatus(args.getLong(COLUMN_STATUS));
            ocorrencia.setUserId(args.getLong(COLUMN_USER_ID));
            ocorrencia.setDateCreated(args.getString(COLUMN_DATE_CREATED));
            ocorrencia.setDateUpdated(args.getString(COLUMN_DATE_UPDATED));
        }
        return ocorrencia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPlacaCarro() {
        return placaCarro;
    }

    public void setPlacaCarro (String placaCarro) {
        this.placaCarro = placaCarro;
    }

    public int getModeloCarro() { return modeloCarro; }

    public void setModeloCarro(int modeloCarro) { this.modeloCarro = modeloCarro; }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) { this.local = local; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFormattedDateCreated() { return EstacionaUFBAFunctions.getFormattedDate(dateCreated); }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
