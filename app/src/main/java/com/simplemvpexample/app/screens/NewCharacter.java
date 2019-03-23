package com.simplemvpexample.app.screens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class NewCharacter extends AppCompatActivity implements TextView.OnEditorActionListener {

    private ImageView picture;
    private EditText name;
    private EditText movie;
    private FloatingActionButton editPicture;
    private EvilCharacter character;
    private CharactersDB charactersDB;

    private boolean isEdit = false;

    private static final int CAMERA_PERMISSION_CHECK = 44;
    private static final int STORAGE_PERMISSION_CHECK = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.new_character );

        charactersDB = new CharactersDB( this );

        picture = findViewById( R.id.ivNewPicture );

        name = findViewById( R.id.etName );
        name.setOnEditorActionListener( this );

        movie = findViewById( R.id.etMovie );
        movie.setOnEditorActionListener( this );

        editPicture = findViewById( R.id.fabEdit );

        editPicture.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions()) {
                    openImageChooser();
                }
            }
        } );

        if (getIntent().hasExtra( "character" )) {
            // Visualize character information
            character = getIntent().getParcelableExtra( "character" );

            if (character != null) {

                isEdit = true;

                name.setText( character.getName() );
                movie.setText( character.getMovie() );

                Uri imageUri = character.getImage() != null ? Uri.parse( character.getImage() ) : null;

                setImagePicture( imageUri );
            }

        } else {
            character = new EvilCharacter();
            setImagePicture( null );
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.toggleSoftInput( InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS );

            if (v.getId() == R.id.etName) {

                String name = v.getText().toString();

                character.setName( name );
                return true;
            }

            if (v.getId() == R.id.etMovie) {

                String movie = v.getText().toString();

                character.setMovie( movie );
            }
        }

        return false;
    }

    private void openImageChooser() {

        // Configure EasyImage to disable multiple image pick from gallery
        EasyImage.configuration( this )
                .setImagesFolderName( "Evil characters" )
                .setAllowMultiplePickInGallery( false );

        EasyImage.openChooserWithGallery( this, "Image Chooser", 0 );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                // Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(NewCharacter.this);
                    if (photoFile != null) photoFile.delete();
                }
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the image

                if (imagesFiles != null && imagesFiles.size() > 0) {

                    Uri imageUri = Uri.fromFile( imagesFiles.get( 0 ) );
                    setImagePicture( imageUri );
                }
            }
        });
    }

    private void setImagePicture(Uri imageUri) {

        if (imageUri == null) {
            Glide.with( this )
                    .load( getDrawable( R.drawable.no_picture ) )
                    .circleCrop()
                    .into( picture );

            return;
        }

        character.setImage( imageUri.toString() );

        Glide.with( this )
                .load( imageUri )
                .circleCrop()
                .into( picture );
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
                // Check that character has at least a name and a movie
                if (validateCharacter()) {

                    if (!isEdit) {
                        // Save in DB and go back
                        charactersDB.insertCharacter( character );
                    } else {
                        // Update element in DB
                        charactersDB.updateCharacter( character );
                    }

                    finish();
                }

                return true;

            case R.id.delete:
                charactersDB.deleteCharacter( character );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private boolean validateCharacter() {

        if (character.getName() == null || character.getName().equalsIgnoreCase( "" )) {

            Toast.makeText( this, R.string.default_no_name, Toast.LENGTH_SHORT ).show();

            return false;
        }

        if (character.getMovie() == null || character.getMovie().equalsIgnoreCase( "" )) {

            Toast.makeText( this, R.string.default_no_movie, Toast.LENGTH_SHORT ).show();

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        switch (requestCode) {
            case CAMERA_PERMISSION_CHECK: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageChooser();
                }
                return;
            }
            case STORAGE_PERMISSION_CHECK: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageChooser();
                }
                return;
            }
        }
    }

    private boolean hasPermissions() {

        PackageManager packageManager = getPackageManager();

        if (packageManager.checkPermission( Manifest.permission.CAMERA, getPackageName() ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CHECK );
            return false;
        }

        if (packageManager.checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName() ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CHECK );
            return false;
        }

        return true;
    }
}
