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
    private Double latitude;
    private Double longitude;
    private String informacao;
    private String imagem;

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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getKeySetor() {
        return keySetor;
    }

    public void setKeySetor(String keySetor) {
        this.keySetor = keySetor;
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

    @Override
    public String toString() {
        return "Local{" +
                "nomeLocal='" + nomeLocal + '\'' +
                '}';
    }
}
