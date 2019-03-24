package com.simplemvpexample.app.screens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.model.EvilCharacter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCharacter extends AppCompatActivity implements TextView.OnEditorActionListener {

    private ImageView picture;
    private EditText name;
    private EditText movie;
    private FloatingActionButton editPicture;
    private EvilCharacter character;
    private CharactersDB charactersDB;

    private boolean isEdit = false;
    String currentPhotoPath = null;
    boolean cameraPermission = false;
    boolean storagePermission = false;

    private static final int PERMISSION_CHECK = 44;

    private static final int IMAGE_SELECTION = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.new_character );

        charactersDB = CharactersDB.getInstance( this );

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


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        Intent chooser = Intent.createChooser(photoPickerIntent, getString( R.string.image_chooser_title ));

        if (cameraPermission && storagePermission) {


            File photoFile =  extFile();

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (photoFile != null) {

                cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( photoFile )); // photoUri );
            }

            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });
        }

        startActivityForResult(chooser, IMAGE_SELECTION );

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == IMAGE_SELECTION && resultCode == RESULT_OK) {

            if ( data != null ) {

                String path = getRealPathFromURI( this, data.getData() );
                Uri imageUri = Uri.fromFile( new File( path ) );
                setImagePicture( imageUri );

            } else if (currentPhotoPath != null) {

                galleryAddPic();

                setImagePicture( Uri.fromFile( new File( currentPhotoPath ) ) );

                currentPhotoPath = null;
            }

        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File extFile() {
        String name = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date(  ) );

        File extStorage = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        File f = new File( extStorage, name + ".jpg" );

        currentPhotoPath = f.getAbsolutePath();
        return f;
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
                .error(
                        Glide.with( this )
                                .load( getDrawable( R.drawable.no_picture ) )
                                .circleCrop()
                )
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

        if (requestCode == PERMISSION_CHECK && grantResults.length > 0) {

            for (int i = 0; i < grantResults.length; i++) {

                if (permissions[i].equalsIgnoreCase( Manifest.permission.CAMERA )) {

                    cameraPermission = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                } else if (permissions[i].equalsIgnoreCase( Manifest.permission.WRITE_EXTERNAL_STORAGE )) {

                    storagePermission = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
        }

        openImageChooser();
    }

    private boolean hasPermissions() {

        PackageManager packageManager = getPackageManager();

        cameraPermission = packageManager.checkPermission( Manifest.permission.CAMERA, getPackageName() ) == PackageManager.PERMISSION_GRANTED;

        storagePermission = packageManager.checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName() ) == PackageManager.PERMISSION_GRANTED;

        String[] permissions;

        if (!cameraPermission && !storagePermission) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else if (!cameraPermission) {
            permissions = new String[]{Manifest.permission.CAMERA};
        } else if (!storagePermission) {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            return true;
        }

        requestPermissions( permissions, PERMISSION_CHECK );

        return false;

    }
}
