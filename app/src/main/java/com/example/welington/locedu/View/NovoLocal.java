package com.example.welington.locedu.View;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class NovoLocal extends AppCompatActivity {

    private EditText nomeLocal;
    private EditText nomeResponsavel;
    private EditText latitude;
    private EditText longitude;
    private EditText informacao;
    private Setor setor;
    private Button salvar;
    private Button cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_local);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);

        nomeLocal = findViewById(R.id.edtNomeEvento);
        nomeResponsavel = findViewById(R.id.edtResponsavelLocal);
        informacao = findViewById(R.id.edtInformacao);
        latitude = findViewById(R.id.edtLatitude);
        longitude = findViewById(R.id.edtLongitude);
        salvar = findViewById(R.id.btnSalvar);
        cancelar = findViewById(R.id.btnCancelar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ReferencesHelper.getDatabaseReference().push().getKey();

                Local l = new Local();
                l.setKeySetor(setor.getKey());
                l.setNomeLocal(nomeLocal.getText().toString());
                l.setNomeResponsavel(nomeResponsavel.getText().toString());
                l.setInformacao(informacao.getText().toString());
                l.setLatitude(Double.parseDouble(latitude.getText().toString()));
                l.setLongitude(Double.parseDouble(longitude.getText().toString()));

                ReferencesHelper.getDatabaseReference().child("Local").child(key).setValue(l).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NovoLocal.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(NovoLocal.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
