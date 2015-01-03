package com.example.bluetoothdatatransfer;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Esta é a atividade principal, e por enquanto a única, deste aplicativo de bate-papo
 * por bluetooth. Nesta atividade, o aplicativo pode estar em um dos seguintes estados:
 * <ul>
 * <li><b>Inicial</b>: é o estado em que o aplicativo se encontra ao iniciar sua
 * execução;</li>
 * <li><b>Aguardando ativação de bluetooth</b>: estado em que o aplicativo fica se,
 * ao tentar obter o dispositivo bluetooth local para iniciar a procura por 
 * dispositivos remotos, for necessário solicitar a ativação do bluetooth no
 * aparelho;</li>
 * <li><b>Procurando dispositivos</b>: o aplicativo fica neste estado para
 * identificar os dispositivos bluetooth remotos visíveis na área de alcance do
 * rádio bluetooth, e a cada dispositivo que se anuncia, é inserido um item na
 * listagem de dispositivos, para que o usuário selecione um para o bate-papo;</li>
 * <li><b>Aguardando recebimento de conexão</b>: é o estado no qual o aplicativo
 * fica ao disponibilizar-se para o bate-papo, e corresponde ao estado de espera
 * de servidor, enquanto o dispositivo remoto que fizer a conexão age como cliente;</li>
 * <li><b>Comunicando</b>: este é o estado objetivo deste aplicativo, e é atingido
 * quando um dispositivo cliente estabelece uma conexão com um dispositivo servidor,
 * e este aceita a conexão, e é neste estado que ocorre o bate-papo, ou seja, o
 * usuário de um dispositivo pode enviar mensagens textuais para o outro dispositivo,
 * e vice-versa.</li>
 * </ul>
 * @author alexandre
 *
 */
public class MainActivity extends Activity {

	/**
	 * A classe DispositivoRemoto é usada apenas para encapsular um objeto de dispositivo
	 * bluetooth obtido durante a procura por dispositivos na área de alcance do radio
	 * bluetooth. Esta classe é usada para fornecer o texto exibido no ListView de
	 * dispositivos visíveis, e para fornecer o objeto de dispositivo bluetooth para que
	 * seja estabelecida uma conexão, de acordo com a seleção do usuário.
	 * 
	 * @author alexandre
	 *
	 */
	private class DispositivoRemoto {
		private BluetoothDevice dispositivoRemoto;

		/**
		 * Construtor simples, apenas inicializa o único atributo deste objeto.
		 * 
		 * @param dispositivoRemoto
		 */
		public DispositivoRemoto(BluetoothDevice dispositivoRemoto) {
			super();
			this.dispositivoRemoto = dispositivoRemoto;
		}

		/**
		 * Retorna o dispositivo bluetooth armazenado neste objeto.
		 * 
		 * @return o dispositivoRemoto
		 */
		public BluetoothDevice getDispositivoRemoto() {
			return dispositivoRemoto;
		}

		/**
		 * Retorna um texto representativo do dispositivo bluetooth encapsulado.
		 * Este texto é formado pelo nome do dispositivo em uma linha, e pelo
		 * seu endereço (MAC).
		 * 
		 * @return o texto representativo do dispositivo bluetooth
		 */
		@Override
		public String toString() {
			return dispositivoRemoto.getName() + "\n"
					+ dispositivoRemoto.getAddress();
		}

	}

	// dados para a identificação do aplicativo:
	public static final String NOME_SERVICO = "Teste Bate-papo Bluetooth";
	public static final UUID UUID_APP = UUID
			.fromString("BEA9B540-BA71-11E2-9E96-0800200C9A66");
	
	// código que identifica a requisição de atividade, usada para tratar o resultado da atividade
	private static final int REQUEST_ENABLE_BT = 10;
	
	// objetos para a exibição da lista de dispositivos
	private ArrayList<DispositivoRemoto> listDispositivos;
	private ArrayAdapter<DispositivoRemoto> aaDispositivos;
	
	// objeto da thread de bate-papo, usada para enviar e receber mensagens de texto
	private TarefaBatePapo tarefaBatePapo;
	
	// objeto usado para permitir a manipulação de elementos da GUI da atividade a partir de outra thread
	private Handler handler = new Handler();
	
	// rótulo usado para decidir quando será necessário cancelar o registro de receptor de broadcast 
	private boolean receiverRegistrado;

