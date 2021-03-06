package br.com.welingtonfidelis.locedu.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Helper.FormularioLocalHelper;
import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Helper.Util;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class NovoLocal extends AppCompatActivity {

    private Setor setor;
    private FloatingActionButton salvar, cancelar, deletar;
    private ImageButton salvar_loc;
    private Local local;

    private FormularioLocalHelper formularioLocalHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_local);

        formularioLocalHelper = new FormularioLocalHelper(NovoLocal.this);

        Gson gson = new Gson();
        setor = gson.fromJson(getIntent().getStringExtra("SETOR"), Setor.class);
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        salvar = findViewById(R.id.btn_salvar);
        cancelar = findViewById(R.id.btn_cancelar);
        deletar = findViewById(R.id.btn_deletar);
        salvar_loc = findViewById(R.id.img_btn_save_loc);

        if (local == null) {//Checa se a activity foi chamada para CRIAR um novo local ou ALTERAR um existente
            deletar.setVisibility(View.GONE);
            local = new Local();
            local.setLatitude(-20.714528);//setando localização padrão para recepçao caso usuário não cadastre
            local.setLongitude(-46.6275);

            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    local = formularioLocalHelper.retornaLocalDoFormulario();
                    local.setKeySetor(setor.getKey());

                    String key = ReferencesHelper.getDatabaseReference().push().getKey();

                    ReferencesHelper.getDatabaseReference().child("Local").child(key).setValue(local).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NovoLocal.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(NovoLocal.this, "Erro ao conectar no banco", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        } else {
            formularioLocalHelper.insereLocalNoFormulario(local);

            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    local = formularioLocalHelper.retornaLocalDoFormulario();

                    ReferencesHelper.getDatabaseReference().child("Local").child(local.getKey()).setValue(local);
                    Toast.makeText(getBaseContext(), "Salvo com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            deletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.exibeAlerta(local.getKey(), "Local", NovoLocal.this);
                }
            });
        }

        salvar_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getSystemService(NovoLocal.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(NovoLocal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NovoLocal.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                EditText lat = findViewById(R.id.edt_latitude);
                EditText lon = findViewById(R.id.edt_longitude);

                lat.setText(String.valueOf(latitude));
                lon.setText(String.valueOf(longitude));
                local.setLatitude(latitude);
                local.setLongitude(longitude);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
