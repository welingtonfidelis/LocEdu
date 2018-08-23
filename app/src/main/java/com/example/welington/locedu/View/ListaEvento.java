package com.example.welington.locedu.View;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.welington.locedu.Adapter.EventoAdapter;
import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListaEvento extends AppCompatActivity {

    private FloatingActionButton novoEvento, btnSair;
    private List<Evento> eventos;
    private RecyclerView listaEvento;
    private TextView nomeLocal;
    private Local local;
    private ImageView home;
    private LinearLayoutManager layoutManager;
    private ValueEventListener eventoEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        eventos = new ArrayList<>();
        home = findViewById(R.id.imgv_home);
        listaEvento = findViewById(R.id.listaEventos);
        (nomeLocal = findViewById(R.id.tvIdentificadorEvento)).setText(local.getNomeLocal().toString());
        novoEvento = findViewById(R.id.fbNovoEvento);
        btnSair = findViewById(R.id.floatingActionButtonSair);

        eventoEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Evento evento = postSnapshot.getValue(Evento.class);
                        evento.setKey(postSnapshot.getKey());
                        eventos.add(evento);
                    }

                    layoutManager = new LinearLayoutManager(ListaEvento.this);
                    listaEvento.setHasFixedSize(true);
                    listaEvento.setLayoutManager(layoutManager);

                    EventoAdapter eventoAdapter = new EventoAdapter(ListaEvento.this, eventos);
                    listaEvento.setAdapter(eventoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ReferencesHelper.getDatabaseReference().child("Evento").orderByChild("localKey").equalTo(local.getKey()).addValueEventListener(eventoEventListener);

        novoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();
                Intent it = new Intent(getBaseContext(), NovoEvento.class);
                it.putExtra("LOCAL", g.toJson(local));
                startActivity(it);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(), ListaSetor.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
            novoEvento.setVisibility(View.VISIBLE);
            //botaoSair.setVisibility(View.VISIBLE);
        }
        else{
            novoEvento.setVisibility(View.GONE);
            //botaoSair.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(eventoEventListener);
    }
}
