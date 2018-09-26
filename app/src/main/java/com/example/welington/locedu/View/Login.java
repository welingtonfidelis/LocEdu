package com.example.welington.locedu.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.welington.locedu.Helper.ReferencesHelper;
import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
/**
 * Demonstrate Firebase Authentication using a custom minted token. For more information, see:
 * https://firebase.google.com/docs/auth/android/custom-auth
 */
public class Login extends AppCompatActivity {

    private EditText usuario;
    private EditText senha;
    private Button logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = findViewById(R.id.editTextUsuario);
        senha = findViewById(R.id.editTextSenha);
        logar = findViewById(R.id.buttonLogar);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usuario.getText().toString();
                String password = senha.getText().toString();

                ReferencesHelper.getFirebaseAuth().signInWithEmailAndPassword(user, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startListaSetor();
                                }else{
                                    Toast.makeText(Login.this,"Credencial inválida", Toast.LENGTH_LONG ).show();
                                }
                            }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
            startListaSetor();
        }
    }

    protected void startListaSetor(){
        Intent it = new Intent(Login.this, ListaSetor.class);
        startActivity(it);
        finish();
    }
}

