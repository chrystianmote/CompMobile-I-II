package br.com.alexromanelli.android;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class CadastroDeAlunosListagem extends ListActivity {
    private AlunoDbAdapter mDbHelper;
	private Cursor cursorAlunos;

	private static final int DELETE_ID = Menu.FIRST;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_alunos);
        
        Button buttonNovoAluno = (Button)findViewById(R.id.buttonNovoAluno);
        buttonNovoAluno.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(CadastroDeAlunosListagem.this, DetalheAluno.class);
				startActivityForResult(intent, ACTIVITY_CREATE);
			}
        });
        
        Button buttonSair = (Button)findViewById(R.id.buttonAlunoSair);
        buttonSair.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cursorAlunos.close();
				mDbHelper.close();
				finish();
			}
        });
        
        mDbHelper = new AlunoDbAdapter(this);
        mDbHelper.open();
        fillData();
        
        registerForContextMenu(getListView());
    }

	private void fillData() {
		// Get all of the notes from the database and create the item list
        cursorAlunos = mDbHelper.fetchAllAlunos();
        startManagingCursor(cursorAlunos);

        String[] from = new String[] { AlunoDbAdapter.KEY_NOME };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter turmas =
            new SimpleCursorAdapter(this, R.layout.linha_turma, cursorAlunos, from, to);
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
	        mDbHelper.deleteAluno(info.id);
	        fillData();
	        return true;
	    }
	    return super.onContextItemSelected(item);	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = cursorAlunos;
		c.moveToPosition(position);
		
		Intent i = new Intent(CadastroDeAlunosListagem.this, DetalheAluno.class);
		
		i.putExtra(AlunoDbAdapter.KEY_ROWID, id);
		i.putExtra(AlunoDbAdapter.KEY_NOME, c.getString(
		        c.getColumnIndexOrThrow(AlunoDbAdapter.KEY_NOME)));
		i.putExtra(AlunoDbAdapter.KEY_DATANASCIMENTO, c.getString(
		        c.getColumnIndexOrThrow(AlunoDbAdapter.KEY_DATANASCIMENTO)));
		i.putExtra(AlunoDbAdapter.KEY_SEXO, c.getString(
		        c.getColumnIndexOrThrow(AlunoDbAdapter.KEY_SEXO)));
		i.putExtra(AlunoDbAdapter.KEY_EMAIL, c.getString(
		        c.getColumnIndexOrThrow(AlunoDbAdapter.KEY_EMAIL)));
		i.putExtra(AlunoDbAdapter.KEY_CIDADE, c.getString(
		        c.getColumnIndexOrThrow(AlunoDbAdapter.KEY_CIDADE)));
		
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = intent.getExtras();

		switch(requestCode) {
		case ACTIVITY_CREATE:
		    String nome = extras.getString(AlunoDbAdapter.KEY_NOME);
		    String dataNascimento = extras.getString(AlunoDbAdapter.KEY_DATANASCIMENTO);
		    String sexo = extras.getString(AlunoDbAdapter.KEY_SEXO);
		    String email = extras.getString(AlunoDbAdapter.KEY_EMAIL);
		    String cidade = extras.getString(AlunoDbAdapter.KEY_CIDADE);
		    mDbHelper.createAluno(nome, dataNascimento, sexo, email, cidade);
		    fillData();
		    break;
		case ACTIVITY_EDIT:
		    Long rowId = extras.getLong(AlunoDbAdapter.KEY_ROWID);
		    if (rowId != null) {
			    nome = extras.getString(AlunoDbAdapter.KEY_NOME);
			    dataNascimento = extras.getString(AlunoDbAdapter.KEY_DATANASCIMENTO);
			    sexo = extras.getString(AlunoDbAdapter.KEY_SEXO);
			    email = extras.getString(AlunoDbAdapter.KEY_EMAIL);
			    cidade = extras.getString(AlunoDbAdapter.KEY_CIDADE);
			    mDbHelper.updateAluno(rowId, nome, dataNascimento, sexo, email, cidade);
		    }
		    fillData();
		    break;
		}
	}
}
