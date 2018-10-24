package br.com.welingtonfidelis.locedu.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Adapter.SetorAdapterGrid;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class ListaSetor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FloatingActionButton botaoNovoSetor,buscarLocal;
    private RecyclerView listaSetores;
    private List<Setor> setores;
    private ValueEventListener setorEventListener;
    private LinearLayoutManager layoutManager;
    private SetorAdapterGrid adapter;
    private EditText nomeLocalBusca;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SETORES NO CAMPUS");
        getSupportActionBar().setSubtitle("Escolha um setor");
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_cor));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listaSetores = findViewById(R.id.listaSetores);
        botaoNovoSetor = findViewById(R.id.floatingButtonCriarSetor);
        botaoNovoSetor.setVisibility(View.GONE);
        buscarLocal = findViewById(R.id.fb_buscar_local);
        nomeLocalBusca = findViewById(R.id.edt_buscar_local);

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);

        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() == null){
            navigationView.getMenu().findItem(R.id.nav_logar).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
        else {
            navigationView.getMenu().findItem(R.id.nav_logar).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Futura tela sobre nós.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_onde_estou) {
            Intent it2 = new Intent(ListaSetor.this, Mapa.class);
            it2.putExtra("TIPOCHAMADA", false);
            startActivity(it2);
        }
        else if (id == R.id.nav_lista_eventos) {
            Intent it = new Intent(ListaSetor.this, ListaEvento.class);
            startActivity(it);
        }
        else if (id == R.id.nav_logar) {
            if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                Toast.makeText(this, "Já está logado.", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent it = new Intent(this, Login.class);
                startActivity(it);
            }
        }
        else if (id == R.id.nav_logout) {
            ReferencesHelper.getFirebaseAuth().signOut();
            botaoNovoSetor.setVisibility(View.GONE);
            Toast.makeText(ListaSetor.this, "Deslogado", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_share) {
            Intent intentComp = new Intent(Intent.ACTION_SEND);
            intentComp.setType("text/plain");
            intentComp.putExtra(Intent.EXTRA_SUBJECT, "LocEdu - seu aplicativo informativo e localização");
            intentComp.putExtra(Intent.EXTRA_TEXT, "Olha essa aplicativo -> https://play.google.com/store/apps/details?id=br.com.welingtonfidelis.locedu");
            startActivity(Intent.createChooser(intentComp, "Selecione um app"));
        }
        else if (id == R.id.nav_send) {
            Intent it = new Intent(ListaSetor.this, Feedback.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReferencesHelper.getDatabaseReference().child("Setor").removeEventListener(setorEventListener);
    }
}
