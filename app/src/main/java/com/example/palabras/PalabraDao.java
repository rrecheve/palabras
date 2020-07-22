package com.example.palabras;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PalabraDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Palabra palabra);

    @Query("DELETE FROM tabla_palabra")
    void deleteAll();

    @Query("SELECT * from tabla_palabra ORDER BY palabra ASC")
    List<Palabra> getAlphabetizedWords();
}
