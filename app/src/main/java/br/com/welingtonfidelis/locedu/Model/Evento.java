package br.com.welingtonfidelis.locedu.Model;

import com.google.firebase.database.Exclude;

/**
 * Created by welington on 17/08/18.
 */

public class Evento {
    private String key;
    private String keyLocal;
    private String nomeEvento;
    private String tipo;
    private String descricao;
    private long data;
    private String horario;
    private String responsavel;
    private int numeroVagas;
    private String siteCadastro;
    private String andar;
    double latitude;
    double longitude;

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

    public String getSiteCadastro() {
        return siteCadastro;
    }

    public void setSiteCadastro(String siteCadastro) {
        this.siteCadastro = siteCadastro;
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
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

    public int getNumeroVagas() {
        return numeroVagas;
    }

    public void setNumeroVagas(int numeroVagas) {
        this.numeroVagas = numeroVagas;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
