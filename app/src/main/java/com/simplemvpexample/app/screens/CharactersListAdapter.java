package com.simplemvpexample.app.screens;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.ArrayList;
import java.util.List;

public class CharactersListAdapter extends RecyclerView.Adapter<CharactersListAdapter.CharacterViewHolder> {

    private List<EvilCharacter> characters;
    private Activity parentActivity;

    private static String TAG = "EvilCharacters";

    public CharactersListAdapter(Activity parentActivity) {
        characters = new ArrayList<>();
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View cell = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.list_cell, viewGroup, false);

        return new CharacterViewHolder( cell );
    }



    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder viewHolder, int position) {

        EvilCharacter eC = characters.get( position );

        viewHolder.name.setText( eC.getName() );
        viewHolder.movie.setText( eC.getMovie() );

        if (eC.getImage() != null && !eC.getImage().equalsIgnoreCase( "" )) {
            Uri imageUri = Uri.parse( eC.getImage() );

            Glide.with( parentActivity )
                    .load( imageUri )
                    .into( viewHolder.picture);
        } else {

            Glide.with( parentActivity )
                    .load( parentActivity.getDrawable( R.drawable.no_picture ) )
                    .into( viewHolder.picture);
        }
    }

    @Override
    public int getItemCount() {
        Log.d( TAG, "ADAPTER - getItemCount(): " + characters.size() );
        return characters.size();
    }

    public void setData(List<EvilCharacter> listOfCharacters) {

        if (characters != null && !characters.isEmpty()) {
            characters.clear();
        }

        characters.addAll( listOfCharacters );

        notifyDataSetChanged();
    }


    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView movie;
        ImageView picture;

        public CharacterViewHolder(@NonNull View view) {
            super(view);

            name = view.findViewById( R.id.tvCellName );
            movie = view.findViewById( R.id.tvCellMovie );
            picture = view.findViewById( R.id.ivCellPicture );
        }
    }

}
