package com.example.welington.locedu.View;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NovoEvento extends AppCompatActivity {

    private TextView nomeLocal;
    private TextView nomeEvento;
    private TextView nomeReponsavel;
    private TextView descricaoEvento;
    private TextView data;
    private TextView horario;
    private Spinner tipoEvento;
    private Button btnSalvar, btnCancelar;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_evento);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        nomeEvento = findViewById(R.id.edtNomeEvento);
        nomeReponsavel = findViewById(R.id.edtResponsavelEvento);
        descricaoEvento = findViewById(R.id.edtDescricaoEvento);
        data = findViewById(R.id.edtDataEvento);
        horario = findViewById(R.id.edtHorarioEvento);
        tipoEvento = findViewById(R.id.spTipoEvento);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSalvar = findViewById(R.id.btnSalvar);
        (nomeLocal = findViewById(R.id.tvNomeLocalEvento)).setText(local.getNomeLocal());

        List<String> listaEventos = new ArrayList<String>();
        listaEventos.add("Minicurso");
        listaEventos.add("Oficina");
        listaEventos.add("Palestra");

        ArrayAdapter<String> dataAdpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaEventos);
        dataAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEvento.setAdapter(dataAdpter);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ReferencesHelper.getDatabaseReference().push().getKey();

                Evento e = new Evento();
                e.setLocalKey(local.getKey());
                e.setNomeEvento(nomeEvento.getText().toString());
                e.setResponsavel(nomeReponsavel.getText().toString());
                e.setDescricao(descricaoEvento.getText().toString());
                e.setData(data.getText().toString());
                e.setHorario(horario.getText().toString());

                ReferencesHelper.getDatabaseReference().child("Evento").child(key).setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NovoEvento.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(NovoEvento.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Toast.makeText(getBaseContext(), String.valueOf(tipoEvento.getSelectedItem()), Toast.LENGTH_LONG ).show();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
