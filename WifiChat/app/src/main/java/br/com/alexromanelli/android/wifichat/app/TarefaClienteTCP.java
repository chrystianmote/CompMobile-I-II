package br.com.alexromanelli.android.wifichat.app;

import android.os.Handler;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import br.com.alexromanelli.android.wifichat.app.dados.Mensagem;

/**
 * Created by alexandre on 11/05/14.
 */
public class TarefaClienteTCP implements Runnable {

    private String ipDestino;
    private String textoMensagem;
    private Handler activityHandler;

    public TarefaClienteTCP(String ipDestino, String textoMensagem, Handler activityHandler) {
        this.ipDestino = ipDestino;
        this.textoMensagem = textoMensagem;
        this.activityHandler = activityHandler;
    }

    @Override
    public void run() {
        try {
            InetAddress enderecoServidor = InetAddress.getByName(ipDestino);
            exibirAviso(MainActivity.TipoAviso.Informacao, "Conectando...");
            Socket tcpSocket = new Socket(enderecoServidor.getHostAddress(), TarefaServidorTCP.PORTA_CHAT);

            byte[] mensagem = textoMensagem.getBytes(TarefaServidorTCP.CHARSET_NAME);

            OutputStream out = tcpSocket.getOutputStream();
            out.write(mensagem);

            String ipLocal = ((InetSocketAddress)tcpSocket.getLocalSocketAddress()).getAddress().getHostAddress();

            Mensagem msg = new Mensagem(ipLocal, ipDestino, textoMensagem);
            MainActivity.getInstance().getMensagemAdapter().addMensagem(msg);
            exibirAviso(MainActivity.TipoAviso.Informacao, "Mensagem enviada para " + ipDestino);

        } catch (Exception e) {
            exibirAviso(MainActivity.TipoAviso.Erro, "Falha ao enviar para " + ipDestino);
        }
    }

    private void exibirAviso(final MainActivity.TipoAviso tipo, final String textoAviso) {
        activityHandler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.getInstance().exibirAviso(tipo, textoAviso);
            }
        });
    }


}
