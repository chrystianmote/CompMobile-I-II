package br.unes.android.experimentoedittext;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	protected static final int MAX_LENGTH = 6;
	private ArrayList<String> listCodigos;
	private ArrayAdapter<String> aaCodigos;
	private ListView listviewCodigos;
	
	private static final int MENU_LIST_CODIGOS_REMOVER = 1;
	private static final int MENU_LIST_CODIGOS_ALTERAR = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText edittextCodigo = (EditText)findViewById(R.id.edittextCodigo);
		edittextCodigo.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (edittextCodigo.getText().length() > MAX_LENGTH) {
						edittextCodigo.getText().delete(MAX_LENGTH, 
								edittextCodigo.getText().length());
					}
				}
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						listCodigos.add(edittextCodigo.getText().toString());
						aaCodigos.notifyDataSetChanged();
						
						edittextCodigo.setText("");
					}
				}
				return false;
			}
		});
		
		listCodigos = new ArrayList<String>();
		aaCodigos = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, 
				listCodigos);
		
		listviewCodigos = (ListView)findViewById(R.id.listviewCodigos);
		listviewCodigos.setAdapter(aaCodigos);
		listviewCodigos.setOnCreateContextMenuListener(this);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_LIST_CODIGOS_REMOVER:
			AdapterView.AdapterContextMenuInfo menuInfo =
				(AdapterContextMenuInfo)item.getMenuInfo();
			int indiceItem = menuInfo.position;
			listCodigos.remove(indiceItem);
			aaCodigos.notifyDataSetChanged();
			break;
		case MENU_LIST_CODIGOS_ALTERAR:
			break;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_LIST_CODIGOS_REMOVER, 0, "Remover");
		menu.add(0, MENU_LIST_CODIGOS_ALTERAR, 1, "Alterar");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}






