package com.simplemvpexample.app.screens;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.DBListener;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.ArrayList;
import java.util.List;

public class ListOfCharacters extends AppCompatActivity implements DBListener {

    private FloatingActionButton addChracter;

    private CharactersDB charactersDB;

    private List<EvilCharacter> evilGuys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.list_of_characters );

        addChracter = findViewById( R.id.fabAdd );

        addChracter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListOfCharacters.this, NewCharacter.class );
                startActivity( intent );
            }
        } );

        charactersDB = new CharactersDB( this );
    }

    @Override
    public void onCharactersAvailable(List<EvilCharacter> characters) {
        if (characters == null) {
            evilGuys = new ArrayList<>();
        } else {
            evilGuys = characters;
        }
    }

    @Override
    protected void onResume() {

        charactersDB.getAllCharacters(this);

        super.onResume();
    }
}
