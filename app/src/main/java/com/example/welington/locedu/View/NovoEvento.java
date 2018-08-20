package com.example.welington.locedu.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NovoEvento extends AppCompatActivity {

    private TextView nomeLocal;
    private EditText nomeEvento;
    private EditText nomeReponsavel;
    private EditText descricaoEvento;
    private EditText data;
    private EditText horario;
    private Spinner tipoEvento;
    private Button btnSalvar, btnCancelar;
    private Local local;

    //variaveis para Widget Calendar
    private Context context = this;
    private Calendar myCalendar = Calendar.getInstance();
    private String dateFormat = "dd/MM/yyyy";
    private DatePickerDialog.OnDateSetListener date;
    private SimpleDateFormat sdf;
    private long currentdate;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_evento);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        nomeEvento = findViewById(R.id.edtNomeEvento);
        nomeReponsavel = findViewById(R.id.edtResponsavelEvento);
        descricaoEvento = findViewById(R.id.edtDescricaoEvento);
        data = findViewById(R.id.edtDataEvento);
        horario = findViewById(R.id.edtHorarioEvento);
        tipoEvento = findViewById(R.id.spTipoEvento);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSalvar = findViewById(R.id.btnSalvar);
        (nomeLocal = findViewById(R.id.tvIdentificadorEvento)).setText(local.getNomeLocal());

        sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        currentdate = System.currentTimeMillis();
        dateString = sdf.format(currentdate);
        data.setText(dateString);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ReferencesHelper.getDatabaseReference().push().getKey();

                Evento e = new Evento();
                e.setLocalKey(local.getKey());
                e.setTipo(String.valueOf(tipoEvento.getSelectedItem()));
                e.setNomeEvento(nomeEvento.getText().toString());
                e.setResponsavel(nomeReponsavel.getText().toString());
                e.setDescricao(descricaoEvento.getText().toString());
                e.setData(data.getText().toString());
                e.setHorario(horario.getText().toString());

                ReferencesHelper.getDatabaseReference().child("Evento").child(key).setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NovoEvento.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(NovoEvento.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Toast.makeText(getBaseContext(), String.valueOf(tipoEvento.getSelectedItem()), Toast.LENGTH_LONG ).show();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateDate() {
        data.setText(sdf.format(myCalendar.getTime()));
    }
}
