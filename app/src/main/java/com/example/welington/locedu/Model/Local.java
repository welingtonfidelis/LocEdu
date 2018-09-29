package com.example.welington.locedu.Model;

import com.google.firebase.database.Exclude;

/**
 * Created by welington on 15/08/18.
 */

public class Local {
    private String key;
    private String keySetor;
    private String nomeLocal;
    private String nomeResponsavel;
    private String email;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private String horarioFuncionamento;
    private String imagem;
    private Long qntEvento;

    public Local() {
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public Long getQntEvento() {
        return qntEvento;
    }

    @Exclude
    public void setQntEvento(Long qntEvento) {
        this.qntEvento = qntEvento;
    }

    public String getKeySetor() {
        return keySetor;
    }

    public void setKeySetor(String keySetor) {
        this.keySetor = keySetor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    @Override
    public String toString() {
        return "Local{" +
                "nomeLocal='" + nomeLocal + '\'' +
                '}';
    }
}
