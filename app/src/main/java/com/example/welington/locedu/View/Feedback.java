package com.example.welington.locedu.View;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.welington.locedu.Model.Constants;
import com.example.welington.locedu.R;

import java.util.HashMap;
import java.util.Map;


public class Feedback extends AppCompatActivity {

    private RatingBar avaliacao;
    private RadioButton rb_sim, rb_nao;
    private TextView valorAvaliacao;
    private EditText reclamacao;

    ProgressDialog progressDialog;
    ImageButton enviar;
    //EditText edtName, edtPhone;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        avaliacao = findViewById(R.id.ratb_avaliacao);
        valorAvaliacao = findViewById(R.id.tv_valorRat);
        reclamacao = findViewById(R.id.edt_relama);
        rb_sim = findViewById(R.id.rb_sim);
        rb_nao = findViewById(R.id.rb_nao);
        avaliacao.setOnRatingBarChangeListener(rat);
        enviar = findViewById(R.id.imgb_enviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resp = "";
                if(rb_sim.isChecked()){
                    resp = "sim";
                }
                else if(rb_nao.isChecked()){
                    resp = "não";
                }
                postData(resp,reclamacao.getText().toString(), valorAvaliacao.getText().toString());

                Toast.makeText(Feedback.this, "Obrigado pelo feedback", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        queue = Volley.newRequestQueue(getApplicationContext());
    }
    RatingBar.OnRatingBarChangeListener rat = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            valorAvaliacao.setText(rating+"");
        }
    };

    public void postData(final String chegouDestino, final String sujestao, final  String pontuacao) {

        //progressDialog.show();
        StringRequest request = new StringRequest(
                com.android.volley.Request.Method.POST,
                Constants.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Snackbar.make(enviar, "Successfully Posted", Snackbar.LENGTH_LONG).show();
                            //edtName.setText(null);
                            //edtPhone.setText(null);
                        } else {
                            Snackbar.make(enviar, "Try Again", Snackbar.LENGTH_LONG).show();
                        }
                        //progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Snackbar.make(enviar, "Error while Posting Data", Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.chegouDestino, chegouDestino);
                params.put(Constants.sujestao, sujestao);
                params.put(Constants.pontuacao, pontuacao);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
