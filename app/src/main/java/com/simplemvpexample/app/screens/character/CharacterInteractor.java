package com.simplemvpexample.app.screens.character;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.simplemvpexample.app.data.db.interfaces.DBHelper;
import com.simplemvpexample.app.data.db.interfaces.DBListener;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.di.ActivityContext;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterInteractor;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterPresenter;

import java.util.List;

import javax.inject.Inject;

public class CharacterInteractor implements I_CharacterInteractor, DBListener {

    private DBHelper dbHelper;
    private I_CharacterPresenter presenter;
    private Context context;

    @Inject
    public CharacterInteractor(@ActivityContext Context context, DBHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.dbHelper.registerListener( this );
    }

    @Override
    public void saveCharacter(CustomCharacter character, boolean isEdit) {
        if (isEdit) {
            dbHelper.updateCharacter( character );
        } else {
            dbHelper.insertCharacter( character );
        }
    }

    @Override
    public void deleteCharacter(CustomCharacter character) {
        dbHelper.deleteCharacter( character );
    }

    @Override
    public void onCharactersAvailable(List<CustomCharacter> characters) {
        // No use here
    }

    @Override
    public void onCharacterDeleted(int characterID) {
        presenter.dbActionPerformed();
    }

    @Override
    public void onCharacterUpdated(CustomCharacter character) {
        presenter.dbActionPerformed();
    }

    @Override
    public void onCharacterInserted(CustomCharacter character) {
        presenter.dbActionPerformed();
    }

    @Override
    public String getRealPathFromURI(Uri contentUri) {
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
    public void onAttachPresenter(I_CharacterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean hasCameraPermission() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        return packageManager.checkPermission( Manifest.permission.CAMERA, packageName ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean hasStoragePermission() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        return packageManager.checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onDetachPresenter() {
        dbHelper.unregisterListener( this );
        this.presenter = null;
    }
}
