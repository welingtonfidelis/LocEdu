package com.example.welington.locedu.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Controller.Util;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlterarEvento extends AppCompatActivity {

    private Evento evento;
    private AlertDialog alerta;
    private TextView identificador;
    private EditText nomeEvento;
    private EditText nomeResponsavel;
    private EditText descricao;
    private EditText data;
    private EditText horario;
    private Spinner spinner;
    private Button btnSalvar;
    private Button btnDeletar;
    private Button btnCancelar;

    //variaveis para Widget Calendar
    private Context context = this;
    private Calendar myCalendar = Calendar.getInstance();
    private String dateFormat = "dd/MM/yyyy";
    private DatePickerDialog.OnDateSetListener date;
    private SimpleDateFormat sdfData;
    private long currentdate;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_evento);

        Gson gson = new Gson();
        evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);

        (identificador = findViewById(R.id.tvIdentificadorEvento)).setText(evento.getKey());
        (spinner = findViewById(R.id.spTipoEvento)).setSelection(Util.posicaoTipoEvento(evento.getTipo()));
        (nomeEvento = findViewById(R.id.edtNomeEvento)).setText(evento.getNomeEvento());
        (nomeResponsavel = findViewById(R.id.edtResponsavelEvento)).setText(evento.getResponsavel());
        (descricao = findViewById(R.id.edtDescricaoEvento)).setText(evento.getDescricao());
        (data = findViewById(R.id.edtDataEvento)).setText(evento.getData());
        (horario = findViewById(R.id.edtHorarioEvento)).setText(evento.getHorario());
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnDeletar = findViewById(R.id.btnDeletar);

        sdfData = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        currentdate = System.currentTimeMillis();
        dateString = sdfData.format(currentdate);
        data.setText(evento.getData());

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

        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hora = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minuto = myCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        horario.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hora, minuto, true);//Yes 24 hour time
                timePickerDialog.setTitle("Escolha o horário");
                timePickerDialog.show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evento.setTipo(String.valueOf(spinner.getSelectedItem()));
                evento.setNomeEvento(nomeEvento.getText().toString());
                evento.setResponsavel(nomeResponsavel.getText().toString());
                evento.setDescricao(descricao.getText().toString());
                evento.setData(data.getText().toString());
                evento.setHorario(horario.getText().toString());

                ReferencesHelper.getDatabaseReference().child("Evento").child(evento.getKey()).setValue(evento);
                Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.exibeAlerta(evento.getKey(), "Evento", AlterarEvento.this);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    //atualiza edt data após clique no calendario
    private void updateDate() {
        data.setText(sdfData.format(myCalendar.getTime()));
    }
}
