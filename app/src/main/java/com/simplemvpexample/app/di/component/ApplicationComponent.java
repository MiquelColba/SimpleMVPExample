package com.simplemvpexample.app.di.component;

import android.content.Context;

import com.simplemvpexample.app.CharactersApp;
import com.simplemvpexample.app.data.db.interfaces.DBHelper;
import com.simplemvpexample.app.di.ApplicationContext;
import com.simplemvpexample.app.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(CharactersApp app);

    @ApplicationContext
    Context getContext();

    DBHelper dbHelper();
}
