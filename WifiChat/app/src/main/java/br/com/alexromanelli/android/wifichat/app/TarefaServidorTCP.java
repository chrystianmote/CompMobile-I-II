package br.com.alexromanelli.android.wifichat.app;

import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import br.com.alexromanelli.android.wifichat.app.dados.Mensagem;

/**
 * A classe TarefaServidorTCP é responsável por gerenciar o socket TCP que ficará disponível
 * para receber conexões de clientes na porta definida no atributo PORTA_CHAT. A cada mudança no
 * estado desta tarefa, uma mensagem sobre o estado atual é exibida na atividade principal.
 *
 * A implementação dos métodos para manipular os sockets de rede foram baseadas no código
 * compartilhado por Jason Wei, disponível em:
 *
 * http://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 *
 */
public class TarefaServidorTCP implements Runnable {

    public static final int PORTA_CHAT = 13000;
    public static final String CHARSET_NAME = "UTF-8";
    private static final int TAM_BYTES_PACOTE = 4096;

    private Handler activityHandler;
    private ServerSocket tcpServerSocket;

    public TarefaServidorTCP(Handler handler) {
        activityHandler = handler;
    }

    private void exibirAviso(final MainActivity.TipoAviso tipo, final String textoAviso) {
        activityHandler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.getInstance().exibirAviso(tipo, textoAviso);
            }
        });
    }

    @Override
    public void run() {
        try {
            executaServidorTCP();
        } catch (final Exception e) {
            exibirAviso(MainActivity.TipoAviso.Erro, e.getMessage());
        }
    }

    private void executaServidorTCP() throws Exception {
        String ipLocal = getLocalIpAddress();
        if (ipLocal != null) {
            tcpServerSocket = new ServerSocket(PORTA_CHAT);
            while (true) {
                exibirAviso(MainActivity.TipoAviso.Informacao, "Aguardando conexão...");
                final Socket tcpClient = tcpServerSocket.accept();
                exibirAviso(MainActivity.TipoAviso.Informacao, "Conectado");

                Thread threadRecebeDados = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recebeDados(tcpClient);
                    }
                });
                threadRecebeDados.start();
            }
        }
        else {
            throw new Exception("Sem conexão.");
        }
    }

    private void recebeDados(Socket tcpClient) {
        try {
            InputStream in = tcpClient.getInputStream();
            byte[] message = new byte[TAM_BYTES_PACOTE];
            int bytesRead;
            while (true) {
                bytesRead = 0;
                try {
                    bytesRead = in.read(message, 0, TAM_BYTES_PACOTE);
                }
                catch (Exception e) {
                    break;
                }
                if (bytesRead == 0)
                    break;

                String ipRemoto = ((InetSocketAddress)tcpClient.getRemoteSocketAddress()).getAddress().getHostAddress();
                String ipLocal = ((InetSocketAddress)tcpClient.getLocalSocketAddress()).getAddress().getHostAddress();
                String textoRecebido = new String(message, CHARSET_NAME);

                Mensagem msg = new Mensagem(ipRemoto, ipLocal, textoRecebido);
                MainActivity.getInstance().getMensagemAdapter().addMensagem(msg);
                exibirAviso(MainActivity.TipoAviso.Informacao, "Mensagem recebida de " + ipRemoto);
            }
            tcpClient.close();
        } catch (IOException e) {
            exibirAviso(MainActivity.TipoAviso.Erro, "Conexão perdida.");
        }
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            exibirAviso(MainActivity.TipoAviso.Erro, "Adaptador de rede indisponível.");
        }
        return null;
    }
}
