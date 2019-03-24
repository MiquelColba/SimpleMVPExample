package com.simplemvpexample.app.screens.character;

import android.content.Intent;

public interface I_CharacterPresenter {

    void onActivityResult( Intent data);

    void onRequestPermissionResults(int reqCode, String[] permissions, int[] grantResults);

    void nameAdded(String name);

    void movieAdded(String movie);

    void editPictureClicked();

    void onSaveCliked();

    void onDeleteClicked();

    void dbActionPerformed();

    void onViewDestroy();
}
