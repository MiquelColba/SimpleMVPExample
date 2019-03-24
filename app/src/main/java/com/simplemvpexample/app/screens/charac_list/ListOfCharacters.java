package com.simplemvpexample.app.screens.charac_list;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCPresenter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCView;
import com.simplemvpexample.app.screens.character.CharacterView;

public class ListOfCharacters extends AppCompatActivity implements I_ListOfCView {

    private FloatingActionButton addCharacter;
    private RecyclerView charactersList;
    private TextView noCharactersMssg;
    private CharactersListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private I_ListOfCPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.list_of_characters );

        noCharactersMssg = findViewById( R.id.tvNoCharacters );
        addCharacter = findViewById( R.id.fabAdd );
        charactersList = findViewById( R.id.rvList );

        charactersList.setHasFixedSize( true );
        adapter = new CharactersListAdapter( this );
        setUpRecyclerView();

        presenter = new ListOfCharactersPresenter( this );
        presenter.attachAdapter( adapter );

        addCharacter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListOfCharacters.this, CharacterView.class );
                startActivity( intent );
            }
        } );

        presenter.startPresenter();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager( this );
        charactersList.setLayoutManager( layoutManager );

        charactersList.setAdapter( adapter );

    }

    @Override
    public void showNoCharacters() {
        noCharactersMssg.setVisibility( View.VISIBLE );
    }

    @Override
    public void hideNoCharacters() {
        noCharactersMssg.setVisibility( View.GONE );
    }

    @Override
    public void viewCharacterDetails(CustomCharacter character) {
        Intent detailsIntent = new Intent( this, CharacterView.class );
        detailsIntent.putExtra( "character", character );
        startActivity( detailsIntent );
    }

    @Override
    protected void onDestroy() {

        if (charactersList != null && charactersList.getAdapter() != null) {
            adapter = null;
            charactersList.setAdapter( null );
        }

        presenter.onViewDestroy();

        super.onDestroy();
    }
}
