package br.com.welingtonfidelis.locedu.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.welingtonfidelis.locedu.Model.Evento;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.gson.Gson;

public class PopUpListaMenu extends AppCompatActivity {

    private Local local;
    private Evento evento;
    private TextView ligar, email, informacao, mapa, foto;
    private ImageView imgInfo, imgLigar, imgEmail, imgFoto, imgMapa;
    private Gson g = new Gson();
    private boolean tipoChamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_lista_menu);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);
        Intent it = getIntent();
        tipoChamada = it.getBooleanExtra("TIPOCHAMADA", false);//verifica se a chamada veio da lita LOCAIS ou EVENTOS

        ligar = findViewById(R.id.tv_ligar_1);
        email = findViewById(R.id.tv_email);
        informacao = findViewById(R.id.tv_info);
        mapa = findViewById(R.id.tv_mapa);
        foto = findViewById(R.id.tv_foto);

        imgInfo = findViewById(R.id.imgv_info);
        imgEmail = findViewById(R.id.imgv_email);
        imgFoto = findViewById(R.id.imgv_foto_local);
        imgLigar = findViewById(R.id.imgv_ligar);
        imgMapa = findViewById(R.id.imgv_mapa_local);

        //Se chamada vier dos eventos, desativa ligar, email e foto
        if(tipoChamada){
            ligar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            foto.setVisibility(View.GONE);

            ImageView foto, ligar, email;
            (foto = findViewById(R.id.imgv_foto_local)).setVisibility(View.GONE);
            (ligar = findViewById(R.id.imgv_ligar)).setVisibility(View.GONE);
            (email = findViewById(R.id.imgv_email)).setVisibility(View.GONE);

            evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);
        }

        informacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipoChamada){
                    Intent it = new Intent(PopUpListaMenu.this, PopUpInfoEvento.class);
                    it.putExtra("EVENTO", g.toJson(evento));
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(PopUpListaMenu.this, PopUpInfoLocal.class);
                    it.putExtra("LOCAL", g.toJson(local));
                    startActivity(it);
                    finish();
                }

            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipoChamada){
                    Intent it = new Intent(PopUpListaMenu.this, PopUpInfoEvento.class);
                    it.putExtra("EVENTO", g.toJson(evento));
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(PopUpListaMenu.this, PopUpInfoLocal.class);
                    it.putExtra("LOCAL", g.toJson(local));
                    startActivity(it);
                    finish();
                }

            }
        });

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g = new Gson();
                Intent it = new Intent(PopUpListaMenu.this, Mapa.class);

                if(tipoChamada){
                    it.putExtra("EVENTO", g.toJson(evento));
                }
                else{
                    it.putExtra("TIPOCHAMADA", true);
                    it.putExtra("LOCAL", g.toJson(local));
                    it.putExtra("TIPOCHAMADA", true);
                }

                startActivity(it);
                finish();
            }
        });

        imgMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g = new Gson();
                Intent it = new Intent(PopUpListaMenu.this, Mapa.class);

                if(tipoChamada){
                    it.putExtra("EVENTO", g.toJson(evento));
                }
                else{
                    it.putExtra("TIPOCHAMADA", true);
                    it.putExtra("LOCAL", g.toJson(local));
                    it.putExtra("TIPOCHAMADA", true);
                }

                startActivity(it);
                finish();
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g = new Gson();
                Intent it = new Intent(PopUpListaMenu.this, PopUpFotoLocal.class);
                it.putExtra("LOCAL", g.toJson(local));
                startActivity(it);
                finish();
            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g = new Gson();
                Intent it = new Intent(PopUpListaMenu.this, PopUpFotoLocal.class);
                it.putExtra("LOCAL", g.toJson(local));
                startActivity(it);
                finish();
            }
        });

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLigar = new Intent(Intent.ACTION_DIAL);
                intentLigar.setData(Uri.parse("tel:"+"3526" + local.getTelefone()));
                startActivity(intentLigar);
            }
        });

        imgLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLigar = new Intent(Intent.ACTION_DIAL);
                intentLigar.setData(Uri.parse("tel:"+"3526" + local.getTelefone()));
                startActivity(intentLigar);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.setType("message/rfc822");
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {local.getEmail()});
                //startActivity(Intent.createChooser(intentEmail, "Escolha um App"));
                startActivity(intentEmail);
                //return false;
            }
        });

        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.setType("message/rfc822");
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {local.getEmail()});
                //startActivity(Intent.createChooser(intentEmail, "Escolha um App"));
                startActivity(intentEmail);
                //return false;
            }
        });
    }
}
