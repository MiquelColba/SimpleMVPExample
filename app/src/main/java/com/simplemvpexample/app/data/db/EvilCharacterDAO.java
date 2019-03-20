package com.simplemvpexample.app.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

@Dao
public interface EvilCharacterDAO {

    @Insert
    void insert(EvilCharacter character);

    @Delete
    void delete(EvilCharacter character);

    @Query( "SELECT * FROM evil_characters" )
    List<EvilCharacter> getAllCharacters();
}
