package com.simplemvpexample.app.screens.charac_list.interfaces;

import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.List;

public interface I_ListOfCPresenter {

    void characterRemoved(int index);

    void characterAdded(int index);

    void characterUpdated(int index);

    void attachAdapter(I_ListOfCAdapter adapter);

    void onAttach(I_ListOfCView view);

    void startPresenter();

    void onViewDestroy();

    void onCharactersAvailable(List<CustomCharacter> characters);

    void onCharacterSelected(CustomCharacter character);

}
