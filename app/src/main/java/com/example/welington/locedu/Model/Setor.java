package com.example.welington.locedu.Model;

import com.google.firebase.database.Exclude;

/**
 * Created by welington on 07/08/18.
 */

public class Setor {
    private String key;
    private String nomeSetor;
    private String bloco;

    public Setor() {
    }

    public Setor(String nomeSetor, String bloco){
        this.nomeSetor = nomeSetor;
        this.bloco = bloco;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(String nomeSetor) {
        this.nomeSetor = nomeSetor;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    @Override
    public String toString() {
        return this.nomeSetor;
    }
}
