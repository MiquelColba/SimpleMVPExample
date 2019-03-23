package com.simplemvpexample.app.screens;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class CharactersListAdapter extends RecyclerView.Adapter<CharactersListAdapter.CharacterViewHolder> implements ItemClickListener {

    private List<EvilCharacter> characters;
    private ListOfCharacters parentActivity;

    private static String TAG = "EvilCharacters";

    public CharactersListAdapter(ListOfCharacters parentActivity) {
        characters = new ArrayList<>();
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View cell = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.list_cell, viewGroup, false);

        CharacterViewHolder vh = new CharacterViewHolder( cell, this );

        return vh;
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
                    .circleCrop()
                    .into( viewHolder.picture);

        } else {

            Glide.with( parentActivity )
                    .load( parentActivity.getDrawable( R.drawable.no_picture ) )
                    .circleCrop()
                    .into( viewHolder.picture);

        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setData(List<EvilCharacter> listOfCharacters) {

        if (characters != null && !characters.isEmpty()) {
            characters.clear();
        }

        characters.addAll( listOfCharacters );

        notifyDataSetChanged();
    }

    public void deleteCharacter(int characterId) {
        if (characters != null && !characters.isEmpty()) {

            int idx = -1;

            for (int i = 0; i < characters.size(); i++) {

                if (characters.get( i ).getId() == characterId) {
                    idx = i;
                    break;
                }
            }

            if (idx != -1) {
                characters.remove( idx );
                notifyItemRemoved( idx );
            }
        }
    }

    public void updateCharacter(EvilCharacter character) {

        if (characters != null && !characters.isEmpty()) {

            int characterId = character.getId();

            int idx = -1;

            for (int i = 0; i < characters.size(); i++) {

                if (characters.get( i ).getId() == characterId) {
                    idx = i;
                    break;
                }
            }

            if (idx != -1) {
                characters.set( idx, character );
                notifyItemChanged( idx );
            }
        }
    }

    public void insertCharacter(EvilCharacter character) {

        if (characters == null) {
            characters = new ArrayList<>(  );
        }

        if (characters.add( character )) {
            notifyItemInserted( characters.size() - 1 );
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        parentActivity.viewCharactersDetails( characters.get( position ) );
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView movie;
        ImageView picture;
        ItemClickListener listener;

        public CharacterViewHolder(@NonNull View view, ItemClickListener clickListener) {

            super(view);

            name = view.findViewById( R.id.tvCellName );
            movie = view.findViewById( R.id.tvCellMovie );
            picture = view.findViewById( R.id.ivCellPicture );

            listener = clickListener;

            view.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick( v, getAdapterPosition() );
        }
    }

}
