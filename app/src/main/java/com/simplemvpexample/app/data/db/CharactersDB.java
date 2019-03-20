package com.simplemvpexample.app.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.simplemvpexample.app.data.model.EvilCharacter;

import java.util.List;

public class CharactersDB {

    private EvilCharacterDAO characterDAO;


    public CharactersDB(Context context) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase( context );

        characterDAO = db.characterDAO();
    }

    public void insertCharacter(EvilCharacter character) {
       new insertAsyncTask( characterDAO ).execute( character );
    }

    public void deleteCharacter(EvilCharacter character) {
        new deleteAsyncTask( characterDAO ).execute( character );
    }

    public void getAllCharacters(DBListener listener) {
        new getCharactersAsyncTask( characterDAO, listener ).execute( );
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

    private static class deleteAsyncTask extends AsyncTask<EvilCharacter, Void, Void> {

        private EvilCharacterDAO mAsyncTaskDao;

        deleteAsyncTask(EvilCharacterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EvilCharacter... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
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

