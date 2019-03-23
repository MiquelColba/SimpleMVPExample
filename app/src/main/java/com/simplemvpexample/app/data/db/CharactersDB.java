package com.simplemvpexample.app.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class CharactersDB {

    private EvilCharacterDAO characterDAO;
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

    public void insertCharacter(EvilCharacter character) {
       new insertAsyncTask( characterDAO , dbListener).execute( character );
    }

    public void deleteCharacter(EvilCharacter character) {
        new deleteAsyncTask( characterDAO, dbListener ).execute( character );
    }

    public void updateCharacter(EvilCharacter character) {
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

    private static class insertAsyncTask extends AsyncTask<EvilCharacter, Void, EvilCharacter> {

        private EvilCharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        insertAsyncTask(EvilCharacterDAO dao, DBListener listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected EvilCharacter doInBackground(final EvilCharacter... params) {

            EvilCharacter character = params[0];

            long id = mAsyncTaskDao.insert(character);

            character.setId( (int) id );

            return character;
        }

        @Override
        protected void onPostExecute(EvilCharacter character) {

            if (dbListener != null) {
                dbListener.onCharacterInserted( character );
            }

            super.onPostExecute( character );
        }
    }

    private static class deleteAsyncTask extends AsyncTask<EvilCharacter, Void, Integer> {

        private EvilCharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        deleteAsyncTask(EvilCharacterDAO dao, DBListener listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Integer doInBackground(final EvilCharacter... params) {

            EvilCharacter character = params[0];

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

    private static class updateAsyncTask extends AsyncTask<EvilCharacter, Void, EvilCharacter> {

        private EvilCharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        updateAsyncTask(EvilCharacterDAO dao, DBListener listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected EvilCharacter doInBackground(final EvilCharacter... params) {

            EvilCharacter character = params[0];
            int rowsUpdated = mAsyncTaskDao.update(character);

            if (rowsUpdated == 1) {
                return character;
            }

            return null;
        }

        @Override
        protected void onPostExecute(EvilCharacter chracter) {

            if (dbListener != null) {
                dbListener.onCharacterUpdated( chracter );
            }
            super.onPostExecute( chracter );
        }
    }

    private static class getCharactersAsyncTask extends AsyncTask<Void, Void, List<EvilCharacter>> {

        private EvilCharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        getCharactersAsyncTask(EvilCharacterDAO dao, DBListener listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected void onPostExecute(List<EvilCharacter> evilCharacters) {
            if (dbListener != null) {

                dbListener.onCharactersAvailable( evilCharacters );
            }

            super.onPostExecute( evilCharacters );
        }

        @Override
        protected List<EvilCharacter> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllCharacters();
        }
    }
}

