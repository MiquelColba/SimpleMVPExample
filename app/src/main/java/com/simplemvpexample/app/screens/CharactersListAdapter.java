package com.simplemvpexample.app.screens;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class CharactersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EvilCharacter> characters;


    public CharactersListAdapter(List<EvilCharacter> characters) {
        this.characters = characters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View cell = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.list_cell, viewGroup, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView movie;
        ImageView picture;

        public CharacterViewHolder(@NonNull View view) {
            super( view );

            name = view.findViewById( R.id.tvCellName );
            movie = view.findViewById( R.id.tvCellMovie );
            picture = view.findViewById( R.id.ivCellPicture );
        }
    }

}
