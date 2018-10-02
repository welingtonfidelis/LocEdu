package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.welington.locedu.R;

public class Home extends AppCompatActivity {

    private ImageView irMapa, irListaSetores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        irMapa = findViewById(R.id.imgv_map_home);
        irListaSetores = findViewById(R.id.imgv_lista_home);

        irMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this, Mapa.class);
                it.putExtra("TIPOCHAMADA", false);
                startActivity(it);
                //finish();
            }
        });

        irListaSetores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this, ListaSetor.class);
                startActivity(it);
                //finish();
            }
        });
    }
}
