package com.simplemvpexample.app.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.List;

@Dao
public interface CharacterDAO {

    @Insert
    long insert(CustomCharacter character);

    @Delete
    int delete(CustomCharacter character);

    @Update
    int update(CustomCharacter character);

    @Query( "SELECT * FROM characters" )
    List<CustomCharacter> getAllCharacters();
}
