package com.example.welington.locedu.View;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.welington.locedu.R;

public class ListaSetor extends AppCompatActivity {

    private FloatingActionButton botaoLogar;
    private FloatingActionButton botaoNovoSetor;
    private ListView listaSetores;

    private Boolean nivelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_setor);

        botaoLogar = findViewById(R.id.floatingButtonLogar);
        botaoNovoSetor = findViewById(R.id.floatingButtonCriarSetor);
        botaoNovoSetor.setVisibility(View.INVISIBLE);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if(bundle != null){
            nivelLogin = (boolean) bundle.get("NIVEL");
            if(nivelLogin){
                botaoNovoSetor.setVisibility(View.VISIBLE);
            }
        }

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaSetor.this, Login.class);
                startActivity(it);
                //finish();
            }
        });

        botaoNovoSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaSetor.this, NovoSetor.class);
                startActivity(it);
                //finish();
            }
        });
    }
}
