package br.com.alexromanelli.android.wifichat.app;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import br.com.alexromanelli.android.wifichat.app.adapter.MensagemAdapter;


public class MainActivity extends ActionBarActivity {
    private static MainActivity INSTANCE;
    public static MainActivity getInstance() {
        return INSTANCE;
    }

    public enum TipoAviso {
        Informacao,
        Erro
    }

    private MensagemAdapter adapter;
    public MensagemAdapter getMensagemAdapter() {
        return adapter;
    }

    private Handler handler;
    private TextView tvAviso;

    private Thread tarefaServidorTCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.INSTANCE = this;
        setContentView(R.layout.activity_main);

        handler = new Handler();

        adapter = new MensagemAdapter(this);
        ListView lvMensagens = (ListView)findViewById(R.id.lvMensagens);
        lvMensagens.setAdapter(adapter);

        final EditText etIpRemoto = (EditText)findViewById(R.id.etIpRemoto);
        final EditText etTextoMensagem = (EditText)findViewById(R.id.etTextoMensagem);
        tvAviso = (TextView)findViewById(R.id.tvAviso);

        etIpRemoto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    etTextoMensagem.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etTextoMensagem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String ipRemoto = etIpRemoto.getText().toString();
                    String textoMensagem = etTextoMensagem.getText().toString();
                    enviarMensagem(ipRemoto, textoMensagem);
                    return true;
                }
                return false;
            }
        });

        // iniciar servidor
        tarefaServidorTCP = new Thread(new TarefaServidorTCP(handler));
        tarefaServidorTCP.start();
    }

    private void enviarMensagem(String ipRemoto, String textoMensagem) {
        Thread tarefaClienteTCP = new Thread(new TarefaClienteTCP(ipRemoto, textoMensagem, handler));
        tarefaClienteTCP.start();
    }

    public void exibirAviso(TipoAviso tipo, String textoAviso) {
        String aviso = textoAviso;
        switch (tipo) {
            case Erro:
                aviso = "Erro: " + aviso;
                break;
            case Informacao:
                aviso = "Info: " + aviso;
                break;
        }
        tvAviso.setText(aviso);
    }

    public void atualizarExibicaoListaMensagens() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sair) {
            finish();
        }
        return true;
    }
}
