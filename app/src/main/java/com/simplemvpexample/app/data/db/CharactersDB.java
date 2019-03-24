package com.simplemvpexample.app.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.simplemvpexample.app.data.model.CustomCharacter;

import java.util.ArrayList;
import java.util.List;

public class CharactersDB implements DBHelper{

    private CharacterDAO characterDAO;
    private List<DBListener> dbListener;

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

    public void insertCharacter(CustomCharacter character) {
       new insertAsyncTask( characterDAO , dbListener).execute( character );
    }

    public void deleteCharacter(CustomCharacter character) {
        new deleteAsyncTask( characterDAO, dbListener ).execute( character );
    }

    public void updateCharacter(CustomCharacter character) {
        new updateAsyncTask( characterDAO, dbListener ).execute( character );
    }

    public void getAllCharacters() {
        new getCharactersAsyncTask( characterDAO, dbListener ).execute( );
    }

    public void registerListener(DBListener listener) {

        if (dbListener == null) {
            dbListener = new ArrayList<>();
        }

        dbListener.add( listener);
    }

    public void unregisterListener(DBListener listener) {
        if (dbListener != null) {

            dbListener.remove( listener );
        }
    }

    private static class insertAsyncTask extends AsyncTask<CustomCharacter, Void, CustomCharacter> {

        private CharacterDAO mAsyncTaskDao;
        private List<DBListener> dbListener;

        insertAsyncTask(CharacterDAO dao, List<DBListener> listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected CustomCharacter doInBackground(final CustomCharacter... params) {

            CustomCharacter character = params[0];

            long id = mAsyncTaskDao.insert(character);

            character.setId( (int) id );

            return character;
        }

        @Override
        protected void onPostExecute(CustomCharacter character) {

            if (dbListener != null) {
                for (DBListener l : dbListener) {
                    l.onCharacterInserted( character );
                }
            }

            super.onPostExecute( character );
        }
    }

    private static class deleteAsyncTask extends AsyncTask<CustomCharacter, Void, Integer> {

        private CharacterDAO mAsyncTaskDao;
        private List<DBListener> dbListener;

        deleteAsyncTask(CharacterDAO dao, List<DBListener> listener) {

            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected Integer doInBackground(final CustomCharacter... params) {

            CustomCharacter character = params[0];

            int rowsDeleted = mAsyncTaskDao.delete(character);

            if (rowsDeleted == 1) {
                return character.getId();
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer characterID) {

            if (dbListener != null) {
                for (DBListener l : dbListener) {
                    l.onCharacterDeleted( characterID );
                }
            }

            super.onPostExecute( characterID );
        }
    }

    private static class updateAsyncTask extends AsyncTask<CustomCharacter, Void, CustomCharacter> {

        private CharacterDAO mAsyncTaskDao;
        private List<DBListener> dbListener;

        updateAsyncTask(CharacterDAO dao, List<DBListener> listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected CustomCharacter doInBackground(final CustomCharacter... params) {

            CustomCharacter character = params[0];
            int rowsUpdated = mAsyncTaskDao.update(character);

            if (rowsUpdated == 1) {
                return character;
            }

            return null;
        }

        @Override
        protected void onPostExecute(CustomCharacter character) {

            if (dbListener != null) {
                for (DBListener l : dbListener) {
                    l.onCharacterUpdated( character );
                }
            }
            super.onPostExecute( character );
        }
    }

    private static class getCharactersAsyncTask extends AsyncTask<Void, Void, List<CustomCharacter>> {

        private CharacterDAO mAsyncTaskDao;
        private List<DBListener> dbListener;

        getCharactersAsyncTask(CharacterDAO dao, List<DBListener> listener) {
            mAsyncTaskDao = dao;
            dbListener = listener;
        }

        @Override
        protected void onPostExecute(List<CustomCharacter> characters) {
            if (dbListener != null) {

                for (DBListener l : dbListener) {
                    l.onCharactersAvailable( characters );
                }
            }

            super.onPostExecute( characters );
        }

        @Override
        protected List<CustomCharacter> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllCharacters();
        }
    }
}

