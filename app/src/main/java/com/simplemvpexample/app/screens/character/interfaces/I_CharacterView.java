package com.simplemvpexample.app.screens.character.interfaces;

import android.content.Intent;
import android.net.Uri;

public interface I_CharacterView {

    void setImage(Uri imageUri);

    void setNoPicture();

    void sendScanBroadcast(Intent scanIntent);

    void requestPermissions(String[] permissions, int code);

    void openImageChooser(Intent chooser);

    void noStoragePermissionMssg();

    void noNameSetMssg();

    void noMovieSetMssg();

    void backToList();
}
