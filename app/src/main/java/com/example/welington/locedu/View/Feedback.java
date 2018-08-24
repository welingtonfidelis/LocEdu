package com.example.welington.locedu.View;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.welington.locedu.R;

public class Feedback extends AppCompatActivity {

    private RatingBar avaliacao;
    private TextView valorAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        avaliacao = findViewById(R.id.ratb_avaliacao);
        valorAvaliacao = findViewById(R.id.tv_valorRat);

        avaliacao.setOnRatingBarChangeListener(rat);

    }
    RatingBar.OnRatingBarChangeListener rat = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            valorAvaliacao.setText(rating+"");
        }
    };
}
