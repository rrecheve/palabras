package com.example.palabras;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NUEVA_PALABRA_REQUEST_CODE = 1;

    PalabraDao mPalabraDao;

    PalabrasListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter= new PalabrasListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PalabrasRoomDB db = PalabrasRoomDB.getDatabase(this);
        mPalabraDao = db.palabraDao();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NuevaPalabraActivity.class);
                startActivityForResult(intent, NUEVA_PALABRA_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        DBTask queryWords = new DBTask(this);
        queryWords.execute();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NUEVA_PALABRA_REQUEST_CODE && resultCode == RESULT_OK) {
            Palabra palabra = new Palabra(data.getStringExtra(NuevaPalabraActivity.EXTRA_REPLY));
            PalabrasRoomDB.databaseWriteExecutor.execute(() -> {
                mPalabraDao.insert(palabra);
            });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private static class DBTask extends AsyncTask<Void, Void, List<Palabra>> {


        private final WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        DBTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Palabra> doInBackground(Void... voids) {
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;

            return activity.mPalabraDao.getAlphabetizedWords();
        }

        @Override
        protected void onPostExecute(List<Palabra> palabras) {
            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.adapter.setWords(palabras);


        }

    }
}
