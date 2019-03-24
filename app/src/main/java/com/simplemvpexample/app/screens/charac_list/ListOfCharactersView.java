package com.simplemvpexample.app.screens.charac_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simplemvpexample.app.CharactersApp;
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.di.component.ActivityComponent;
import com.simplemvpexample.app.di.component.DaggerActivityComponent;
import com.simplemvpexample.app.di.module.ActivityModule;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCPresenter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCView;
import com.simplemvpexample.app.screens.character.CharacterView;

import javax.inject.Inject;

public class ListOfCharactersView extends AppCompatActivity implements I_ListOfCView {

    private FloatingActionButton addCharacter;
    private RecyclerView charactersList;
    private TextView noCharactersMssg;
    private LinearLayoutManager layoutManager;
    private ActivityComponent activityComponent;
    private CharactersListAdapter adapter;

    @Inject
    I_ListOfCPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.list_of_characters );

        noCharactersMssg = findViewById( R.id.tvNoCharacters );
        addCharacter = findViewById( R.id.fabAdd );
        charactersList = findViewById( R.id.rvList );

        getActivityComponent().inject( this );

        charactersList.setHasFixedSize( true );
        adapter = new CharactersListAdapter( this );
        setUpRecyclerView();

        addCharacter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListOfCharactersView.this, CharacterView.class );
                startActivity( intent );
            }
        } );

        presenter.onAttach( this );
        presenter.attachAdapter( adapter );
        presenter.startPresenter();
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {

            activityComponent = DaggerActivityComponent
                    .builder()
                    .activityModule( new ActivityModule( this ) )
                    .applicationComponent( CharactersApp.get( this ).getComponent() )
                    .build();
        }

        return activityComponent;
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
