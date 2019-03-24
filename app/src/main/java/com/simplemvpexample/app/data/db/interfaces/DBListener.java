package com.simplemvpexample.app.data.db.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.List;

public interface DBListener {

    void onCharactersAvailable(List<CustomCharacter> characters);

    void onCharacterDeleted(int characterID);

    void onCharacterUpdated(CustomCharacter character);

    void onCharacterInserted(CustomCharacter character);
}
