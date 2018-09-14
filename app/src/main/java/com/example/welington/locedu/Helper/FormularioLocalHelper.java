package com.example.welington.locedu.Helper;

import android.widget.EditText;

import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.NovoLocal;

public class FormularioLocalHelper {
    private EditText nomeLocal;
    private EditText latitude;
    private EditText longitude;
    private EditText informacao;
    private Local local;

    public FormularioLocalHelper(NovoLocal activity) {
        this.local = new Local();
        
        this.nomeLocal = activity.findViewById(R.id.edtNomeEvento);
        this.latitude = activity.findViewById(R.id.edtLatitude);
        this.longitude = activity.findViewById(R.id.edtLongitude);
        this.informacao = activity.findViewById(R.id.edtInformacao);
    }
    
    public Local retornaLocalDoFormulario(){
        this.local.setNomeLocal(nomeLocal.getText().toString());
        this.local.setInformacao(informacao.getText().toString());
        this.local.setLatitude(Double.parseDouble(latitude.getText().toString()));
        this.local.setLongitude(Double.parseDouble(longitude.getText().toString()));

        return local;
    }

    public void insereLocalNoFormulario(Local l){
        this.nomeLocal.setText(l.getNomeLocal());
        this.informacao.setText(l.getInformacao());
        this.latitude.setText(l.getLatitude().toString());
        this.longitude.setText(l.getLongitude().toString());

        this.local = l;
    }
}
