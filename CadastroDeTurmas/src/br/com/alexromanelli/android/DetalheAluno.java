package br.com.alexromanelli.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DetalheAluno extends Activity {

	Long rowId;
	EditText editNome;
	EditText editDataNascimento;
	EditText editSexo;
	EditText editEmail;
	EditText editCidade;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhe_aluno);

	    editNome = (EditText) findViewById(R.id.editAlunoNome);
	    editDataNascimento = (EditText) findViewById(R.id.editAlunoDataNascimento);
	    editSexo = (EditText) findViewById(R.id.editAlunoSexo);
	    editEmail = (EditText) findViewById(R.id.editAlunoEmail);
	    editCidade = (EditText) findViewById(R.id.editAlunoCidade);

	    Button buttonSalvar = (Button) findViewById(R.id.buttonAlunoSalvar);

	    rowId = null;
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	        String nome = extras.getString(AlunoDbAdapter.KEY_NOME);
	        String dataNascimento = extras.getString(AlunoDbAdapter.KEY_DATANASCIMENTO);
	        String sexo = extras.getString(AlunoDbAdapter.KEY_SEXO);
	        String email = extras.getString(AlunoDbAdapter.KEY_EMAIL);
	        String cidade = extras.getString(AlunoDbAdapter.KEY_CIDADE);
	        
	        rowId = extras.getLong(AlunoDbAdapter.KEY_ROWID);

	        if (nome != null) {
	            editNome.setText(nome);
	        }
	        if (dataNascimento != null) {
	            editDataNascimento.setText(dataNascimento);
	        }
	        if (sexo != null) {
	            editSexo.setText(sexo);
	        }
	        if (email != null) {
	            editEmail.setText(email);
	        }
	        if (cidade != null) {
	            editCidade.setText(cidade);
	        }
	    }

	    buttonSalvar.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
	            Bundle bundle = new Bundle();

	            bundle.putString(AlunoDbAdapter.KEY_NOME, editNome.getText().toString());
	            bundle.putString(AlunoDbAdapter.KEY_DATANASCIMENTO, editDataNascimento.getText().toString());
	            bundle.putString(AlunoDbAdapter.KEY_SEXO, editSexo.getText().toString());
	            bundle.putString(AlunoDbAdapter.KEY_EMAIL, editEmail.getText().toString());
	            bundle.putString(AlunoDbAdapter.KEY_CIDADE, editCidade.getText().toString());
	            if (rowId != null) {
	                bundle.putLong(AlunoDbAdapter.KEY_ROWID, rowId);
	            }

	            Intent mIntent = new Intent();
	            mIntent.putExtras(bundle);
	            setResult(RESULT_OK, mIntent);
	            finish();
			}
	    });
	}

}
