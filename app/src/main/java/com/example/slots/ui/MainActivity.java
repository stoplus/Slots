package com.example.slots.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.slots.MyApp;
import com.example.slots.R;
import com.example.slots.entityRoom.UserDao;

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
    }
}
