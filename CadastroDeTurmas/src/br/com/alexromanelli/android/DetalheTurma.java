package br.com.alexromanelli.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DetalheTurma extends Activity {

	Long rowId;
	EditText editAbreviacao;
	EditText editDescricao;
	EditText editAno;
	EditText editSemestre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_turma);

	    editAbreviacao = (EditText) findViewById(R.id.editTurmaAbreviacao);
	    editDescricao = (EditText) findViewById(R.id.editTurmaDescricao);
	    editAno = (EditText) findViewById(R.id.editTurmaAno);
	    editSemestre = (EditText) findViewById(R.id.editTurmaSemestre);

	    Button buttonSalvar = (Button) findViewById(R.id.buttonTurmaSalvar);

	    rowId = null;
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	        String abreviacao = extras.getString(TurmaDbAdapter.KEY_ABREVIACAO);
	        String descricao = extras.getString(TurmaDbAdapter.KEY_DESCRICAO);
	        int ano = -1, semestre = -1;
	        try {
	        	ano = extras.getInt(TurmaDbAdapter.KEY_ANO);
	        } catch (Exception e) { }
	        try {
	        	semestre = extras.getInt(TurmaDbAdapter.KEY_SEMESTRE);
	        } catch (Exception e) { }
	        
	        rowId = extras.getLong(TurmaDbAdapter.KEY_ROWID);

	        if (abreviacao != null) {
	            editAbreviacao.setText(abreviacao);
	        }
	        if (descricao != null) {
	            editDescricao.setText(descricao);
	        }
	        if (ano != -1) {
	            editAno.setText(Integer.toString(ano));
	        }
	        if (semestre != -1) {
	            editSemestre.setText(Integer.toString(semestre));
	        }
	    }

	    buttonSalvar.setOnClickListener(new OnClickListener() {

		public void onClick(View arg0) {
	            Bundle bundle = new Bundle();

	            bundle.putString(TurmaDbAdapter.KEY_ABREVIACAO, editAbreviacao.getText().toString());
	            bundle.putString(TurmaDbAdapter.KEY_DESCRICAO, editDescricao.getText().toString());
	            bundle.putInt(TurmaDbAdapter.KEY_ANO, Integer.parseInt(editAno.getText().toString()));
	            bundle.putInt(TurmaDbAdapter.KEY_SEMESTRE, Integer.parseInt(editSemestre.getText().toString()));
	            if (rowId != null) {
	                bundle.putLong(TurmaDbAdapter.KEY_ROWID, rowId);
	            }

	            Intent mIntent = new Intent();
	            mIntent.putExtras(bundle);
	            setResult(RESULT_OK, mIntent);
	            finish();
		}
	    });
	}

}
