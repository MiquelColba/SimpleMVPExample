package com.simplemvpexample.app.screens.character.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

public interface I_CharacterInteractor {

    void saveCharacter(CustomCharacter character, boolean isEdit);

    void deleteCharacter(CustomCharacter character);

    void onAttachPresenter(I_CharacterPresenter presenter);

    void onDetachPresenter();
}
