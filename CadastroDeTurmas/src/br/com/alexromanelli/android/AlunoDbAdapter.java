package br.com.alexromanelli.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlunoDbAdapter {

    public static final String KEY_NOME = "nome";
    public static final String KEY_DATANASCIMENTO = "datanascimento";
    public static final String KEY_SEXO = "sexo";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CIDADE = "cidade";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "alunoDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "aluno";
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
    public AlunoDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the "aluno"s database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public AlunoDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new "aluno". If the "aluno" is successfully created 
     * return the new rowId for that "aluno", otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long createAluno(String nome, String dataNascimento, String sexo, 
    		String email, String cidade) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nome);
        initialValues.put(KEY_DATANASCIMENTO, dataNascimento);
        initialValues.put(KEY_SEXO, sexo);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_CIDADE, cidade);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the "aluno" with the given rowId
     * 
     * @param rowId id of "aluno" to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteAluno(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all "aluno"s in the database
     * 
     * @return Cursor over all "aluno"s
     */
    public Cursor fetchAllAlunos() {
        return mDb.query(DATABASE_TABLE, 
        		new String[] {KEY_ROWID, 
        		              KEY_NOME,
        		              KEY_DATANASCIMENTO, 
        		              KEY_SEXO, 
        		              KEY_EMAIL, 
        		              KEY_CIDADE}, 
        		null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the "aluno" that matches the given rowId
     * 
     * @param rowId id of "aluno" to retrieve
     * @return Cursor positioned to matching "aluno", if found
     * @throws SQLException if "aluno" could not be found/retrieved
     */
    public Cursor fetchAluno(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_NOME, KEY_DATANASCIMENTO, KEY_SEXO, KEY_EMAIL, KEY_CIDADE}, 
                    KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the "aluno" using the details provided. The "aluno" to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of "aluno" to update
     * @return true if the "aluno" was successfully updated, false otherwise
     */
    public boolean updateAluno(long rowId, String nome, String dataNascimento,
    		String sexo, String email, String cidade) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOME, nome);
        args.put(KEY_DATANASCIMENTO, dataNascimento);
        args.put(KEY_SEXO, sexo);
        args.put(KEY_EMAIL, email);
        args.put(KEY_CIDADE, cidade);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
