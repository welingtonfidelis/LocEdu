package com.example.welington.locedu.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.welington.locedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * Demonstrate Firebase Authentication using a custom minted token. For more information, see:
 * https://firebase.google.com/docs/auth/android/custom-auth
 */
public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null){
                    Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
                    ((TextView) findViewById(R.id.usuarioLogado)).setText("logado");
                }
                else{
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                    ((TextView) findViewById(R.id.usuarioLogado)).setText("Nao logado");
                }
            }
        };

        mAuth.signInWithEmailAndPassword("admin@gmail.com", "123456").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.w("AUTH", "Falha ao efetuar o Login: ", task.getException());
                }
                else{
                    Log.d("AUTH", "Login Efetuado com sucesso!!!");
                }
            }
        });

        findViewById(R.id.buttonLogar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(),ListaSetor.class);
                startActivity(it);
                finish();
            }
        });
    }
    @Override
    public  void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
