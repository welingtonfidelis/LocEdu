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

import br.com.welingtonfidelis.locedu.Adapter.LocalAdapter;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.R;

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
    private String nomeBusca;
    private TextView semResultadosBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_local);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);
        Intent it =  getIntent();
        nomeBusca = it.getStringExtra("NOMEBUSCA");

        //Criando e editando toolbar
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        if(nomeBusca!=null){
            getSupportActionBar().setTitle("LOCAIS DA BUSCA");
        }
        else{
            getSupportActionBar().setTitle(setor.getNomeSetor());
        }
        getSupportActionBar().setSubtitle("Escolhar um local ou servidor");
        toolbar.setBackgroundColor(Color.parseColor("#05ADE8"));

        listaLocais = findViewById(R.id.listaLocais);
        botaoNovoLocal = findViewById(R.id.floatingActionButtonNovoLocal);
        semResultadosBusca = findViewById(R.id.tv_sem_resultado);

        registerForContextMenu(listaLocais);

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

                    if(locais.size() > 0){
                       semResultadosBusca.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(nomeBusca == null){
            ReferencesHelper.getDatabaseReference().child("Local").orderByChild("keySetor").equalTo(setor.getKey()).addValueEventListener(localEventListener);
        }
        else{
            String upperString = nomeBusca.substring(0,1).toUpperCase() + nomeBusca.substring(1);//garantindo que a primeira letra da busca seja maiúscula
            ReferencesHelper.getDatabaseReference().child("Local").orderByChild("nomeLocal").startAt(upperString).endAt(upperString+"\uf8ff").addValueEventListener(localEventListener);
        }

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
                    Intent it = new Intent(ListaLocal.this, Login.class);
                    startActivity(it);
                }
                return true;
            case R.id.ondeEstou:
                Intent it2 = new Intent(ListaLocal.this, Mapa.class);
                it2.putExtra("TIPOCHAMADA", false);
                startActivity(it2);
                return true;
            case R.id.sair:
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    //ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
                    ReferencesHelper.getFirebaseAuth().signOut();
                    botaoNovoLocal.setVisibility(View.GONE);
                    Toast.makeText(this, "Deslogado", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Não está logado.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.feedBacak:
                Intent it = new Intent(ListaLocal.this, Feedback.class);
                startActivity(it);
                return true;
            case R.id.menu_home:
                Intent it3 = new Intent(ListaLocal.this, Home.class);
                startActivity(it3);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() == null){
            menu.findItem(R.id.sair).setVisible(false);
            return super.onPrepareOptionsMenu(menu);

        }
        else{
            menu.findItem(R.id.sair).setVisible(true);
            return super.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(localEventListener);
    }
}
