package com.simplemvpexample.app.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.simplemvpexample.app.data.model.Character;

import java.util.List;

@Dao
public interface CharacterDAO {

    @Insert
    long insert(Character character);

    @Delete
    int delete(Character character);

    @Update
    int update(Character character);

    @Query( "SELECT * FROM characters" )
    List<Character> getAllCharacters();
}
