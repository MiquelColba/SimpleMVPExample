package com.simplemvpexample.app.screens.character;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.simplemvpexample.app.CharactersApp;
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.di.component.ActivityComponent;
import com.simplemvpexample.app.di.component.DaggerActivityComponent;
import com.simplemvpexample.app.di.module.ActivityModule;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterPresenter;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterView;

import javax.inject.Inject;

public class CharacterView extends AppCompatActivity implements TextView.OnEditorActionListener, I_CharacterView {

    private ImageView picture;
    private EditText name;
    private EditText movie;
    private FloatingActionButton editPicture;

    private ActivityComponent activityComponent;

    private boolean isEdit = false;

    private static final int IMAGE_SELECTION = 122;

    @Inject
    I_CharacterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.new_character );

        picture = findViewById( R.id.ivNewPicture );
        name = findViewById( R.id.etName );
        movie = findViewById( R.id.etMovie );
        editPicture = findViewById( R.id.fabEdit );

        getActivityComponent().inject( this );

        presenter.onAttach( this );

        name.setOnEditorActionListener( this );
        movie.setOnEditorActionListener( this );
        editPicture.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editPictureClicked();
            }
        } );


        if (getIntent().hasExtra( "character" )) {

            // Visualize character information
            CustomCharacter editableCharacter = getIntent().getParcelableExtra( "character" );

            name.setText( editableCharacter.getName() );
            movie.setText( editableCharacter.getMovie() );
            isEdit = true;

            presenter.editCharacter( editableCharacter );

        } else {

            presenter.newCharacter();
        }
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

    @Override
    public void setImage(Uri imageUri) {
        Glide.with( this )
                .load( imageUri )
                .error(
                        Glide.with( this )
                                .load( getDrawable( R.drawable.no_picture ) )
                                .circleCrop()
                )
                .circleCrop()
                .into( picture );
    }

    @Override
    public void setNoPicture() {
        Glide.with( this )
                .load( getDrawable( R.drawable.no_picture ) )
                .circleCrop()
                .into( picture );
    }

    @Override
    public void sendScanBroadcast(Intent scanIntent) {
        this.sendBroadcast( scanIntent );
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.toggleSoftInput( InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS );

            if (v.getId() == R.id.etName) {
                String name = v.getText().toString();
                presenter.nameAdded( name );
                return true;
            }

            if (v.getId() == R.id.etMovie) {
                String movie = v.getText().toString();
                presenter.movieAdded( movie );
                return true;
            }
        }

        return false;
    }

    @Override
    public void openImageChooser(Intent chooser) {
        startActivityForResult( chooser, IMAGE_SELECTION );
    }

    @Override
    public void noStoragePermissionMssg() {
        Toast.makeText( this, R.string.storage_permission_needed, Toast.LENGTH_LONG ).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == IMAGE_SELECTION && resultCode == RESULT_OK) {
            presenter.onActivityResult( data );
        }
    }

    @Override
    public void noNameSetMssg() {
        Toast.makeText( this, R.string.default_no_name, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void noMovieSetMssg() {
        Toast.makeText( this, R.string.default_no_movie, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if (isEdit) {
            inflater.inflate( R.menu.save_delete_menu, menu );
        } else {
            inflater.inflate( R.menu.save_menu, menu );
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                presenter.onSaveCliked();
                return true;
            case R.id.delete:
                presenter.onDeleteClicked();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        presenter.onRequestPermissionResults( requestCode, permissions, grantResults );
    }

    @Override
    public void backToList() {
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onViewDestroy();
        super.onDestroy();
    }
}
