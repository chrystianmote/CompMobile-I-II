
package br.com.alexromanelli.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TurmaDbAdapter {

    public static final String KEY_ABREVIACAO = "abreviacao";
    public static final String KEY_DESCRICAO = "descricao";
    public static final String KEY_ANO = "ano";
    public static final String KEY_SEMESTRE = "semestre";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "turmaDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "turma";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DatabaseScripts.DATABASE_CREATE_TURMA);
            db.execSQL(DatabaseScripts.DATABASE_CREATE_ALUNO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(DatabaseScripts.DROP_TABLE_TURMA);
            db.execSQL(DatabaseScripts.DROP_TABLE_ALUNO);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public TurmaDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the "turma"s database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public TurmaDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new "turma". If the "turma" is successfully created 
     * return the new rowId for that "turma", otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long createTurma(String abreviacao, String descricao, int ano, int semestre) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ABREVIACAO, abreviacao);
        initialValues.put(KEY_DESCRICAO, descricao);
        initialValues.put(KEY_ANO, ano);
        initialValues.put(KEY_SEMESTRE, semestre);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the "turma" with the given rowId
     * 
     * @param rowId id of "turma" to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTurma(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all "turma"s in the database
     * 
     * @return Cursor over all "turma"s
     */
    public Cursor fetchAllTurmas() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ABREVIACAO,
                KEY_DESCRICAO, KEY_ANO, KEY_SEMESTRE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the "turma" that matches the given rowId
     * 
     * @param rowId id of "turma" to retrieve
     * @return Cursor positioned to matching "turma", if found
     * @throws SQLException if "turma" could not be found/retrieved
     */
    public Cursor fetchTurma(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_ABREVIACAO, KEY_DESCRICAO, KEY_ANO, KEY_SEMESTRE}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the "turma" using the details provided. The "turma" to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of "turma" to update
     * @return true if the "turma" was successfully updated, false otherwise
     */
    public boolean updateTurma(long rowId, String abreviacao, String descricao,
    		int ano, int semestre) {
        ContentValues args = new ContentValues();
        args.put(KEY_ABREVIACAO, abreviacao);
        args.put(KEY_DESCRICAO, descricao);
        args.put(KEY_ANO, ano);
        args.put(KEY_SEMESTRE, semestre);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
