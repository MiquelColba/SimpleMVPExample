package com.simplemvpexample.app.screens.charac_list.interfaces;

public interface I_ListOfCInteractor {

    void onAttach(I_ListOfCPresenter presenter);

    void onDetach();

    void getCharacters();

    boolean hasCharacters();

}
