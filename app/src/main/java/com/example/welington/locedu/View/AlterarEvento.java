package com.example.welington.locedu.View;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Controller.Util;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

public class AlterarEvento extends AppCompatActivity {

    private Evento evento;
    private AlertDialog alerta;
    private TextView identificador;
    private EditText nomeEvento;
    private EditText nomeResponsavel;
    private EditText descricao;
    private EditText data;
    private EditText hora;
    private Spinner spinner;
    private Button btnSalvar;
    private Button btnDeletar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_evento);

        Gson gson = new Gson();
        evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);

        (identificador = findViewById(R.id.tvIdentificadorEvento)).setText(evento.getKey());
        (spinner = findViewById(R.id.spTipoEvento)).setSelection(Util.posicaoSpinner(evento.getTipo()));
        (nomeEvento = findViewById(R.id.edtNomeEvento)).setText(evento.getNomeEvento());
        (nomeResponsavel = findViewById(R.id.edtResponsavelEvento)).setText(evento.getResponsavel());
        (descricao = findViewById(R.id.edtDescricaoEvento)).setText(evento.getDescricao());
        (data = findViewById(R.id.edtDataEvento)).setText(evento.getData());
        (hora = findViewById(R.id.edtHorarioEvento)).setText(evento.getHorario());
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnDeletar = findViewById(R.id.btnDeletar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evento.setTipo(String.valueOf(spinner.getSelectedItem()));
                evento.setNomeEvento(nomeEvento.getText().toString());
                evento.setResponsavel(nomeResponsavel.getText().toString());
                evento.setDescricao(descricao.getText().toString());
                evento.setData(data.getText().toString());
                evento.setHorario(hora.getText().toString());

                ReferencesHelper.getDatabaseReference().child("Evento").child(evento.getKey()).setValue(evento);
                Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibeAlerta(evento.getKey(), "Evento");
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void exibeAlerta(final String  chave, final String tabela) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            vibrator.vibrate(500);
        }
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("ALERTA");
        //define a mensagem
        builder.setMessage("Deletar informação?");
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ReferencesHelper.getDatabaseReference().child(tabela).child(chave).removeValue();
                Toast.makeText(getBaseContext(), "Informação excluida.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
