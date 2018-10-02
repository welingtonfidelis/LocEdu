package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Adapter.EventoAdapter;
import com.example.welington.locedu.Helper.ReferencesHelper;
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
    private ImageButton home;
    private LinearLayoutManager layoutManager;
    private ValueEventListener eventoEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        eventos = new ArrayList<>();
        home = findViewById(R.id.imgb_home);
        listaEvento = findViewById(R.id.listaEventos);
        (nomeLocal = findViewById(R.id.tvIdentificadorEvento)).setText(local.getNomeLocal().toString());
        novoEvento = findViewById(R.id.fbNovoEvento);

        eventoEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
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

                    EventoAdapter eventoAdapter = new EventoAdapter(ListaEvento.this, eventos, local);
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
                Intent it = new Intent(getBaseContext(), Home.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_ajuda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logar:
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    Toast.makeText(this, "Já está logado.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent it = new Intent(this, Login.class);
                    startActivity(it);
                }
                return true;
            case R.id.ondeEstou:
                Intent it2 = new Intent(this, Mapa.class);
                it2.putExtra("TIPOCHAMADA", false);
                startActivity(it2);
                return true;
            case R.id.sair:
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    //ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
                    ReferencesHelper.getFirebaseAuth().signOut();
                    novoEvento.setVisibility(View.GONE);
                    Toast.makeText(this, "Deslogado", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Não está logado.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(eventoEventListener);
    }
}
