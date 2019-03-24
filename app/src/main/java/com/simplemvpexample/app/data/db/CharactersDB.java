package com.simplemvpexample.app.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.simplemvpexample.app.data.model.Character;

import java.util.List;

public class CharactersDB {

    private CharacterDAO characterDAO;
    private DBListener dbListener;

    private static volatile CharactersDB charactersDB;


    public static CharactersDB getInstance(final Context context) {

        if (charactersDB == null) {
            charactersDB = new CharactersDB(context);
        }

        return charactersDB;
    }

    private CharactersDB(Context context) {

            AppRoomDatabase db = AppRoomDatabase.getDatabase( context );

            characterDAO = db.characterDAO();

    }

    public void insertCharacter(Character character) {
       new insertAsyncTask( characterDAO , dbListener).execute( character );
    }

    public void deleteCharacter(Character character) {
        new deleteAsyncTask( characterDAO, dbListener ).execute( character );
    }

    public void updateCharacter(Character character) {
        new updateAsyncTask( characterDAO, dbListener ).execute( character );
    }

    public void getAllCharacters() {
        new getCharactersAsyncTask( characterDAO, dbListener ).execute( );
    }

    public void registerListener(DBListener listener) {
        dbListener = listener;
    }

    public void unregisterListener() {
        dbListener = null;
    }

    private static class insertAsyncTask extends AsyncTask<Character, Void, Character> {

        private CharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        insertAsyncTask(CharacterDAO dao, DBListener listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Character doInBackground(final Character... params) {

            Character character = params[0];

            long id = mAsyncTaskDao.insert(character);

            character.setId( (int) id );

            return character;
        }

        @Override
        protected void onPostExecute(Character character) {

            if (dbListener != null) {
                dbListener.onCharacterInserted( character );
            }

            super.onPostExecute( character );
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Character, Void, Integer> {

        private CharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        deleteAsyncTask(CharacterDAO dao, DBListener listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Integer doInBackground(final Character... params) {

            Character character = params[0];

            int rowsDeleted = mAsyncTaskDao.delete(character);

            if (rowsDeleted == 1) {
                return character.getId();
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer characterID) {
            if (dbListener != null) {
                dbListener.onCharacterDeleted( characterID );
            }
            super.onPostExecute( characterID );
        }
    }

    private static class updateAsyncTask extends AsyncTask<Character, Void, Character> {

        private CharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        updateAsyncTask(CharacterDAO dao, DBListener listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Character doInBackground(final Character... params) {

            Character character = params[0];
            int rowsUpdated = mAsyncTaskDao.update(character);

            if (rowsUpdated == 1) {
                return character;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Character chracter) {

            if (dbListener != null) {
                dbListener.onCharacterUpdated( chracter );
            }
            super.onPostExecute( chracter );
        }
    }

    private static class getCharactersAsyncTask extends AsyncTask<Void, Void, List<Character>> {

        private CharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        getCharactersAsyncTask(CharacterDAO dao, DBListener listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected void onPostExecute(List<Character> evilCharacters) {
            if (dbListener != null) {

                dbListener.onCharactersAvailable( evilCharacters );
            }

            super.onPostExecute( evilCharacters );
        }

        @Override
        protected List<Character> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllCharacters();
        }
    }
}

