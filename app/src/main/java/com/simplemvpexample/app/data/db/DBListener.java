package com.simplemvpexample.app.data.db;

import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public interface DBListener {

    void onCharactersAvailable(List<EvilCharacter> characters);

    void onCharacterDeleted(int characterID);

    void onCharacterUpdated(int characterID);
}
