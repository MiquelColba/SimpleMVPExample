package com.simplemvpexample.app.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class CharactersDB {

    private EvilCharacterDAO characterDAO;
    private DBListener dbListener;


    public CharactersDB(Context context) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase( context );

        characterDAO = db.characterDAO();
    }

    public void insertCharacter(EvilCharacter character) {
       new insertAsyncTask( characterDAO ).execute( character );
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

    private static class insertAsyncTask extends AsyncTask<EvilCharacter, Void, Void> {

        private EvilCharacterDAO mAsyncTaskDao;

        insertAsyncTask(EvilCharacterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EvilCharacter... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
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
            int rowsDeleted = mAsyncTaskDao.delete(params[0]);
            return rowsDeleted;
        }

        @Override
        protected void onPostExecute(Integer rowsDeleted) {
            dbListener.onCharacterDeleted( rowsDeleted );
            super.onPostExecute( rowsDeleted );
        }
    }

    private static class updateAsyncTask extends AsyncTask<EvilCharacter, Void, Integer> {

        private EvilCharacterDAO mAsyncTaskDao;
        private DBListener dbListener;

        updateAsyncTask(EvilCharacterDAO dao, DBListener listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Integer doInBackground(final EvilCharacter... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer rowsUpdated) {
            dbListener.onCharacterUpdated( rowsUpdated );
            super.onPostExecute( rowsUpdated );
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
            dbListener.onCharactersAvailable( evilCharacters );
            super.onPostExecute( evilCharacters );
        }

        @Override
        protected List<EvilCharacter> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllCharacters();
        }
    }
}

