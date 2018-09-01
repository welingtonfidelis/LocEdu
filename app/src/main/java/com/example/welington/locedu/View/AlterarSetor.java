package com.example.welington.locedu.View;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Controller.Util;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class AlterarSetor extends AppCompatActivity {

    private Setor setor;
    private EditText nomeSetor;
    private EditText nomeResponsavel;
    private TextView identificador;
    private AlertDialog alerta;
    private Spinner spListaBloco;
    private Button botaoSalvar;
    private Button botaoCancelar;
    private Button botaoDeletar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_setor);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);

        (nomeSetor = findViewById(R.id.editTextNomeSetor)).setText(setor.getNomeSetor());
        (nomeResponsavel = findViewById(R.id.edtNomeResponsavel)).setText(setor.getNomeResponsavel());
        (spListaBloco = findViewById(R.id.spListaBloco)).setSelection(Util.posicaoBloco(setor.getBloco()));
        (identificador = findViewById(R.id.tvIdentificador)).setText(setor.getKey());
        botaoSalvar = findViewById(R.id.buttonSalvar);
        botaoDeletar = findViewById(R.id.buttonDeletar);
        botaoCancelar = findViewById(R.id.buttonCancelar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setor.setNomeSetor(nomeSetor.getText().toString());
                setor.setBloco(String.valueOf(spListaBloco.getSelectedItem()));

                ReferencesHelper.getDatabaseReference().child("Setor").child(setor.getKey()).setValue(setor);
                Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        botaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.exibeAlerta(setor.getKey(), "Setor", AlterarSetor.this);
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
