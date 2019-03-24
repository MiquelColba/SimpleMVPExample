package com.simplemvpexample.app.screens.charac_list.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

public interface I_ListOfCInteractor {

    void onAttach(I_ListOfCPresenter presenter);

    void onDetach();

    void getCharacters();

    boolean hasCharacters();

    CustomCharacter getCharacter(int index);

}
