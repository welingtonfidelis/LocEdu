package com.example.welington.locedu.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class AlterarSetor extends AppCompatActivity {

    private Setor setor;
    private EditText nomeSetor;
    private EditText bloco;

    private Button botaoSalvar;
    private Button botaoCancelar;
    private Button botaoDeletar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_setor);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);

        (nomeSetor = (EditText) findViewById(R.id.editTextNomeSetor)).setText(setor.getNomeSetor());
        (bloco = (EditText) findViewById(R.id.editTextBloco)).setText(setor.getBloco());
        botaoSalvar = (Button) findViewById(R.id.buttonSalvar);
        botaoDeletar = (Button) findViewById(R.id.buttonDeletar);
        botaoCancelar = (Button) findViewById(R.id.buttonCancelar);


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setor.setNomeSetor(nomeSetor.getText().toString());
                setor.setBloco(bloco.getText().toString());

                ReferencesHelper.getDatabaseReference().child("Setor").child(setor.getKey()).setValue(setor);
                finish();
            }
        });
    }
}
