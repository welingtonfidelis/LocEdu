package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NovoSetor extends AppCompatActivity {
    private EditText nomeSetor;
    private Button botaoSalvar;
    private Button botaoCancelar;
    private Spinner spListaBloco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_setor);

        nomeSetor = (EditText) findViewById(R.id.editTextNomeSetor);
        botaoSalvar = (Button) findViewById(R.id.buttonSalvar);
        botaoCancelar = (Button) findViewById(R.id.buttonCancelar);
        spListaBloco = findViewById(R.id.spListaBloco);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ReferencesHelper.getDatabaseReference().push().getKey();

                Setor s = new Setor();
                s.setNomeSetor(nomeSetor.getText().toString());
                s.setBloco(String.valueOf(spListaBloco.getSelectedItem()));

                ReferencesHelper.getDatabaseReference().child("Setor").child(key).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(NovoSetor.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(NovoSetor.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
