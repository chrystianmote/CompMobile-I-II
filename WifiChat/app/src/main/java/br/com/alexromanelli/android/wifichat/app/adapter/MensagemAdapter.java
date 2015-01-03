package br.com.alexromanelli.android.wifichat.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.alexromanelli.android.wifichat.app.MainActivity;
import br.com.alexromanelli.android.wifichat.app.R;
import br.com.alexromanelli.android.wifichat.app.dados.Mensagem;

/**
 * Created by alexandre on 11/05/14.
 */
public class MensagemAdapter extends BaseAdapter {

    private static MensagemAdapter INSTANCE;
    public static MensagemAdapter getInstance() {
        return INSTANCE;
    }

    private Context context;
    private ArrayList<Mensagem> listMensagem;

    public MensagemAdapter(Context context) {
        INSTANCE = this;
        this.context = context;
        listMensagem = new ArrayList<Mensagem>();
    }

    @Override
    public int getCount() {
        return listMensagem.size();
    }

    @Override
    public Object getItem(int i) {
        return listMensagem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int ind = listMensagem.size() - i - 1;
        View registro;
        LayoutInflater inflater = LayoutInflater.from(context);
        registro = inflater.inflate(R.layout.item_list_mensagem, viewGroup, false);

        TextView tvItemMsgOrigem = (TextView)registro.findViewById(R.id.tvItemMsgOrigem),
                 tvItemMsgDestino = (TextView)registro.findViewById(R.id.tvItemMsgDestino),
                 tvItemMsgTexto = (TextView)registro.findViewById(R.id.tvItemMsgTexto);

        tvItemMsgOrigem.setText(listMensagem.get(i).getOrigem());
        tvItemMsgDestino.setText(listMensagem.get(i).getDestino());
        tvItemMsgTexto.setText(listMensagem.get(i).getTextoMensagem());

        return registro;
    }

    public void addMensagem(Mensagem msg) {
        listMensagem.add(0, msg);
        MainActivity.getInstance().atualizarExibicaoListaMensagens();
    }
}
