package com.simplemvpexample.app.screens;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.EvilCharacter;

public class CharacterDetails extends AppCompatActivity {

    private ImageView picture;
    private FloatingActionButton editPicture;
    private TextView name;
    private TextView movie;

    private EvilCharacter character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.character_details );

        character = getIntent().getParcelableExtra( "character" );

        picture = findViewById( R.id.ivNewPicture );

        editPicture = findViewById( R.id.fabEdit );

        name = findViewById( R.id.tvName );
        name.setText( character.getName() );

        movie = findViewById( R.id.tvMovie );
        movie.setText( character.getMovie() );

        if (character.getImage().equalsIgnoreCase( "" )) {

            Glide.with( this )
                    .load( getDrawable( R.drawable.no_picture ) )
                    .circleCrop()
                    .into( picture );
        } else {

            Uri image = Uri.parse( character.getImage() );

            Glide.with( this )
                .load( image )
                .circleCrop()
                .into( picture );
        }
    }
}
