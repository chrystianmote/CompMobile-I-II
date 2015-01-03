package br.com.alexromanelli.android.experimentolistview;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	private class Disciplina {
		public static final String KEY_CODIGO = "codigo";
		public static final String KEY_NOME = "nome";
		public static final String KEY_CREDITOS = "creditos";
	}
	
	private ArrayList<HashMap<String,Object>> listDisciplinas;
	
	private SimpleAdapter saDisciplinas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listDisciplinas = new ArrayList<HashMap<String,Object>>();
		
		String[] origemDisciplina = { 
			Disciplina.KEY_CODIGO,
			Disciplina.KEY_NOME,
			Disciplina.KEY_CREDITOS
		};
		
		int[] destinoDisciplina = {
			R.id.textviewCodigoDisciplina,
			R.id.textviewNomeDisciplina,
			R.id.textviewCreditosDisciplina
		};
		
		saDisciplinas = new SimpleAdapter(this, 
				listDisciplinas, 
				R.layout.item_lista_disciplina, 
				origemDisciplina, 
				destinoDisciplina);
		
		Spinner spinnerDisciplina = (Spinner)findViewById(R.id.spinnerDisciplina);
		spinnerDisciplina.setAdapter(saDisciplinas);
		
		popularDisciplinas();
	}

	private void popularDisciplinas() {
		inserirDisciplina("DCT191", "Inteligência Artificial", 2);
		inserirDisciplina("DCT192", "Computação Móvel I", 4);
		inserirDisciplina("DCT175","Gestão de Projetos de Software",4);
		inserirDisciplina("DCT177","Engenharia de Software",4);
		inserirDisciplina("DCT149","Sistemas Distribuídos",2);
		inserirDisciplina("DCT156","Tópicos Avançados em Informática I",4);
	}
	
	private void inserirDisciplina(String codigo, String nome, int creditos) {
		HashMap<String, Object> disciplina = new HashMap<String, Object>();
		disciplina.put(Disciplina.KEY_CODIGO, codigo);
		disciplina.put(Disciplina.KEY_NOME, nome);
		disciplina.put(Disciplina.KEY_CREDITOS, creditos);
		listDisciplinas.add(disciplina);
		saDisciplinas.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
