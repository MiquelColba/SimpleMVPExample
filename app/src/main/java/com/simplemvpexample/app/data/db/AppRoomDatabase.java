package com.simplemvpexample.app.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.simplemvpexample.app.data.model.CustomCharacter;

@Database( entities = {CustomCharacter.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract CharacterDAO characterDAO();

    private static volatile AppRoomDatabase INSTANCE;

    static AppRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "characters_database").build();
                }
            }
        }

        return INSTANCE;
    }

}
