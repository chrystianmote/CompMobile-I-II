package br.com.alexromanelli.android.wifichat.app.dados;

/**
 * Created by alexandre on 11/05/14.
 */
public class Mensagem {

    private String origem;
    private String destino;
    private String textoMensagem;

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTextoMensagem() {
        return textoMensagem;
    }

    public void setTextoMensagem(String textoMensagem) {
        this.textoMensagem = textoMensagem;
    }

    public Mensagem(String origem, String destino, String textoMensagem) {

        this.origem = origem;
        this.destino = destino;
        this.textoMensagem = textoMensagem;
    }
}
