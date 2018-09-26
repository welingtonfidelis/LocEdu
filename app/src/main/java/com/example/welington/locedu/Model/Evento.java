package com.example.welington.locedu.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by welington on 17/08/18.
 */

public class Evento {
    private String key;
    private String keyLocal;
    private String nomeEvento;
    private String tipo;
    private String descricao;
    private String data;
    private String horario;
    private String responsavel;

    public Evento() {
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getLocalKey() {
        return keyLocal;
    }

    public void setLocalKey(String localKey) {
        this.keyLocal = localKey;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
