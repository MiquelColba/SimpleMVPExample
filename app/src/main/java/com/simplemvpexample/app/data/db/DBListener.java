package com.simplemvpexample.app.data.db;

import com.simplemvpexample.app.data.model.Character;

import java.util.List;

public interface DBListener {

    void onCharactersAvailable(List<Character> characters);

    void onCharacterDeleted(int characterID);

    void onCharacterUpdated(Character character);

    void onCharacterInserted(Character character);
}
