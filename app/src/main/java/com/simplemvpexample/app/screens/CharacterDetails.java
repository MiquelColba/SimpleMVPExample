package com.simplemvpexample.app.screens;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simplemvpexample.app.R;

public class CharacterDetails extends AppCompatActivity {

    private ImageView picture;
    private FloatingActionButton editPicture;
    private TextView name;
    private TextView movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.character_details );

        picture = findViewById( R.id.ivNewPicture );

        editPicture = findViewById( R.id.fabEdit );

        name = findViewById( R.id.tvName );

        movie = findViewById( R.id.tvMovie );

        Glide.with( this )
                .load( getDrawable( R.drawable.no_picture ) )
                .circleCrop()
                .into( picture );
    }
}
