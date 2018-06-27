package com.example.slots.entityRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface GameDataDao {
    @Query("SELECT * FROM GameData ORDER BY id")
    Flowable<List<GameData>> getListAccounts();

    @Insert
    void insert(GameData... imageObjs);

    @Update
    void update(GameData imageObj);

    @Delete
    void delete(GameData... imageObj);
}
