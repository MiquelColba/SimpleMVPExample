package com.simplemvpexample.app.screens.charac_list.interfaces;

import android.content.Context;

import com.simplemvpexample.app.data.model.CustomCharacter;

public interface I_ListOfCView {

    void showNoCharacters();

    void hideNoCharacters();

    void viewCharacterDetails(CustomCharacter character);
}
