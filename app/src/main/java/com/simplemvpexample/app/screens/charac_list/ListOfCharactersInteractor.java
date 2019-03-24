package com.simplemvpexample.app.screens.charac_list;

import android.content.Context;

import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.interfaces.DBHelper;
import com.simplemvpexample.app.data.db.interfaces.DBListener;
import com.simplemvpexample.app.data.model.CustomCharacter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCInteractor;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCPresenter;

import java.util.ArrayList;
import java.util.List;

public class ListOfCharactersInteractor implements I_ListOfCInteractor, DBListener {

    private DBHelper dbHelper;
    private I_ListOfCPresenter presenter;
    private List<CustomCharacter> characters;

    public ListOfCharactersInteractor(Context context) {
        this.dbHelper = CharactersDB.getInstance( context );
        dbHelper.registerListener( this );
    }

    @Override
    public void onAttach(I_ListOfCPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDetach() {
        dbHelper.unregisterListener( this );
        presenter = null;
    }

    @Override
    public void getCharacters() {
        dbHelper.getAllCharacters();
    }

    @Override
    public CustomCharacter getCharacter(int index) {
        return null;
    }

    @Override
    public boolean hasCharacters() {

        if (characters != null) {
            return !characters.isEmpty();
        }

        return false;
    }

    @Override
    public void onCharactersAvailable(List<CustomCharacter> characters) {

        if (this.characters == null) {
            this.characters = new ArrayList<>();
        }

        this.characters.addAll( characters );
        presenter.onCharactersAvailable( this.characters );
    }

    @Override
    public void onCharacterDeleted(int characterID) {
        if (characters != null && !characters.isEmpty()) {

            int index = getIndexFromId( characterID );

            if (index != -1) {
                characters.remove( index );

                presenter.characterRemoved( index );
            }
        }
    }

    @Override
    public void onCharacterUpdated(CustomCharacter character) {
        if (characters != null && !characters.isEmpty()) {

            int index = getIndexFromId( character.getId() );

            if (index != -1) {
                characters.set( index, character );

                presenter.characterUpdated( index );
            }
        }
    }

    @Override
    public void onCharacterInserted(CustomCharacter character) {
        if (characters == null) {
            characters = new ArrayList<>(  );
        }

        int index = characters.size();

        if (characters.add( character )) {
            presenter.characterAdded( index );
        }
    }

    private int getIndexFromId(int id) {
        int idx = -1;

        for (int i = 0; i < characters.size(); i++) {

            if (characters.get( i ).getId() == id) {
                idx = i;
                break;
            }
        }

        return idx;
    }
}
