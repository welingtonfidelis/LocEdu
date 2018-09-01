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
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Controller.Util;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class AlterarLocal extends AppCompatActivity {

    private Local local;
    private TextView identificador;
    private EditText edtNomeLocal;
    //private EditText edtNomeResponsavelLocal;
    private EditText informacao;
    private EditText edtLatitude;
    private EditText edtLongitude;
    private Button btnSalvar;
    private Button btnDeletar;
    private Button btnCancelar;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_local);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        (identificador = findViewById(R.id.tvIdentificador)).setText(local.getKey());
        (edtNomeLocal = findViewById(R.id.edtNomeEvento)).setText(local.getNomeLocal());
       // (edtNomeResponsavelLocal = findViewById(R.id.edtNomeResponsavelEvento)).setText(local.getNomeResponsavel());
        (informacao = findViewById(R.id.edtInformacao)).setText(local.getInformacao());
        (edtLatitude = findViewById(R.id.edtLatitude)).setText(local.getLatitude().toString());
        (edtLongitude = findViewById(R.id.edtLongitude)).setText(local.getLongitude().toString());
        btnSalvar = findViewById(R.id.btnSalvar);
        btnDeletar = findViewById(R.id.btnDeletar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                local.setNomeLocal(edtNomeLocal.getText().toString());
                //local.setNomeResponsavel(edtNomeResponsavelLocal.getText().toString());
                local.setInformacao(informacao.getText().toString());
                local.setLatitude(Double.parseDouble(edtLatitude.getText().toString()));
                local.setLongitude(Double.parseDouble(edtLongitude.getText().toString()));

                ReferencesHelper.getDatabaseReference().child("Local").child(local.getKey()).setValue(local);
                Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.exibeAlerta(local.getKey(), "Local", AlterarLocal.this);
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
