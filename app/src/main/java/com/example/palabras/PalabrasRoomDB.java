package com.example.palabras;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Palabra.class}, version = 1, exportSchema = false)
public abstract class PalabrasRoomDB extends RoomDatabase {

    public abstract PalabraDao palabraDao();

    private static volatile PalabrasRoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PalabrasRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PalabrasRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PalabrasRoomDB.class, "word_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);


            databaseWriteExecutor.execute(() -> {

                PalabraDao dao = INSTANCE.palabraDao();
                dao.deleteAll();

                Palabra palabra = new Palabra("Control");
                dao.insert(palabra);
                palabra = new Palabra("Práctico");
                dao.insert(palabra);
            });
        }
    };
}