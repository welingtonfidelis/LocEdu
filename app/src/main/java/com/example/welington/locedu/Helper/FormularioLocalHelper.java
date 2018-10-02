package com.example.welington.locedu.Helper;

import android.widget.EditText;
import android.widget.Spinner;

import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.NovoLocal;

public class FormularioLocalHelper {
    private EditText nomeLocal;
    private EditText nomeResponsavel;
    private EditText email;
    private EditText telefone;
    private EditText latitude;
    private EditText longitude;
    private EditText horarioFuncionamento;
    private Spinner andar;
    private Local local;

    public FormularioLocalHelper(NovoLocal activity) {
        this.local = new Local();
        
        this.nomeLocal = activity.findViewById(R.id.edt_nome_local);
        this.nomeResponsavel = activity.findViewById(R.id.edt_nome_responsavel);
        this.email = activity.findViewById(R.id.edt_email_local);
        this.telefone = activity.findViewById(R.id.edt_telefone_local);
        this.latitude = activity.findViewById(R.id.edt_latitude);
        this.longitude = activity.findViewById(R.id.edt_longitude);
        this.andar = activity.findViewById(R.id.spn_andar);
        this.horarioFuncionamento = activity.findViewById(R.id.edt_horario_funcionamento);
    }
    
    public Local retornaLocalDoFormulario(){
        this.local.setNomeLocal(nomeLocal.getText().toString());
        this.local.setNomeResponsavel(nomeResponsavel.getText().toString());
        this.local.setTelefone(telefone.getText().toString());
        this.local.setEmail(email.getText().toString());
        this.local.setHorarioFuncionamento(horarioFuncionamento.getText().toString());
        this.local.setLatitude(Double.parseDouble(latitude.getText().toString()));
        this.local.setLongitude(Double.parseDouble(longitude.getText().toString()));
        this.local.setAndar(andar.getSelectedItem().toString());

        return local;
    }

    public void insereLocalNoFormulario(Local l){
        this.nomeLocal.setText(l.getNomeLocal());
        this.nomeResponsavel.setText(l.getNomeResponsavel());
        this.telefone.setText(l.getTelefone());
        this.email.setText(l.getEmail());
        this.horarioFuncionamento.setText(l.getHorarioFuncionamento());
        this.latitude.setText(l.getLatitude().toString());
        this.longitude.setText(l.getLongitude().toString());
        this.andar.setSelection(Util.andar(l.getAndar()));

        this.local = l;
    }
}
