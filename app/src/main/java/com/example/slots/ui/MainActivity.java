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

        final TextView firstEditText = findViewById(R.id.textViewCombinations);
        firstEditText.setTypeface(Typeface.createFromAsset(
                getAssets(), "Sextan-Bold-FFP.ttf"));
    }

    public void btnSpin(View view) {
    }
}
