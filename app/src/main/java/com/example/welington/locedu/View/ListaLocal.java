package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.welington.locedu.Adapter.LocalAdapter;
import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListaLocal extends AppCompatActivity {

    private Setor setor;
    private RecyclerView listaLocais;
    private FloatingActionButton botaoNovoLocal;
    private List<Local> locais;
    private ValueEventListener localEventListener;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_local);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);

        listaLocais = findViewById(R.id.listaLocais);
        botaoNovoLocal = findViewById(R.id.floatingActionButtonNovoLocal);

        locais = new ArrayList<>();

        localEventListener = new ValueEventListener() {
            @Override
            public void onDataChange( com.google.firebase.database.DataSnapshot dataSnapshot) {
                locais.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Local local = postSnapshot.getValue(Local.class);
                        local.setKey(postSnapshot.getKey());
                        locais.add(local);
                    }

                    layoutManager = new LinearLayoutManager(ListaLocal.this);
                    listaLocais.setHasFixedSize(true);
                    listaLocais.setLayoutManager(layoutManager);

                    LocalAdapter localAdapter = new LocalAdapter(ListaLocal.this, locais);
                    listaLocais.setAdapter(localAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ReferencesHelper.getDatabaseReference().child("Local").orderByChild("keySetor").equalTo(setor.getKey()).addValueEventListener(localEventListener);

        botaoNovoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent it = new Intent(getBaseContext(), NovoLocal.class);
                it.putExtra("SETOR", gson.toJson(setor));
                startActivity(it);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
            botaoNovoLocal.setVisibility(View.VISIBLE);
            //botaoSair.setVisibility(View.VISIBLE);
        }
        else{
            botaoNovoLocal.setVisibility(View.GONE);
            //botaoSair.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(localEventListener);
    }
}
