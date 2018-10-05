package com.example.welington.locedu.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.welington.locedu.Helper.PermissionHelper;
import com.example.welington.locedu.R;

public class SplashScreen extends AppCompatActivity {

    private Handler handler;
    private ImageView imageView;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.logo_id);
        rotacionarImagem();

        if(PermissionHelper.request(1,SplashScreen.this,permissions)){
            startAplication();
        }
    }

    private void startAplication() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent it = new Intent(SplashScreen.this, Home.class);
                startActivity(it);
                finish();
            }
        },2100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startAplication();
            } else {
                Toast.makeText(SplashScreen.this, "Negado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void rotacionarImagem(){
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setInterpolator(new LinearInterpolator());

        imageView.startAnimation(rotate);
    }
}

