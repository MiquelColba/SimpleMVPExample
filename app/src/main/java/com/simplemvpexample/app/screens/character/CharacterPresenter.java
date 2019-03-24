package com.simplemvpexample.app.screens.character;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.simplemvpexample.app.R;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterInteractor;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterPresenter;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CharacterPresenter implements I_CharacterPresenter {

    private I_CharacterView view;
    private I_CharacterInteractor interactor;

    private boolean isEdit;
    String currentPhotoPath = null;
    boolean cameraPermission = false;
    boolean storagePermission = false;
    private CustomCharacter character;

    private static final int PERMISSION_CHECK = 44;

    public CharacterPresenter(I_CharacterView view) {
        this.view = view;
        this.interactor = new CharacterInteractor( view.getContext() );
        interactor.onAttachPresenter( this );
        character = new CustomCharacter();
        isEdit = false;
        view.setNoPicture();
    }

    public CharacterPresenter(I_CharacterView view, CustomCharacter character) {
        this.view = view;
        this.interactor = new CharacterInteractor( view.getContext() );
        interactor.onAttachPresenter( this );
        this.character = character;
        isEdit = true;

        if (character.getImage() != null) {
            view.setImage( Uri.parse( character.getImage() ) );
        } else {
            view.setNoPicture();
        }
    }

    @Override
    public void dbActionPerformed() {
        // Insert, Update or Delete action has been done
        view.backToList();
    }

    @Override
    public void onViewDestroy() {
        interactor.onDetachPresenter();
        view = null;
    }

    @Override
    public void onActivityResult(Intent data) {


        if (data != null) {

            String path = getRealPathFromURI( view.getContext(), data.getData() );
            Uri imageUri = Uri.fromFile( new File( path ) );
            character.setImage( imageUri.toString() );
            view.setImage( imageUri );

        } else if (currentPhotoPath != null) {

            galleryAddPic();

            Uri imageUri = Uri.fromFile( new File( currentPhotoPath ) );
            character.setImage( imageUri.toString() );

            view.setImage( imageUri );

            currentPhotoPath = null;
        }

    }

    @Override
    public void nameAdded(String name) {
        character.setName( name );
    }

    @Override
    public void movieAdded(String movie) {
        character.setMovie( movie );
    }

    @Override
    public void editPictureClicked() {

        if (checkPermissions()) {
            openImageChooser();
        }
    }

    @Override
    public void onSaveCliked() {
        if (validateCharacter()) {
            interactor.saveCharacter( character, isEdit );
        }
    }

    @Override
    public void onDeleteClicked() {
        interactor.deleteCharacter( character );
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File f = new File( currentPhotoPath );
        Uri contentUri = Uri.fromFile( f );
        mediaScanIntent.setData( contentUri );

        view.sendScanBroadcast( mediaScanIntent );
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query( contentUri, proj, null, null, null );
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cursor.moveToFirst();
            return cursor.getString( column_index );
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionResults(int reqCode, String[] permissions, int[] grantResults) {

        if (reqCode == PERMISSION_CHECK && grantResults.length > 0) {

            for (int i = 0; i < grantResults.length; i++) {

                if (permissions[i].equalsIgnoreCase( Manifest.permission.CAMERA )) {

                    cameraPermission = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                } else if (permissions[i].equalsIgnoreCase( Manifest.permission.WRITE_EXTERNAL_STORAGE )) {

                    storagePermission = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            openImageChooser();
        }
    }

    private void requestPermissions() {

        String[] permissions = null;

        if (!cameraPermission && !storagePermission) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else if (!cameraPermission) {
            permissions = new String[]{Manifest.permission.CAMERA};
        } else if (!storagePermission) {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        view.requestPermissions( permissions, PERMISSION_CHECK );
    }

    private boolean checkPermissions() {

        PackageManager packageManager = view.getContext().getPackageManager();
        String packageName = view.getContext().getPackageName();

        cameraPermission = packageManager.checkPermission( Manifest.permission.CAMERA, packageName ) == PackageManager.PERMISSION_GRANTED;

        storagePermission = packageManager.checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName ) == PackageManager.PERMISSION_GRANTED;

        if (!storagePermission || !cameraPermission) {
            requestPermissions();
            return false;
        }

        return true;
    }

    private void openImageChooser() {

        if (!storagePermission) {
            view.noStoragePermissionMssg();
        } else {
            Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
            photoPickerIntent.setType( "image/*" );

            Intent chooser = Intent.createChooser( photoPickerIntent, view.getContext().getString( R.string.image_chooser_title ) );

            if (cameraPermission) {

                File photoFile = extFile();

                Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                if (photoFile != null) {
                    cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( photoFile ) );
                }

                chooser.putExtra( Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent} );
            }

            view.openImageChooser( chooser );
        }

    }

    private File extFile() {
        String name = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date(  ) );

        File extStorage = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        File f = new File( extStorage, name + ".jpg" );

        currentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private boolean validateCharacter() {

        if (character.getName() == null || character.getName().equalsIgnoreCase( "" )) {
            view.noNameSetMssg();
            return false;
        }

        if (character.getMovie() == null || character.getMovie().equalsIgnoreCase( "" )) {
            view.noMovieSetMssg();
            return false;
        }

        return true;
    }

}
