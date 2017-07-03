package com.matc89.estacionaufba.db.vo;

/**
 * Created by tedri on 29/06/2017.
 */

public class User {

    //Atributos que são as colunas da tabela
    private long id;
    private String nome;
    private String email;
    private String password;
    private String placaCarro;
    private long status;
    private String dateCreated;
    private String dateUpdated;

    public User() {

    }

    public User(String nome, String email, String password, String placaCarro, long status, String dateCreated, String
            dateUpdated) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.placaCarro = placaCarro;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPlacaCarro() { return placaCarro; }

    public void setPlacaCarro(String placaCarro) { this.placaCarro = placaCarro; }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
