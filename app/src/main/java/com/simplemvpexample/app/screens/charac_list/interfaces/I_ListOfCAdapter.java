package com.simplemvpexample.app.screens.charac_list.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.List;

public interface I_ListOfCAdapter {

    void setCharacters(List<CustomCharacter> charactersList);

    void characterInserted(int index);

    void characterRemoved(int index);

    void characterUpdated(int index);

    void onAttach(I_ListOfCPresenter presenter);

    void onDetach();

}
