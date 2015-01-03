/**
 * 
 */
package com.example.bluetoothdatatransfer;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * A classe ServidorBluetoothThread é usada para tratar da disponibilização do dispositivo
 * para aceitar conexões via bluetooth, para o aplicativo de bate-papo.
 * 
 * @author alexandre
 *
 */
public class ServidorBluetoothThread extends Thread {

	// socket de servidor para conexão bluetooth
	private final BluetoothServerSocket btServerSocket;
	
	// objeto usado para manipular elementos da GUI da atividade principal
	private Handler handler;
	
	/**
	 * Construtor da classe ServidorBluetoothThread. Inicializa os atributos desta thread.
	 * 
	 * @param adaptadorBT o objeto do adaptador bluetooth local ativo
	 * @param handler o manipulador de elementos da GUI da atividade principal
	 */
	public ServidorBluetoothThread(BluetoothAdapter adaptadorBT, Handler handler) {
		this.handler = handler;
		
		// faz a inicialização do socket de servidor para conexão bluetooth
		BluetoothServerSocket temp = null;
		try {
			temp = adaptadorBT.listenUsingRfcommWithServiceRecord(MainActivity.NOME_SERVICO, 
					MainActivity.UUID_APP);
		} catch (IOException e) {
			
		}
		// armazena o socket no atributo do objeto
		btServerSocket = temp;
	}
	
	@Override
	public void run() {
		BluetoothSocket socket = null; // variável usada para receber o socket do cliente
		while (true) {
			try {
				// bloqueia a thread até que seja recebida uma requisição de cliente para conexão
				socket = btServerSocket.accept();
			} catch (IOException e) {
				break;
			}
			if (socket != null) {
				// se há um socket de cliente, inicia o bate-papo em outra thread
				iniciaThreadBatePapo(socket);
				try {
					// se já está sendo executado um bate-papo, o servidor pode ser cancelado
					btServerSocket.close();
				} catch (IOException e) {
				}
				break;
			}
		}
	}
	
	/**
	 * Inicia a thread que será responsável por controlar o envio e o recebimento
	 * de mensagens via bluetooth.
	 * 
	 * @param socket o socket da conexão bluetooth recebida do cliente
	 */
	private void iniciaThreadBatePapo(BluetoothSocket socket) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.getInstance().exibePainelBatePapo();
			}
		});
		TarefaBatePapo tarefaBatePapo = new TarefaBatePapo(socket, handler);
		MainActivity.getInstance().setTarefaBatePapo(tarefaBatePapo);
		tarefaBatePapo.start();
	}

	/**
	 * Cancela o servidor de conexões bluetooth para o bate-papo. 
	 */
	public void cancelListening() {
		try {
			btServerSocket.close();
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... não pude parar o serviço");
				}
			});
		}
	}

}
