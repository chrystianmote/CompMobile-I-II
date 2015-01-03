package br.com.alexromanelli.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuPrincipal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.menu_principal);
		
		Button buttonIniciarCadastroTurma = (Button)findViewById(R.id.buttonIniciarCadastroTurma);
		Button buttonIniciarCadastroAluno = (Button)findViewById(R.id.buttonIniciarCadastroAluno);
		Button buttonIniciarCadastroDisciplina = (Button)findViewById(R.id.buttonIniciarCadastroDisciplina);
		
		buttonIniciarCadastroTurma.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MenuPrincipal.this, CadastroDeTurmasListagem.class);
				startActivity(i);
			}
		});
		
		buttonIniciarCadastroAluno.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MenuPrincipal.this, CadastroDeAlunosListagem.class);
				startActivity(i);
			}
		});
		
		buttonIniciarCadastroDisciplina.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MenuPrincipal.this, CadastroDeDisciplinasListagem.class);
				startActivity(i);
			}
		});
	}

}
