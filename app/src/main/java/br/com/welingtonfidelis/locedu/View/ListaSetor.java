package br.com.welingtonfidelis.locedu.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Adapter.SetorAdapterGrid;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaSetor extends AppCompatActivity {

    private FloatingActionButton botaoNovoSetor,buscarLocal;
    private RecyclerView listaSetores;
    private List<Setor> setores;
    private ValueEventListener setorEventListener;
    private LinearLayoutManager layoutManager;
    private SetorAdapterGrid adapter;
    private EditText nomeLocalBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_setor);

        listaSetores = findViewById(R.id.listaSetores);
        botaoNovoSetor = findViewById(R.id.floatingButtonCriarSetor);
        botaoNovoSetor.setVisibility(View.GONE);
        buscarLocal = findViewById(R.id.fb_buscar_local);
        nomeLocalBusca = findViewById(R.id.edt_buscar_local);

        //Criando e editando toolbar
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SETORES NO CAMPUS");
        getSupportActionBar().setSubtitle("Escolhar um setor");
        toolbar.setBackgroundColor(Color.parseColor("#05ADE8"));

        toolbar.inflateMenu(R.menu.menu_login_ajuda);

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
                    /*int oneRowHeight = listaSetores.getHeight();
                    int rows = (int) (setores.size() / 2);
                    ViewGroup.LayoutParams params = listaSetores.getLayoutParams();
                    params.height = oneRowHeight * rows;
                    listaSetores.setLayoutParams(params);*/
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

        buscarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nomeLocalBusca.getText().toString().equals("")){
                    Intent it = new Intent(ListaSetor.this, ListaLocal.class);
                    it.putExtra("NOMEBUSCA", nomeLocalBusca.getText().toString());
                    startActivity(it);
                }
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
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    Toast.makeText(this, "Já está logado.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent it = new Intent(this, Login.class);
                    startActivity(it);
                }
                return true;
            case R.id.ondeEstou:
                Intent it2 = new Intent(ListaSetor.this, Mapa.class);
                it2.putExtra("TIPOCHAMADA", false);
                startActivity(it2);
                return true;
            case R.id.sair:
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    //ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
                    ReferencesHelper.getFirebaseAuth().signOut();
                    botaoNovoSetor.setVisibility(View.GONE);
                    Toast.makeText(ListaSetor.this, "Deslogado", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Não está logado.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.feedBacak:
                Intent it = new Intent(ListaSetor.this, Feedback.class);
                startActivity(it);
                return true;
            case R.id.menu_home:
                Intent it3 = new Intent(ListaSetor.this, Home.class);
                startActivity(it3);
                finish();
                return true;

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
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
    }
}