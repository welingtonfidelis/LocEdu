package com.example.welington.locedu.Model;

/**
 * Created by welington on 07/08/18.
 */

public class Setor {
    private String nomeSetor;
    private String nomeResponsavel;
    private String telefone;

    public Setor() {
    }

    public Setor(String nomeSetor, String nomeResponsavel, String telefone){

        this.nomeSetor = nomeSetor;
        this.nomeResponsavel = nomeResponsavel;
        this.telefone = telefone;
    }


    public String getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(String nomeSetor) {
        this.nomeSetor = nomeSetor;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return this.nomeSetor;
    }
}
