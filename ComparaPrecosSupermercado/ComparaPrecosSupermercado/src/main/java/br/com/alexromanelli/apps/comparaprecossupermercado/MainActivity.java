package br.com.alexromanelli.apps.comparaprecossupermercado;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ArrayList<Double> precoList;
    private ArrayList<Double> quantidadeList;

    private ArrayList<EditText> editPrecoList;
    private ArrayList<EditText> editQuantidadeList;

    private ArrayList<TextView> tvClassificacaoList;

    private void atualizaValores() {
        precoList.clear();
        quantidadeList.clear();

        StringBuilder sbValor = new StringBuilder();
        for (EditText ed : editPrecoList) {
            sbValor.delete(0, sbValor.length());
            sbValor.append(ed.getText());
            double preco = Double.MAX_VALUE;
            if (sbValor.length() > 0)
                preco = Double.parseDouble(sbValor.toString());
            precoList.add(preco);
        }
        for (EditText ed : editQuantidadeList) {
            sbValor.delete(0, sbValor.length());
            sbValor.append(ed.getText());
            double quantidade = Double.MIN_VALUE; // Double.MIN_VALUE é o valor mais próximo de 0.0
            if (sbValor.length() > 0)
                quantidade = Double.parseDouble(sbValor.toString());
            quantidadeList.add(quantidade);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        editPrecoList = new ArrayList<EditText>();
        editQuantidadeList = new ArrayList<EditText>();
        tvClassificacaoList = new ArrayList<TextView>();

        precoList = new ArrayList<Double>();
        quantidadeList = new ArrayList<Double>();
    }

    private void atualizaClassificacao() {
        atualizaValores();

        int numElementos = precoList.size();
        double[] relacao = new double[numElementos];
        int[] classificacao = new int[numElementos];
        double[] afastamento = new double[numElementos];

        // calcula relações preço/quantidade
        for (int i = 0; i < numElementos; i++) {
            relacao[i] = precoList.get(i) / quantidadeList.get(i);
        }

        double menorRelacao = Double.MAX_VALUE;

        // faz a classificação
        for (int i = 0; i < numElementos; i++) {
            int quantidadeMenores = 0;
            for (int j = 0; j < numElementos; j++)
                if (relacao[j] < relacao[i])
                    quantidadeMenores++;
            classificacao[i] = quantidadeMenores + 1;

            // encontra a menor relação entre todas
            if (quantidadeMenores == 0)
                menorRelacao = relacao[i];
        }

        // monta um pequeno relatório com o afastamento de cada item para o que tem a menor relação
        for (int i = 0; i < numElementos; i++) {
            double dif = relacao[i] - menorRelacao;
            afastamento[i] = 100.0 * dif / menorRelacao;
        }

        // exibe resultados
        for (int i = 0; i < numElementos; i++) {
            String s = classificacao[i] + "º";
            if (classificacao[i] > 1) {
                int af = (int)Math.round(afastamento[i] * 10);
                s += " [+" + Integer.toString(af / 10) + "." +
                        Integer.toString(af % 10) + "%]";
            }
            tvClassificacaoList.get(i).setText(s);
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            editPrecoList.add((EditText)rootView.findViewById(R.id.editPrecoA));
            editQuantidadeList.add((EditText)rootView.findViewById(R.id.editQuantidadeA));
            tvClassificacaoList.add((TextView)rootView.findViewById(R.id.tvClassificacaoA));
            editPrecoList.add((EditText)rootView.findViewById(R.id.editPrecoB));
            editQuantidadeList.add((EditText)rootView.findViewById(R.id.editQuantidadeB));
            tvClassificacaoList.add((TextView)rootView.findViewById(R.id.tvClassificacaoB));
            editPrecoList.add((EditText)rootView.findViewById(R.id.editPrecoC));
            editQuantidadeList.add((EditText)rootView.findViewById(R.id.editQuantidadeC));
            tvClassificacaoList.add((TextView)rootView.findViewById(R.id.tvClassificacaoC));
            editPrecoList.add((EditText)rootView.findViewById(R.id.editPrecoD));
            editQuantidadeList.add((EditText)rootView.findViewById(R.id.editQuantidadeD));
            tvClassificacaoList.add((TextView)rootView.findViewById(R.id.tvClassificacaoD));

            Button btAtualizarClassificacao = (Button)rootView.findViewById(R.id.btAtualizarClassificacao);
            btAtualizarClassificacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    atualizaClassificacao();
                }
            });

            return rootView;
        }
    }

}
