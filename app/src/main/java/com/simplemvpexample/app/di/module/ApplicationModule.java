package com.simplemvpexample.app.di.module;

import android.app.Application;
import android.content.Context;

import com.simplemvpexample.app.data.db.CharactersDB;
import com.simplemvpexample.app.data.db.interfaces.DBHelper;
import com.simplemvpexample.app.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application mApplication){ this.mApplication = mApplication; }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(CharactersDB db) {
        return db;
    }
}
