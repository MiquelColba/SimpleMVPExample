package com.simplemvpexample.app.di.component;

import com.simplemvpexample.app.di.PerActivity;
import com.simplemvpexample.app.di.module.ActivityModule;
import com.simplemvpexample.app.screens.charac_list.ListOfCharactersView;
import com.simplemvpexample.app.screens.character.CharacterView;

import dagger.Component;

@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    void inject(ListOfCharactersView activity);

    void inject(CharacterView activity);
}
