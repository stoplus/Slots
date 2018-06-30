package com.example.slots.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import com.example.slots.entityRoom.GameData;
import com.example.slots.entityRoom.GameDataDao;

@Database(entities = {GameData.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public abstract GameDataDao gameDataDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}//class MyDatabase
