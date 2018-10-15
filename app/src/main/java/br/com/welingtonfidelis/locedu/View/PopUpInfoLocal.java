package br.com.welingtonfidelis.locedu.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.gson.Gson;

public class PopUpInfoLocal extends AppCompatActivity {

    private TextView nome, responsavel, telefone, email, horario, andar;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info_local);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        nome = findViewById(R.id.tv_nome_local);
        responsavel = findViewById(R.id.tv_nome_responsavel_local);
        telefone = findViewById(R.id.tv_telefone_local);
        email = findViewById(R.id.tv_email_local);
        horario = findViewById(R.id.tv_horario_local);
        andar = findViewById(R.id.tv_andar_local);

        //setando informações
        nome.setText(local.getNomeLocal());
        responsavel.setText(local.getNomeResponsavel());
        telefone.setText("3526-"+local.getTelefone());
        email.setText(local.getEmail());
        horario.setText(local.getHorarioFuncionamento());
        andar.setText(local.getAndar());
    }
}
