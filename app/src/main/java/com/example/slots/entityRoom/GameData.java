package com.example.slots.entityRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "GameData")
public class GameData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "jackpot")
    private int jackpot;

    @ColumnInfo(name = "coins")
    private int coins;

    @ColumnInfo(name = "bet")
    private int bet;

    public GameData(int jackpot, int coins, int bet) {
        this.jackpot = jackpot;
        this.coins = coins;
        this.bet = bet;
    }//GameData


    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJackpot() {
        return jackpot;
    }

    public void setJackpot(int jackpot) {
        this.jackpot = jackpot;
    }
}//GameData
