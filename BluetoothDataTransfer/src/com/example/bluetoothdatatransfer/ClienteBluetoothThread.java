package com.example.bluetoothdatatransfer;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ClienteBluetoothThread extends Thread {
	private BluetoothDevice dispositivoRemoto;
	private BluetoothSocket socket;
	private Handler handler;
	
	public ClienteBluetoothThread(BluetoothDevice dispositivo, Handler handler, TarefaBatePapo tbp) {
		this.dispositivoRemoto = dispositivo;
		this.handler = handler;
		BluetoothSocket tempSocket = null;
		try {
			tempSocket = dispositivoRemoto.createRfcommSocketToServiceRecord(MainActivity.UUID_APP);
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... conexão mal sucedida");
				}
			});
		}
		socket = tempSocket;
	}

	@Override
	public void run() {
		try {
			socket.connect();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MainActivity.getInstance().exibeMensagem("problemas... falha no dispositivo Bluetooth");
					}
				});			
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					MainActivity.getInstance().exibeMensagem("problemas... conexão mal sucedida");
				}
			});
			
			return;
		}
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.getInstance().exibePainelBatePapo();
				MainActivity.getInstance().exibeMensagem("conexão iniciada...");
			}
		});
		TarefaBatePapo tBatePapo = new TarefaBatePapo(socket, handler);
		MainActivity.getInstance().setTarefaBatePapo(tBatePapo);
		tBatePapo.start();
	}
	
}
