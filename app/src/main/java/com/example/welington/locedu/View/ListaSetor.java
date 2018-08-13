package com.example.welington.locedu.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.welington.locedu.Adapter.SetorAdapter;
import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaSetor extends AppCompatActivity {

    private FloatingActionButton botaoLogar;
    private FloatingActionButton botaoNovoSetor;
    private RecyclerView listaSetores;
    private Boolean nivelLogin;
    private List<Setor> setores;
    private ValueEventListener setorEventListener;
    private LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_setor);

        listaSetores = findViewById(R.id.listaSetores);
        botaoLogar = findViewById(R.id.floatingButtonLogar);
        botaoNovoSetor = findViewById(R.id.floatingButtonCriarSetor);
        botaoNovoSetor.setVisibility(View.INVISIBLE);

        setores = new ArrayList<>();

        setorEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Setor setor = postSnapshot.getValue(Setor.class);

                        setores.add(setor);
                    }

                    Log.e("e", setores.toString());

                    layoutManager = new LinearLayoutManager(ListaSetor.this);
                    listaSetores.setHasFixedSize(true);
                    listaSetores.setLayoutManager(layoutManager);

                    SetorAdapter setorAdapter = new SetorAdapter(ListaSetor.this, setores);
                    listaSetores.setAdapter(setorAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ReferencesHelper.getDatabaseReference().child("Setor").addValueEventListener(setorEventListener);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaSetor.this, Login.class);
                startActivity(it);
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
    }
}
