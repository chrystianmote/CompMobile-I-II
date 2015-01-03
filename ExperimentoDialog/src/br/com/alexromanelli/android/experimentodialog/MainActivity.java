package br.com.alexromanelli.android.experimentodialog;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A classe MainActivity é a atividade única desta aplicação.
 * Sua função é apresentar uma lista de dados na tela, e disponibilizar
 * para o usuário um menu de opções para selecionar entre três conjuntos
 * de dados para exibição. No menu há também opções para incluir novos
 * registros nos conjuntos de dados. A lista de dados permite acesso a
 * um menu de contexto, que disponibiliza opções para alterar ou remover
 * um registro exibido.
 * 
 * @author Alexandre Romanelli
 * @since 2013-09-05
 *
 */
public class MainActivity extends Activity {

	/**
	 * identificador do item de menu dedicado à função de alterar um registro
	 */
	public static final int MENU_ITEM_ALTERAR = 1;
	/**
	 * identificador do item de menu dedicado à função de excluir um registro
	 */
	public static final int MENU_ITEM_EXCLUIR = 2;

	// opções para exibição de conjuntos de dados
	private static final int OPCAO_EXIBIR_CURSO = 1;
	private static final int OPCAO_EXIBIR_DISCIPLINA = 2;
	private static final int OPCAO_EXIBIR_ALUNO = 3;

	/**
	 * armazena a opção de exibição que está ativa
	 */
	private int opcaoExibir;

	// repositórios de dados
	private ArrayList<String> listCurso;
	private ArrayList<String> listDisciplina;
	private ArrayList<String> listAluno;

	/**
	 * armazena uma referência para a lista de dados que está sendo exibida
	 */
	@SuppressWarnings("rawtypes")
	private List listDados;
	
	/**
	 * armazena uma referência para o adaptador de exibição que está sendo usado
	 */
	private BaseAdapter baDados;

	// adaptadores de exibição de conjunto de dados
	private ArrayAdapter<String> aaCurso;
	private ArrayAdapter<String> aaDisciplina;
	private ArrayAdapter<String> aaAluno;

	// referências para componentes da interface
	private ListView listviewDados;
	private TextView textviewTitulo;
	
	/**
	 * Diálogo usado para inserir um registro. Apresenta um formulário e 
	 * os botões para salvar ou cancelar a inclusão.
	 */
	private AlertDialog dialogInserir;
	
	/**
	 * Diálogo usado para modificar um registro. Apresenta um formulário
	 * e os botões para salvar ou cancelar a alteração.
	 */
	private AlertDialog dialogAlterar;
	
