package com.example.welington.locedu.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class PopUpInfoLocal extends AppCompatActivity {

    private TextView nome, responsavel, telefone, info;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info_local);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        (nome = findViewById(R.id.tv_NomeLocal)).setText(local.getNomeLocal());
        //(responsavel = findViewById(R.id.tv_ResponsavelLocal)).setText(local.getNomeResponsavel());
        telefone = findViewById(R.id.tv_TelefoneLocal);
        (info = findViewById(R.id.tv_InformacaoLocal)).setText(local.getHorarioFuncionamento());
    }
}
