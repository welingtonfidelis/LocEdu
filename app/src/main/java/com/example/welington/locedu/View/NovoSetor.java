package com.example.welington.locedu.View;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.welington.locedu.Helper.FormularioSetorHelper;
import com.example.welington.locedu.Helper.ReferencesHelper;
import com.example.welington.locedu.Helper.Util;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class NovoSetor extends AppCompatActivity {
    private FloatingActionButton botaoSalvar, botaoDeletar,botaoCancelar;
    private FormularioSetorHelper formularioSetorHelper;

    private Setor setor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_setor);

        formularioSetorHelper = new FormularioSetorHelper(NovoSetor.this);

        botaoSalvar = findViewById(R.id.btn_salvar);
        botaoCancelar = findViewById(R.id.btn_cancelar);
        botaoDeletar = findViewById(R.id.btn_deletar);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);

        if(setor == null){//verifica se a activity foi chamada para CRIAR um novo setor ou ATUALIZAR um setor
            botaoDeletar.setVisibility(View.GONE);
            setor = new Setor();
            botaoSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = ReferencesHelper.getDatabaseReference().push().getKey();

                    setor = formularioSetorHelper.retornaSetorDoFormulario();

                    ReferencesHelper.getDatabaseReference().child("Setor").child(key).setValue(setor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(NovoSetor.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(NovoSetor.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }else{
            formularioSetorHelper.insereSetorNoFormulario(setor);

            botaoSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setor = formularioSetorHelper.retornaSetorDoFormulario();

                    ReferencesHelper.getDatabaseReference().child("Setor").child(setor.getKey()).setValue(setor);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            botaoDeletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.exibeAlerta(setor.getKey(), "Setor", NovoSetor.this);
                }
            });
        }



        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
