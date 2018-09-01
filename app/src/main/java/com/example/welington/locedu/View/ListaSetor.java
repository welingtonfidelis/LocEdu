package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.welington.locedu.Adapter.SetorAdapterGrid;
import com.example.welington.locedu.Adapter.SetorAdapterList;
import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaSetor extends AppCompatActivity {

    private FloatingActionButton botaoNovoSetor;
    private RecyclerView listaSetores;
    private List<Setor> setores;
    private ValueEventListener setorEventListener;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    private SetorAdapterGrid adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_setor);

        listaSetores = findViewById(R.id.listaSetores);
        botaoNovoSetor = findViewById(R.id.floatingButtonCriarSetor);
        botaoNovoSetor.setVisibility(View.GONE);
        toolbar = findViewById(R.id.tb_menu);
        setSupportActionBar(toolbar);

        setores = new ArrayList<>();

        setorEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                setores.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Setor setor = postSnapshot.getValue(Setor.class);

                        setor.setKey(postSnapshot.getKey());
                        setores.add(setor);
                    }

                    int numColunas = 2;
                    listaSetores.setLayoutManager(new GridLayoutManager(ListaSetor.this, numColunas));
                    adapter = new SetorAdapterGrid(ListaSetor.this, setores);
                    //adapter.setClickListener(ListaSetor.this);
                    listaSetores.setAdapter(adapter);

                    //Log.e("e", setores.toString());

                    /*layoutManager = new LinearLayoutManager(ListaSetor.this);
                    listaSetores.setHasFixedSize(true);
                    listaSetores.setLayoutManager(layoutManager);

                    SetorAdapterList setorAdapter = new SetorAdapterList(ListaSetor.this, setores);
                    listaSetores.setAdapter(setorAdapter);*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ReferencesHelper.getDatabaseReference().child("Setor").addValueEventListener(setorEventListener);

        botaoNovoSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaSetor.this, NovoSetor.class);
                startActivity(it);
                //finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
            botaoNovoSetor.setVisibility(View.VISIBLE);
        }
        else{
            botaoNovoSetor.setVisibility(View.GONE);
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
                Intent it = new Intent(ListaSetor.this, Login.class);
                startActivity(it);
                return true;
            case R.id.ajuda:
                Toast.makeText(ListaSetor.this, "ajuda", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sair:
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    //ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
                    ReferencesHelper.getFirebaseAuth().signOut();
                    botaoNovoSetor.setVisibility(View.GONE);
                    Toast.makeText(ListaSetor.this, "Deslogado", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
    }
}
