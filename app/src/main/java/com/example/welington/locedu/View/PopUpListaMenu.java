package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class PopUpListaMenu extends AppCompatActivity {

    private Local local;
    private TextView ligar, email, informacao, mapa, foto;
    private Gson g = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_lista_menu);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("EVENTO"), Local.class);

        ligar = findViewById(R.id.tv_ligar);
        email = findViewById(R.id.tv_email);
        informacao = findViewById(R.id.tv_info);
        mapa = findViewById(R.id.tv_mapa);
        foto = findViewById(R.id.tv_foto);

        informacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PopUpListaMenu.this, PopUpInfoLocal.class);
                it.putExtra("LOCAL", g.toJson(local));
                startActivity(it);
                finish();
            }
        });

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PopUpListaMenu.this, "SHOW", Toast.LENGTH_SHORT).show();
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PopUpListaMenu.this, "SHOW", Toast.LENGTH_SHORT).show();
            }
        });

        informacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PopUpListaMenu.this, "SHOW", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
