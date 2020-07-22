package com.example.palabras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PalabrasListAdapter extends RecyclerView.Adapter<PalabrasListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView palabraItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            palabraItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Palabra> mPalabras;

    PalabrasListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mPalabras != null) {
            Palabra current = mPalabras.get(position);
            holder.palabraItemView.setText(current.getPalabra());
        } else {
            holder.palabraItemView.setText("No Palabra");
        }
    }

    void setWords(List<Palabra> palabras){
        mPalabras = palabras;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPalabras != null)
            return mPalabras.size();
        else return 0;
    }
}
