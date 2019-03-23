package com.simplemvpexample.app.screens;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.DBListener;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class ListOfCharacters extends AppCompatActivity implements DBListener {

    private FloatingActionButton addCharacter;
    private CharactersDB charactersDB;
    private RecyclerView charactersList;
    private TextView noCharactersMssg;
    private CharactersListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static String TAG = "EvilCharacters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.list_of_characters );

        noCharactersMssg = findViewById( R.id.tvNoCharacters );

        addCharacter = findViewById( R.id.fabAdd );

        addCharacter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListOfCharacters.this, NewCharacter.class );
                startActivity( intent );
            }
        } );

        charactersDB = CharactersDB.getInstance( this );
        charactersDB.registerListener( this );

        charactersList = findViewById( R.id.rvList );
        charactersList.setHasFixedSize( true );
        adapter = new CharactersListAdapter( this );
        setUpRecyclerView();

        charactersDB.getAllCharacters();
    }

    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager( this );
        charactersList.setLayoutManager( layoutManager );

        charactersList.setAdapter( adapter );

    }

    @Override
    public void onCharactersAvailable(List<EvilCharacter> characters) {

        if (characters != null && !characters.isEmpty()) {
            if (adapter != null) {
                noCharactersMssg.setVisibility( View.GONE );
                adapter.setData( characters );
            }
        } else {
            noCharactersMssg.setVisibility( View.VISIBLE );
        }
    }

    @Override
    public void onCharacterInserted(EvilCharacter character) {
        if (character != null && adapter != null) {
            adapter.insertCharacter( character );
            if (adapter.getItemCount() > 0) {
                noCharactersMssg.setVisibility( View.GONE );
            }
        }
    }

    @Override
    public void onCharacterDeleted(int characterID) {
        if (characterID != -1 && adapter != null) {
            adapter.deleteCharacter( characterID );
        }
    }

    public void noCharactersAvailable() {
        noCharactersMssg.setVisibility( View.VISIBLE );
    }

    @Override
    public void onCharacterUpdated(EvilCharacter character) {
        if (character != null && adapter != null) {
            adapter.updateCharacter( character );
        }
    }

    public void viewCharactersDetails(EvilCharacter character) {
        Intent detailsIntent = new Intent( this, NewCharacter.class );
        detailsIntent.putExtra( "character", character );
        startActivity( detailsIntent );
    }

    @Override
    protected void onDestroy() {
        if (charactersDB != null) {
            charactersDB.unregisterListener();
        }

        if (charactersList != null && charactersList.getAdapter() != null) {
            adapter = null;
            charactersList.setAdapter( null );
        }

        super.onDestroy();
    }
}
