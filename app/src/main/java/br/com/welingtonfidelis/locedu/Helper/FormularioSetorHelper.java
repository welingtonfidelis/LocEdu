package br.com.welingtonfidelis.locedu.Helper;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.View.NovoSetor;

import br.com.welingtonfidelis.locedu.R;

public class FormularioSetorHelper {
    private EditText nomeSetor;
    private FloatingActionButton botaoSalvar, botaoDeletar, botaoCancelar;

    private Setor setor;

    public FormularioSetorHelper(NovoSetor activity){
        this.setor = new Setor();

        this.nomeSetor = activity.findViewById(R.id.editTextNomeSetor);
        this.botaoSalvar = activity.findViewById(R.id.btn_salvar);
        this.botaoCancelar = activity.findViewById(R.id.btn_cancelar);
        this.botaoDeletar = activity.findViewById(R.id.btn_deletar);
    }

    public Setor retornaSetorDoFormulario(){
        setor.setNomeSetor(nomeSetor.getText().toString());

        return setor;
    }

    public void insereSetorNoFormulario(Setor s){
        this.nomeSetor.setText(s.getNomeSetor());

        this.setor = s;
    }
}
