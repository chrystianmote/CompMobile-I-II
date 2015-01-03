package br.com.alexromanelli.android;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CadastroDeTurmasListagem extends ListActivity {
    private TurmaDbAdapter mDbHelper;
	private Cursor cursorTurmas;

	private static final int DELETE_ID = Menu.FIRST;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_turmas);
        
        Button buttonNovaTurma = (Button)findViewById(R.id.buttonNovaTurma);
        buttonNovaTurma.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(CadastroDeTurmasListagem.this, DetalheTurma.class);
				startActivityForResult(intent, ACTIVITY_CREATE);
			}
        });
        
        Button buttonSair = (Button)findViewById(R.id.buttonTurmaSair);
        buttonSair.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cursorTurmas.close();
				mDbHelper.close();
				finish();
			}
        });
        
        mDbHelper = new TurmaDbAdapter(this);
        mDbHelper.open();
        fillData();
        
        registerForContextMenu(getListView());
    }

	private void fillData() {
		// Get all of the notes from the database and create the item list
        cursorTurmas = mDbHelper.fetchAllTurmas();
        startManagingCursor(cursorTurmas);

        String[] from = new String[] { TurmaDbAdapter.KEY_ABREVIACAO };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter turmas =
            new SimpleCursorAdapter(this, R.layout.linha_turma, cursorTurmas, from, to);
        setListAdapter(turmas);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID , 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	    case DELETE_ID:
	        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        mDbHelper.deleteTurma(info.id);
	        fillData();
	        return true;
	    }
	    return super.onContextItemSelected(item);	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor c = cursorTurmas;
		c.moveToPosition(position);
		
		Intent i = new Intent(CadastroDeTurmasListagem.this, DetalheTurma.class);
		
		i.putExtra(TurmaDbAdapter.KEY_ROWID, id);
		i.putExtra(TurmaDbAdapter.KEY_ABREVIACAO, c.getString(
		        c.getColumnIndexOrThrow(TurmaDbAdapter.KEY_ABREVIACAO)));
		i.putExtra(TurmaDbAdapter.KEY_DESCRICAO, c.getString(
		        c.getColumnIndexOrThrow(TurmaDbAdapter.KEY_DESCRICAO)));
		i.putExtra(TurmaDbAdapter.KEY_ANO, c.getInt(
		        c.getColumnIndexOrThrow(TurmaDbAdapter.KEY_ANO)));
		i.putExtra(TurmaDbAdapter.KEY_SEMESTRE, c.getInt(
		        c.getColumnIndexOrThrow(TurmaDbAdapter.KEY_SEMESTRE)));
		
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = intent.getExtras();

		switch(requestCode) {
		case ACTIVITY_CREATE:
		    String abreviacao = extras.getString(TurmaDbAdapter.KEY_ABREVIACAO);
		    String descricao = extras.getString(TurmaDbAdapter.KEY_DESCRICAO);
		    int ano = extras.getInt(TurmaDbAdapter.KEY_ANO);
		    int semestre = extras.getInt(TurmaDbAdapter.KEY_SEMESTRE);
		    mDbHelper.createTurma(abreviacao, descricao, ano, semestre);
		    fillData();
		    break;
		case ACTIVITY_EDIT:
		    Long rowId = extras.getLong(TurmaDbAdapter.KEY_ROWID);
		    if (rowId != null) {
			    abreviacao = extras.getString(TurmaDbAdapter.KEY_ABREVIACAO);
			    descricao = extras.getString(TurmaDbAdapter.KEY_DESCRICAO);
			    ano = extras.getInt(TurmaDbAdapter.KEY_ANO);
			    semestre = extras.getInt(TurmaDbAdapter.KEY_SEMESTRE);
			    mDbHelper.updateTurma(rowId, abreviacao, descricao, ano, semestre);
		    }
		    fillData();
		    break;
		}
	}
}