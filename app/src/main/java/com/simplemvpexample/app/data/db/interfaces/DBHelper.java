package com.simplemvpexample.app.data.db.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

public interface DBHelper {

    void insertCharacter(CustomCharacter character);

    void updateCharacter(CustomCharacter character);

    void deleteCharacter(CustomCharacter character);

    void getAllCharacters();

    void registerListener(DBListener listener);

    void unregisterListener(DBListener listener);
}
