package br.com.welingtonfidelis.locedu.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Adapter.EventoAdapter;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Model.Evento;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaEvento extends AppCompatActivity {

    private FloatingActionButton novoEvento, btnSair;
    private List<Evento> eventos;
    private RecyclerView listaEvento;
    private TextView nomeLocal;
    private Local local;
    private LinearLayoutManager layoutManager;
    private ValueEventListener eventoEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        //Criando e editando toolbar
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        if(local == null){
            getSupportActionBar().setTitle("EVENTOS DISPONÍVEIS");
        }
        else{
            getSupportActionBar().setTitle(local.getNomeLocal());
        }
        getSupportActionBar().setSubtitle("Escolha um evento");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_cor));

        eventos = new ArrayList<>();
        listaEvento = findViewById(R.id.listaEventos);
        novoEvento = findViewById(R.id.fbNovoEvento);

        eventoEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                eventos.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Evento evento = postSnapshot.getValue(Evento.class);
                        evento.setKey(postSnapshot.getKey());

                        Date data = new Date();
                        if(evento.getData() >= data.getTime()) eventos.add(evento);
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

        if(local == null){
            ReferencesHelper.getDatabaseReference().child("Evento").addValueEventListener(eventoEventListener);
        }
        else{
            ReferencesHelper.getDatabaseReference().child("Evento").orderByChild("localKey").equalTo(local.getKey()).addValueEventListener(eventoEventListener);
        }


        novoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();
                Intent it = new Intent(getBaseContext(), NovoEvento.class);
                it.putExtra("LOCAL", g.toJson(local));
                startActivity(it);
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
            case R.id.menu_home:
                Intent it = new Intent(ListaEvento.this, Home.class);
                startActivity(it);
                finish();

            default:
                finish();
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(eventoEventListener);
    }
}