	/** 
	 * objeto receptor de broadcast, usado para tratar dos anúncios de dispositivos visíveis que
	 * forem recebidos durante a procura por dispositivos remotos próximos
	 */
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				aaDispositivos.add(new DispositivoRemoto(device));
			}
		}
	};
	
	// referência para o adaptador bluetooth local 
	private BluetoothAdapter btAdapter;

	/**
	 * implementação simplificada de singleton, para acessar a instância desta classe a 
	 * partir de objetos de outras classes
	 */
	private static MainActivity instance = null;
	public static MainActivity getInstance() {
		return instance;
	}
	
	/**
	 * Permite que o atributo tarefaBatePapo receba uma referência para a thread
	 * de bate-papo que foi iniciada pela execução da thread servidor ou da cliente.
	 * 
	 * @param tarefa a thread iniciada para fazer a comunicação via bluetooth
	 */
	public void setTarefaBatePapo(TarefaBatePapo tarefa) {
		tarefaBatePapo = tarefa;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// armazena referência para singleton
		instance = this;
		
		setContentView(R.layout.activity_main);
		
		// inicializa rótulo de receptor de broadcast como falso
		receiverRegistrado = false;

		// configura lista de dispositivos
		ListView listviewDispositivos = (ListView) findViewById(R.id.listviewDispositivos);
		listDispositivos = new ArrayList<DispositivoRemoto>();
		aaDispositivos = new ArrayAdapter<DispositivoRemoto>(this,
				android.R.layout.simple_list_item_1, listDispositivos);
		listviewDispositivos.setAdapter(aaDispositivos);

		// configura o evento de clique sobre um item da lista de dispositivos
		listviewDispositivos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// obtém o dispositivo remoto referente ao item clicado
				BluetoothDevice dispositivo = listDispositivos.get(position)
						.getDispositivoRemoto();
				// inicia cliente, que tenta estabelecer conexão com servidor
				iniciaTarefaCliente(dispositivo);
			}
		});

		// configura o evento de pressionamento de tecla no campo de edição da mensagem a enviar
		final EditText editMensagem = (EditText) findViewById(R.id.editMensagem);
		editMensagem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						// se a ação for o pressionamento de uma tecla, e esta for a tecla enter:
						
						// obtém o texto digitado
						String mensagem = editMensagem.getText().toString();
						// envia a mensagem
						tarefaBatePapo.enviaMensagem(mensagem);
						// inclui a informação da mensagem enviada no visualizador de mensagens
						incluiMensagemEnviada(mensagem);
						
						// limpa campo de edição da mensagem e retorna o foco para este campo
						editMensagem.setText("");
						editMensagem.requestFocus();
						
						return true; // operação efetuada normalmente
					}
				}
				return false; // não houve tratamento do evento
			}
		});
	}

	/**
	 * Inicia thread para fazer conexão com um dispositivo remoto.
	 *  
	 * @param dispositivo o servidor que será alvo da conexão
	 */
	protected void iniciaTarefaCliente(BluetoothDevice dispositivo) {
		ClienteBluetoothThread cliente = new ClienteBluetoothThread(
				dispositivo, handler, tarefaBatePapo);
		cliente.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// preenche o menu com a definição do recurso de menu "main"
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// identifica o item de menu selecionado e executa a ação associada
		switch (item.getItemId()) {
		case R.id.action_procura_dispositivos:
			iniciaProcuraPorDispositivos();
			return true;
		case R.id.action_tornar_disponivel:
			tornarDispositivoDisponivel();
			return true;
		}
		return false;
	}

	/**
	 * Este método solicita permissão do usuário para ativar a visibilidade do dispositivo
	 * bluetooth por 300s. Esta solicitação é feita em uma atividade do sistema para este
	 * fim. O aplicativo fica parado aguardando a conclusão desssa atividade do sistema e,
	 * depois, inicia a thread de servidor de bate-papo, se houver dispositivo bluetooth
	 * disponibilizado pelo usuário.
	 * 
	 */
	private void tornarDispositivoDisponivel() {
		Intent visivel = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		visivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(visivel);

		obtemAdaptadorBluetooth();
		if (btAdapter == null) // exibe mensagem de erro
			Toast.makeText(this, "Bluetooth não encontrado ou inexistente",
					Toast.LENGTH_LONG).show();
		else { // inicia thread servidor
			ServidorBluetoothThread servidor = new ServidorBluetoothThread(
					btAdapter, handler);
			servidor.start();
		}
	}

	/**
	 * Este método apenas obtém o adaptador default do dispositivo móvel
	 */
	private void obtemAdaptadorBluetooth() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Este método leva o aplicativo ao estado de espera por anúncios de dispositivos próximos.
	 */
	private void iniciaProcuraPorDispositivos() {
		// exibe o elemento da GUI que contém a lista de dispositivos
		exibePainelDispositivos();

		// obtém o adaptador
		obtemAdaptadorBluetooth();

		if (btAdapter == null) { // sem dispositivo bluetooth disponível
			Toast.makeText(this, "Bluetooth não encontrado ou inexistente",
					Toast.LENGTH_LONG).show();
		} else {
			// habilita bluetooth, se ainda não estiver habilitado
			if (!btAdapter.isEnabled()) {
				// esta habilitação é feita com uma atividade do sistema para esta finalidade
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				// esta atividade do sistema retorna uma resposta positiva ou negativa
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			} else
				// se o dispositivo estiver previamente habilitado, prossegue a procura por dispositivos
				prossegueProcuraPorDispositivos();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK)
				// se o resultado de atividade recebido for para a requisição para habilitar o bluetooth,
				// e o resultado for positivo, prossegue a procura por dispositivos
				prossegueProcuraPorDispositivos();
			else
				// se o resultado for negativo, não há como prosseguir a procura por dispositivos
				Toast.makeText(this, "sem Bluetooth, não tenho o que fazer...",
						Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Método usado para que seja feita a sequência da procura por dispositivos. Isto
	 * permite que esta sequência seja feita após o usuário ativar o dispositivo
	 * bluetooth, se estivesse desativado.
	 */
	private void prossegueProcuraPorDispositivos() {
		// inicia um filtro para capturar os anúncios de dispositivos BT encontrados
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// registra esta atividade como receptora deste tipo de anúncio
		registerReceiver(broadcastReceiver, filter);
		// ajusta o rótulo de receptor de broadcast para verdadeiro
		receiverRegistrado = true;
		// inicia processo de descoberta do adaptador bluetooth
		btAdapter.startDiscovery();
	}

	/**
	 * Exibe o elemento da GUI para apresentação da lista de dispositivos, e oculta
	 * o elemento para apresentação do bate-papo.
	 */
	protected void exibePainelDispositivos() {
		findViewById(R.id.painelConexao).setVisibility(View.VISIBLE);
		findViewById(R.id.painelBatePapo).setVisibility(View.GONE);
	}

	/**
	 * Exibe o elemento da GUI para apresentação do bate-papo, e oculta
	 * o elemento para apresentação da lista de dispositivos.
	 */
	protected void exibePainelBatePapo() {
		findViewById(R.id.painelConexao).setVisibility(View.GONE);
		findViewById(R.id.painelBatePapo).setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiverRegistrado)
			// se estiver feito o registro de receptor de broadcast, cancelar este registro
			unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Método usado para que uma thread remota possa exibir uma mensagem em Toast
	 * pelo objeto handler.
	 * 
	 * @param mensagem a mensagem a ser exibida
	 */
	protected void exibeMensagem(String mensagem) {
		Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
	}

	/**
	 * Inclui uma informação de mensagem enviada no elemento da GUI para este fim.
	 * 
	 * @param mensagem a mensagem que foi enviada ao dispositivo remoto
	 */
	public void incluiMensagemEnviada(String mensagem) {
		TextView textMensagens = (TextView) findViewById(R.id.textMensagens);
		textMensagens.setText("[eu]:" + mensagem + "\n\n"
				+ textMensagens.getText().toString());
	}
	
	/**
	 * Inclui uma informação de mensagem recebida de um dispositivo remoto, no elemento
	 * da GUI destinado a apresentar o bate-papo.
	 * 
	 * @param mensagem a mensagem que foi recebida do dispositivo remoto
	 */
	public void incluiMensagemRecebida(String mensagem) {
		TextView textMensagens = (TextView) findViewById(R.id.textMensagens);
		textMensagens.setText("[outro]:" + mensagem + "\n\n"
				+ textMensagens.getText().toString());
	}
}
