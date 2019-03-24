package com.simplemvpexample.app;

import android.app.Application;
import android.content.Context;

import com.simplemvpexample.app.di.component.ApplicationComponent;
import com.simplemvpexample.app.di.component.DaggerApplicationComponent;
import com.simplemvpexample.app.di.module.ApplicationModule;

public class CharactersApp extends Application {

    private ApplicationComponent mApplicationComponent;

    public static CharactersApp get(Context context) {
        return (CharactersApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule( new ApplicationModule( this ) ).build();

        mApplicationComponent.inject( this );
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent component) {
        mApplicationComponent = component;
    }

}
