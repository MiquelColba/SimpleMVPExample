package com.simplemvpexample.app.screens.character;

import android.content.Context;

import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.DBHelper;
import com.simplemvpexample.app.data.db.DBListener;
import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.List;

public class CharacterInteractor implements I_CharacterInteractor, DBListener {

    private DBHelper dbHelper;
    private I_CharacterPresenter presenter;

    public CharacterInteractor(Context context) {
        dbHelper = CharactersDB.getInstance( context );
        dbHelper.registerListener( this );
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
    public void onAttachPresenter(I_CharacterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDetachPresenter() {
        dbHelper.unregisterListener( this );
        this.presenter = null;
    }
}
