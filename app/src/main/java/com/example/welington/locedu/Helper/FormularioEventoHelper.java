package com.example.welington.locedu.Helper;

import android.widget.EditText;
import android.widget.Spinner;

import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.NovoEvento;

public class FormularioEventoHelper {
    private Evento evento;
    private EditText nomeEvento;
    private EditText nomeReponsavel;
    private EditText descricaoEvento;
    private EditText data;
    private EditText numeroVagas;
    private EditText horario;
    private Spinner tipoEvento;

    public FormularioEventoHelper(NovoEvento activity){
        this.evento = new Evento();

        this.nomeEvento = activity.findViewById(R.id.edtNomeEvento);
        this.nomeReponsavel = activity.findViewById(R.id.edtResponsavelEvento);
        this.descricaoEvento = activity.findViewById(R.id.edtDescricaoEvento);
        this.data = activity.findViewById(R.id.edtDataEvento);
        this.horario = activity.findViewById(R.id.edtHorarioEvento);
        this.numeroVagas = activity.findViewById(R.id.edt_numero_vagas_evento);
        this.tipoEvento = activity.findViewById(R.id.spTipoEvento);
    }

    public Evento retornaEventoDoFormulario(){
        this.evento.setNomeEvento(nomeEvento.getText().toString());
        this.evento.setResponsavel(nomeReponsavel.getText().toString());
        this.evento.setDescricao(descricaoEvento.getText().toString());
        this.evento.setTipo(String.valueOf(tipoEvento.getSelectedItem()));
        this.evento.setNumeroVagas(Integer.parseInt(numeroVagas.getText().toString()));

        return evento;
    }

    public void insereEventoNoFormulario(Evento e){
        this.nomeEvento.setText(e.getNomeEvento());
        this.nomeReponsavel.setText(e.getResponsavel());
        this.descricaoEvento.setText(e.getDescricao());
        this.data.setText(e.getData());
        this.horario.setText(e.getHorario());
        this.numeroVagas.setText(String.valueOf(e.getNumeroVagas()));
        this.tipoEvento.setSelection(Util.posicaoTipoEvento(e.getTipo()));

        this.evento = e;
    }
}
