package com.example.slots;

import android.app.Application;

import com.example.slots.component.DaggerDataBaseComponent;
import com.example.slots.component.DataBaseComponent;
import com.example.slots.module.DatabaseModule;

public class MyApp extends Application {
    private static MyApp app;
    private DataBaseComponent dataBaseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        dataBaseComponent = DaggerDataBaseComponent.builder()
                .databaseModule(new DatabaseModule(getApplicationContext()))
                .build();
    }

    public static MyApp app() {
        return app;
    }

    public DataBaseComponent appComponent() {
        return dataBaseComponent;
    }
}//class MyApp