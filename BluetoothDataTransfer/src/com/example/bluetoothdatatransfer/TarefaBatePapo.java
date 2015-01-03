package com.example.bluetoothdatatransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class TarefaBatePapo extends Thread {

	private Handler handler;
	private BluetoothSocket socket;
	private InputStream inStream;
	private OutputStream outStream;
	
	public TarefaBatePapo(BluetoothSocket socket, Handler handler) {
		this.handler = handler;
		this.socket = socket;
		
		InputStream tempIn = null;
		OutputStream tempOut = null;
		try {
			tempIn = socket.getInputStream();
			tempOut = socket.getOutputStream();
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... conexão mal sucedida");
				}
			});
		}
		inStream = tempIn;
		outStream = tempOut;
	}

	@Override
	public void interrupt() {
		try {
			socket.close();
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... não pude fechar a conexão");
				}
			});
		}
	}

	@Override
	public void run() {
		final byte[] buffer = new byte[1024];
		
		while (true) {
			try {
				final int numBytes = inStream.read(buffer);
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							MainActivity.getInstance().incluiMensagemRecebida(new String(buffer, 0, numBytes, "UTF-8"));
						} catch (UnsupportedEncodingException e) {}
					}
				});
			} catch (IOException e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MainActivity.getInstance().exibeMensagem("aviso... conexão encerrada");
					}
				});
				break;
			}
		}
	}
	
	public void enviaMensagem(String mensagem) {
		try {
			byte[] msg = mensagem.getBytes("UTF-8");
			outStream.write(msg);
		} catch (UnsupportedEncodingException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... caractere inválido");
				}
			});
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... mensagem não enviada");
				}
			});
		} 
	}
}
