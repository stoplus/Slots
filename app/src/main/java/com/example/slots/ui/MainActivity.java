package com.example.slots.ui;

import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.slots.MyApp;
import com.example.slots.R;
import com.example.slots.entityRoom.UserDao;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
//    @Inject
//    CardDao cardDao;
//    @Inject
//    HistoryDao historyDao;
    @Inject
UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApp.app().appComponent().inject(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final TextView jackpot = findViewById(R.id.textView_jackpot);
        final TextView firstEditText = findViewById(R.id.textViewCombinations);
        final TextView textView_field_lines_descript = findViewById(R.id.textView_field_lines_descript);
        final TextView coins_descript = findViewById(R.id.coins_descript);
        final TextView field_bet_descript = findViewById(R.id.field_bet_descript);

        firstEditText.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        textView_field_lines_descript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        coins_descript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        field_bet_descript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        jackpot.setTypeface(Typeface.createFromAsset(getAssets(), "Bodonio.ttf"));
    }

    public void btnSpin(View view) {
    }

    public void button_small_minus(View view) {
    }

    public void button_small_plus(View view) {
    }

    public void button_blue_small(View view) {
    }
}
