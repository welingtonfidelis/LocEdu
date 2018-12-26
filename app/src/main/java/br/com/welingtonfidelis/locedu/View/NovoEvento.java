package br.com.welingtonfidelis.locedu.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Helper.FormularioEventoHelper;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Helper.Util;
import br.com.welingtonfidelis.locedu.Model.Evento;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NovoEvento extends AppCompatActivity {
    private TextView data, horario;
    //private Spinner tipoEvento;
    private FloatingActionButton btnSalvar, btnCancelar, btnDeletar;
    private Evento evento;
    private FormularioEventoHelper formularioEventoHelper;
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

        formularioEventoHelper = new FormularioEventoHelper(NovoEvento.this);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);
        evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);

        data = findViewById(R.id.edtDataEvento);
        horario = findViewById(R.id.edtHorarioEvento);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnDeletar = findViewById(R.id.btn_deletar);

        sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        currentdate = System.currentTimeMillis();
        dateString = sdf.format(currentdate);

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

        if(evento == null){
            btnDeletar.setVisibility(View.GONE);
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evento = new Evento();
                    evento = formularioEventoHelper.retornaEventoDoFormulario();
                    evento.setHorario(horario.getText().toString());
                    //evento.setData(data.getText().toString());
                    evento.setData(myCalendar.getTimeInMillis());
                    evento.setLocalKey(local.getKey());
                    evento.setLatitude(local.getLatitude());
                    evento.setLongitude(local.getLongitude());
                    evento.setAndar(local.getAndar());

                    String key = ReferencesHelper.getDatabaseReference().push().getKey();

                    ReferencesHelper.getDatabaseReference().child("Evento").child(key).setValue(evento).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                }
            });
        }else{
            formularioEventoHelper.insereEventoNoFormulario(evento);

            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evento = formularioEventoHelper.retornaEventoDoFormulario();
                    evento.setHorario(horario.getText().toString());
                    //evento.setData(data.getText().toString());
                    evento.setData(myCalendar.getTimeInMillis());

                    ReferencesHelper.getDatabaseReference().child("Evento").child(evento.getKey()).setValue(evento);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            btnDeletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.exibeAlerta(evento.getKey(), "Evento", NovoEvento.this);
                }
            });
        }


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
