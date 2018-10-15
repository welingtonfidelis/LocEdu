package br.com.welingtonfidelis.locedu.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import br.com.welingtonfidelis.locedu.Model.Evento;
import br.com.welingtonfidelis.locedu.R;
import com.google.gson.Gson;

public class PopUpInfoEvento extends AppCompatActivity {

    private Evento evento;
    private TextView nome, responsavel, horario, data, informacao, tipo, vagas, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info_evento);

        Gson gson = new Gson();
        evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);

        (nome = findViewById(R.id.tv_nome_evento)).setText(evento.getNomeEvento());
        (tipo = findViewById(R.id.tv_tipo_evento)).setText(evento.getTipo());
        (responsavel = findViewById(R.id.tv_responsavel_evento)).setText(evento.getResponsavel());
        (horario = findViewById(R.id.tv_horario)).setText(evento.getHorario());
        (data = findViewById(R.id.tv_data)).setText(evento.getData());
        (informacao = findViewById(R.id.tv_informacao)).setText(evento.getDescricao());
        (vagas = findViewById(R.id.tv_numero_vagas)).setText(String.valueOf(evento.getNumeroVagas()));
        (link = findViewById(R.id.tv_link_inscricao)).setText("Clique aqui!");

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreLink(evento.getSiteCadastro());
            }
        });
    }
    private void abreLink(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse(url));
        startActivity(it);
    }
}
