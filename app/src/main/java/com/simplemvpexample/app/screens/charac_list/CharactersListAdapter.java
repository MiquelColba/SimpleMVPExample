package com.simplemvpexample.app.screens.charac_list;

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
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCAdapter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCPresenter;
import com.simplemvpexample.app.screens.charac_list.interfaces.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CharactersListAdapter extends RecyclerView.Adapter<CharactersListAdapter.CharacterViewHolder> implements ItemClickListener, I_ListOfCAdapter {

    private List<CustomCharacter> characters;
    private ListOfCharacters parentActivity;
    private I_ListOfCPresenter presenter;

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

        CustomCharacter eC = characters.get( position );

        viewHolder.name.setText( eC.getName() );
        viewHolder.movie.setText( eC.getMovie() );

        if (eC.getImage() != null && !eC.getImage().equalsIgnoreCase( "" )) {

            Uri imageUri = Uri.parse( eC.getImage() );

            Glide.with( parentActivity )
                    .load( imageUri )
                    .error(
                            Glide.with( parentActivity)
                                    .load( parentActivity.getDrawable( R.drawable.no_picture ) )
                                    .circleCrop()
                    )
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

    @Override
    public void setCharacters(List<CustomCharacter> charactersList) {
        if (characters != null && !characters.isEmpty()) {
            characters.clear();
        }

        characters = charactersList;

        notifyDataSetChanged();
    }

    @Override
    public void characterInserted(int index) {
        notifyItemInserted( index );
    }

    @Override
    public void characterRemoved(int index) {
        notifyItemRemoved( index );
    }

    @Override
    public void characterUpdated(int index) {
        notifyItemChanged( index );
    }

    @Override
    public void onAttach(I_ListOfCPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDetach() {
        presenter = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        presenter.onCharacterSelected( characters.get( position ) );
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