	/**
	 * Insere dados nos repositórios. Método usado apenas para testes.
	 */
	private void popularListas() {
		listCurso.add("Sistemas de Informação");
		listCurso.add("Direito");
		listCurso.add("Administração");

		listDisciplina.add("Computação Móvel I");
		listDisciplina.add("Estrutura de Dados I");
		listDisciplina.add("Compiladores");

		listAluno.add("João");
		listAluno.add("Maria");
		listAluno.add("Derp");
		listAluno.add("Derpete");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listCurso = new ArrayList<String>();
		listDisciplina = new ArrayList<String>();
		listAluno = new ArrayList<String>();

		popularListas();

		aaCurso = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listCurso);
		aaDisciplina = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listDisciplina);
		aaAluno = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listAluno);

		listviewDados = (ListView) findViewById(R.id.listviewDados);
		listviewDados.setOnCreateContextMenuListener(this);
		textviewTitulo = (TextView) findViewById(R.id.textviewTitulo);
		
		listDados = null;
		baDados = null;
		opcaoExibir = 0;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_ITEM_ALTERAR, 0, R.string.menu_item_alterar_item);
		menu.add(0, MENU_ITEM_EXCLUIR, 1, R.string.menu_item_remover_item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (featureId == Window.FEATURE_CONTEXT_MENU) {
			
			AdapterView.AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
					.getMenuInfo();
			int indiceItem = menuInfo.position;
			
			switch (item.getItemId()) {
			case MENU_ITEM_EXCLUIR:
				acaoRemoverItem(indiceItem);
				return true;
			case MENU_ITEM_ALTERAR:
				acaoAlterarItem(indiceItem);
				return true;
			}
			
		} else if (featureId == Window.FEATURE_OPTIONS_PANEL) {
			
			switch (item.getItemId()) {
			case R.id.menuitemExibirCursos:
				exibirDados(listCurso, aaCurso, R.string.titulo_cursos);
				opcaoExibir = OPCAO_EXIBIR_CURSO;
				return true;

			case R.id.menuitemExibirDisciplinas:
				exibirDados(listDisciplina, aaDisciplina,
						R.string.titulo_disciplinas);
				opcaoExibir = OPCAO_EXIBIR_DISCIPLINA;
				return true;

			case R.id.menuitemExibirAlunos:
				exibirDados(listAluno, aaAluno, R.string.titulo_alunos);
				opcaoExibir = OPCAO_EXIBIR_ALUNO;
				return true;

			case R.id.menuitemIncluirCurso:
				exibirDialogoIncluirCurso();
				return true;

			case R.id.menuitemIncluirDisciplina:
				exibirDialogoIncluirDisciplina();
				return true;

			case R.id.menuitemIncluirAluno:
				exibirDialogoIncluirAluno();
				return true;
			}
		}
		return false;
	}

	/**
	 * Configura a exibição de um conjunto de dados na interface com
	 * o usuário.
	 * 
	 * @param dados o repositório de dados selecionado para exibição.
	 * @param adapter o adaptador de exibição para os dados do repositório.
	 * @param titulo o recurso de string que deve ser exibido na interface, 
	 *        acima dos dados.
	 */
	private void exibirDados(ArrayList<String> dados,
			ArrayAdapter<String> adapter, int titulo) {
		listDados = dados;
		baDados = adapter;
		listviewDados.setAdapter(adapter);
		textviewTitulo.setText(titulo);
	}

	/**
	 * Executa a ação de remoção de um item, associada ao item do menu
	 * de contexto para remover.
	 * 
	 * @param indiceItem a posição do item a remover na lista exibida.
	 */
	private void acaoRemoverItem(int indiceItem) {
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
		adBuilder
				.setMessage(R.string.mensagem_confirmacao_excluir)

				.setPositiveButton(R.string.botao_confirmar_exclusao,
						new OnRemoveItemListener(this, indiceItem))

				.setNegativeButton(R.string.botao_cancelar_exclusao,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								exibirMensagem(R.string.mensagem_exclusao_cancelada);
							}
						});

		Dialog dialog = adBuilder.create();
		dialog.show();
	}

	/**
	 * Exibe, em um toast, uma mensagem definida em um recurso pré-configurado.
	 * 
	 * @param mensagem o recurso de texto da mensagem a exibir.
	 */
	protected void exibirMensagem(int mensagem) {
		Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
	}

	/**
	 * Remove um item do repositório de dados que está sendo exibido.
	 * 
	 * @param indiceItem a posição do item a remover na lista exibida.
	 */
	public void removerItem(int indiceItem) {
		listDados.remove(indiceItem);
		baDados.notifyDataSetChanged();
		exibirMensagem(R.string.mensagem_exclusao_realizada);
	}

	/**
	 * Executa a ação de alteração de um item, associada ao item de menu
	 * de contexto para alterar.
	 * 
	 * @param indiceItem a posição do item a modificar na lista exibida.
	 */
	private void acaoAlterarItem(int indiceItem) {
		switch (opcaoExibir) {
		case OPCAO_EXIBIR_CURSO:
			exibirDialogoAlterarCurso(indiceItem);
			break;
		case OPCAO_EXIBIR_DISCIPLINA:
			exibirDialogoAlterarDisciplina(indiceItem);
			break;
		case OPCAO_EXIBIR_ALUNO:
			exibirDialogoAlterarAluno(indiceItem);
			break;
		}
	}

	/**
	 * Faz a exibição de um diálogo contendo um formulário para alterar
	 * um registro de curso. Os dados atuais são incluídos no formulário
	 * através de um listener acionado quando o diálogo estiver pronto.
	 * 
	 * @param indiceItem a posição do item a modificar na lista exibida.
	 */
	private void exibirDialogoAlterarCurso(final int indiceItem) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		builder
			.setView(inflater.inflate(R.layout.formulario_curso, null))
			
			.setPositiveButton(R.string.botao_salvar, 
					new OnUpdateItemListener(this, indiceItem))
						
			.setNegativeButton(R.string.botao_cancelar,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							exibirMensagem(R.string.mensagem_alteracao_cancelada);
						}
					});

		dialogAlterar = builder.create();
		dialogAlterar.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				EditText edittextNomeCurso = (EditText) dialogAlterar
						.findViewById(R.id.edittextNomeCurso);
				// EditText edittextDuracaoCurso =
				// (EditText)this.findViewById(R.id.edittextDuracaoCurso);

				edittextNomeCurso.setText(listCurso.get(indiceItem));
			}
		});
		dialogAlterar.show();
	}

	/**
	 * 
	 * @param indiceItem
	 */
	private void exibirDialogoAlterarDisciplina(int indiceItem) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param indiceItem
	 */
	private void exibirDialogoAlterarAluno(int indiceItem) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Faz a exibição de um diálogo contendo um formulário para incluir
	 * um novo registro de curso.
	 */
	private void exibirDialogoIncluirCurso() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		builder
			.setView(inflater.inflate(R.layout.formulario_curso, null))
			
			.setPositiveButton(R.string.botao_salvar, 
					new OnInsertItemListener(this))
						
			.setNegativeButton(R.string.botao_cancelar,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							exibirMensagem(R.string.mensagem_inclusao_cancelada);
						}
					});

		dialogInserir = builder.create();
		dialogInserir.show();
	}

	/**
	 * 
	 */
	private void exibirDialogoIncluirDisciplina() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void exibirDialogoIncluirAluno() {
		// TODO Auto-generated method stub

	}

	/**
	 * Faz a seleção do método apropriado para fazer a inclusão dos dados
	 * do formulário no repositório de dados que está sendo exibido. Em
	 * seguida, exibe uma mensagem informando a conclusão da inclusão e
	 * atualiza a interface.
	 */
	public void inserirItem() {
		switch (opcaoExibir) {
		
		case OPCAO_EXIBIR_CURSO:
			inserirItemCurso();
			break;
			
		case OPCAO_EXIBIR_DISCIPLINA:
			break;
			
		case OPCAO_EXIBIR_ALUNO:
			break;
		}
		
		exibirMensagem(R.string.mensagem_inclusao_realizada);
		baDados.notifyDataSetChanged();
	}

	/**
	 * Faz a seleção do método apropriado para modificar os dados de um 
	 * registro de acordo com as alterações feitas pelo usuário no formulário
	 * exibido no diálogo. Em seguida, exibe uma mensagem informando a
	 * conclusão da modificação e atualiza a interface.
	 * 
	 * @param indiceItem a posição do item a modificar na lista em exibição.
	 */
	public void alterarItem(int indiceItem) {
		switch (opcaoExibir) {
		
		case OPCAO_EXIBIR_CURSO:
			alterarItemCurso(indiceItem);
			break;
			
		case OPCAO_EXIBIR_DISCIPLINA:
			break;
			
		case OPCAO_EXIBIR_ALUNO:
			break;
		}
		
		exibirMensagem(R.string.mensagem_alteracao_realizada);
		baDados.notifyDataSetChanged();
	}
	
	/**
	 * Obtém os dados do formulário de curso e insere o novo registro
	 * no repositório de dados de cursos.
	 */
	private void inserirItemCurso() {
		EditText edittextNomeCurso = (EditText) dialogInserir
				.findViewById(R.id.edittextNomeCurso);
		EditText edittextDuracaoCurso = (EditText) dialogInserir
				.findViewById(R.id.edittextDuracaoCurso);

		String nomeCurso = edittextNomeCurso.getText().toString();
		int duracaoCurso = Integer.parseInt(edittextDuracaoCurso.getText()
				.toString());

		/*
		 * apenas para este exemplo. na atividade, cada valor deve estar em
		 * um campo do objeto Curso
		 */
		String curso = nomeCurso + " - [" + duracaoCurso + " períodos]";
		
		listCurso.add(curso);
	}
	
	/**
	 * Obtém os dados do formulário de curso e modifica o registro
	 * selecionado no repositório de dados de cursos.
	 * 
	 * @param indiceItem a posição do registro a modificar na lista
	 *        em exibição.
	 */
	private void alterarItemCurso(int indiceItem) {
		EditText edittextNomeCurso = (EditText) dialogAlterar
				.findViewById(R.id.edittextNomeCurso);
		EditText edittextDuracaoCurso = (EditText) dialogAlterar
				.findViewById(R.id.edittextDuracaoCurso);

		String nomeCurso = edittextNomeCurso.getText().toString();
		int duracaoCurso = Integer.parseInt(edittextDuracaoCurso.getText()
				.toString());

		/*
		 * apenas para este exemplo. na atividade, cada valor deve estar em
		 * um campo do objeto Curso
		 */
		String curso = nomeCurso + " - [" + duracaoCurso + " períodos]";
		
		listCurso.set(indiceItem, curso);
	}

}
