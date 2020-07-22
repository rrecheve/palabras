package com.example.palabras;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_palabra")
public class Palabra {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "palabra")
    private String mPalabra;

    public Palabra(String palabra) {this.mPalabra = palabra;}

    public String getPalabra(){return this.mPalabra;}
}