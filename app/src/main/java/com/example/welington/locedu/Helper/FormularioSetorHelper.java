package com.example.welington.locedu.Helper;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.NovoSetor;

public class FormularioSetorHelper {
    private EditText nomeSetor;
    private EditText nomeResponsavel;
    private Button botaoSalvar, botaoDeletar;
    private Button botaoCancelar;
    private Spinner spListaBloco;

    private Setor setor;

    public FormularioSetorHelper(NovoSetor activity){
        this.setor = new Setor();

        this.nomeSetor = activity.findViewById(R.id.editTextNomeSetor);
        this.nomeResponsavel = activity.findViewById(R.id.editTextNomeResponsavelSetor);
        this.botaoSalvar = activity.findViewById(R.id.buttonSalvar);
        this.spListaBloco = activity.findViewById(R.id.spListaBloco);
    }

    public Setor retornaSetorDoFormulario(){
        setor.setNomeSetor(nomeSetor.getText().toString());
        setor.setNomeResponsavel(nomeResponsavel.getText().toString());
        setor.setBloco(String.valueOf(spListaBloco.getSelectedItem()));

        return setor;
    }

    public void insereSetorNoFormulario(Setor s){
        this.nomeSetor.setText(s.getNomeSetor());
        this.nomeResponsavel.setText(s.getNomeResponsavel());
        this.spListaBloco.setSelection(Util.posicaoBloco(s.getBloco()));

        this.setor = s;
    }
}
