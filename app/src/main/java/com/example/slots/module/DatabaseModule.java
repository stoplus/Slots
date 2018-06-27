package com.example.slots.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.slots.database.MyDatabase;
import com.example.slots.entityRoom.GameDataDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton @Provides
    public MyDatabase provideMyDatabase(Context context){
        return Room.databaseBuilder(context, MyDatabase.class, "my-db").build();
    }

//    @Singleton @Provides
//    public CardDao provideCardDao(MyDatabase myDatabase){
//        return myDatabase.cardDao();
//    }

    @Singleton @Provides
    public GameDataDao provideUserDao(MyDatabase myDatabase){
        return myDatabase.gameDataDao();
    }

//    @Singleton @Provides
//    public HistoryDao provideHistoryDao(MyDatabase myDatabase){
//        return myDatabase.historyDao();
//    }

}//DatabaseModule

