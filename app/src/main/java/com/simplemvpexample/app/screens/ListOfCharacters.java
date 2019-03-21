package com.simplemvpexample.app.screens;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.DBListener;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class ListOfCharacters extends AppCompatActivity implements DBListener {

    private FloatingActionButton addCharacter;
    private CharactersDB charactersDB;
    private RecyclerView charactersList;
    private CharactersListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static String TAG = "EvilCharacters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.list_of_characters );

        addCharacter = findViewById( R.id.fabAdd );

        addCharacter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListOfCharacters.this, NewCharacter.class );
                startActivity( intent );
            }
        } );

        charactersDB = new CharactersDB( this );

        charactersList = findViewById( R.id.rvList );
        charactersList.setHasFixedSize( true );
        adapter = new CharactersListAdapter( this );
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager( this );
        charactersList.setLayoutManager( layoutManager );

        charactersList.setAdapter( adapter );

    }

    @Override
    public void onCharactersAvailable(List<EvilCharacter> characters) {

        if (characters != null) {
            if (adapter != null) {
                adapter.setData( characters );
            }
        }
    }

    @Override
    protected void onResume() {

        charactersDB.getAllCharacters(this);

        super.onResume();
    }

    public void viewCharactersDetails(EvilCharacter character) {
        Intent detailsIntent = new Intent( this, CharacterDetails.class );
        detailsIntent.putExtra( "character", character );
        startActivity( detailsIntent );
    }
}
