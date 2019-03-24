package com.simplemvpexample.app.screens.character.interfaces;

import android.content.Intent;

import com.simplemvpexample.app.data.model.CustomCharacter;

public interface I_CharacterPresenter {

    void onActivityResult( Intent data);

    void onRequestPermissionResults(int reqCode, String[] permissions, int[] grantResults);

    void nameAdded(String name);

    void movieAdded(String movie);

    void editPictureClicked();

    void onSaveCliked();

    void onDeleteClicked();

    void dbActionPerformed();

    void newCharacter();

    void editCharacter(CustomCharacter character);

    void onAttach(I_CharacterView view);

    void onViewDestroy();
}
