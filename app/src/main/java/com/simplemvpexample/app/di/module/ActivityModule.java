package com.simplemvpexample.app.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.simplemvpexample.app.di.ActivityContext;
import com.simplemvpexample.app.di.PerActivity;
import com.simplemvpexample.app.screens.charac_list.ListOfCharactersInteractor;
import com.simplemvpexample.app.screens.charac_list.ListOfCharactersPresenter;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCInteractor;
import com.simplemvpexample.app.screens.charac_list.interfaces.I_ListOfCPresenter;
import com.simplemvpexample.app.screens.character.CharacterInteractor;
import com.simplemvpexample.app.screens.character.CharacterPresenter;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterInteractor;
import com.simplemvpexample.app.screens.character.interfaces.I_CharacterPresenter;

import dagger.Module;
import dagger.Provides;

@Module
@PerActivity
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    I_CharacterPresenter provideCharacterPresenter(CharacterPresenter presenter) {
        return presenter;
    }

    @Provides
    I_CharacterInteractor provideCharacterInteractor(CharacterInteractor interactor) {
        return interactor;
    }

    @Provides
    I_ListOfCPresenter provideListPresenter(ListOfCharactersPresenter presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    I_ListOfCInteractor provideListInteractor(ListOfCharactersInteractor interactor) {
        return interactor;
    }

}
