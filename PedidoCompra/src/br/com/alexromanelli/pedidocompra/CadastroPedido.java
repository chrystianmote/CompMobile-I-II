package br.com.alexromanelli.pedidocompra;

import java.util.ArrayList;

import br.com.alexromanelli.android.dados.Cliente;
import br.com.alexromanelli.android.dados.Contato;
import br.com.alexromanelli.android.dados.Filial;
import br.com.alexromanelli.android.dados.GerenciadorModelo;
import br.com.alexromanelli.android.dados.Pedido;
import br.com.alexromanelli.android.dados.Produto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CadastroPedido extends Activity {
	private static final int SALVAR = Menu.FIRST;
	private static final int CANCELAR = Menu.FIRST + 1;

	public static final int SALVO = 1;
	public static final int CANCELADO = 0;

	private GerenciadorModelo modeloDados;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SALVAR, Menu.NONE, R.string.menu_salvar);
		menu.add(0, CANCELAR, Menu.NONE, R.string.menu_cancelar);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SALVAR:
			// prepara bundle
			Bundle bundle = new Bundle();
			bundle.putInt(Pedido.ID_PEDIDO, idPedido);
			bundle.putInt(Pedido.ID_CLIENTE,
					((Cliente) spinnerCliente.getSelectedItem()).getIdCliente());
			bundle.putInt(Pedido.ID_FILIAL,
					((Filial) spinnerCliente.getSelectedItem()).getIdFilial());
			bundle.putInt(Pedido.ID_CONTATO,
					((Contato) spinnerCliente.getSelectedItem()).getIdContato());
			bundle.putInt(Pedido.ID_PRODUTO,
					((Produto) spinnerCliente.getSelectedItem()).getIdProduto());
			bundle.putInt(Pedido.QUANTIDADE, getQuantidade());
			bundle.putDouble(Pedido.DESCONTO, getDesconto());

			// define resultado
			Intent i = new Intent();
			i.putExtras(bundle);
			setResult(SALVO, i);
			break;
		case CANCELAR:
			setResult(CANCELADO);
			finish();
			break;
		}
		return true;
	}

	private int idPedido;
	private Spinner spinnerCliente;
	private Spinner spinnerFilial;
	private Spinner spinnerContato;
	private Spinner spinnerProduto;

	private EditText editPrecoUnitario;
	private EditText editQuantidade;
	private EditText editDesconto;
	private EditText editValorPedido;

	private ArrayList<Filial> dadosFilial;
	private ArrayList<Contato> dadosContato;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_pedido);

		inicializarDados();

		spinnerCliente = (Spinner) findViewById(R.id.spinnerPedidoCliente);
		spinnerFilial = (Spinner) findViewById(R.id.spinnerPedidoFilial);
		spinnerContato = (Spinner) findViewById(R.id.spinnerPedidoContato);
		spinnerProduto = (Spinner) findViewById(R.id.spinnerPedidoProduto);
		editPrecoUnitario = (EditText) findViewById(R.id.editPrecoUnitario);
		editQuantidade = (EditText) findViewById(R.id.editQuantidade);
		editDesconto = (EditText) findViewById(R.id.editDesconto);
		editValorPedido = (EditText) findViewById(R.id.editValorPedido);

		preparaEditores();
		preparaSpinnerCliente();
		preparaSpinnerProduto();

		idPedido = -1;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idPedido = extras.getInt(Pedido.ID_PEDIDO);

			int idCliente = extras.getInt(Pedido.ID_CLIENTE);
			int idFilial = extras.getInt(Pedido.ID_FILIAL);
			int idContato = extras.getInt(Pedido.ID_CONTATO);
			int idProduto = extras.getInt(Pedido.ID_PRODUTO);
			int quantidade = extras.getInt(Pedido.QUANTIDADE);
			double desconto = extras.getDouble(Pedido.DESCONTO);
			double valorPedido = extras.getDouble(Pedido.VALOR_PEDIDO);

			setCliente(idCliente);
			preparaSpinnerFilial(idCliente);
			setFilial(idFilial);
			preparaSpinnerContato(idFilial);
			setContato(idContato);
			setProduto(idProduto);
			editQuantidade.setText(Integer.toString(quantidade));
			editDesconto.setText(Double.toString(desconto));
			editValorPedido.setText(Double.toString(valorPedido));
		}
	}

	private void preparaEditores() {
		editPrecoUnitario.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				atualizaValorPedido();
			}
		});

		editQuantidade.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				atualizaValorPedido();
			}
		});

		editDesconto.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				atualizaValorPedido();
			}
		});
	}

	private void atualizaValorPedido() {
		double desconto = getDesconto();
		int quantidade = getQuantidade();
		double precoUnitario = getPrecoUnitario();

		double valorPedido = precoUnitario * quantidade * (1 - desconto);
		editValorPedido.setText(Double.toString(valorPedido));
	}

	private void setCliente(int id) {
		int pos = -1;
		for (Cliente r : modeloDados.getBdCliente()) {
			pos++;
			if (r.getIdCliente() == id)
				break;
		}
		if (pos >= 0)
			spinnerCliente.setSelection(pos, true);
	}

	private void setFilial(int id) {
		int pos = -1;
		for (Filial r : dadosFilial) {
			pos++;
			if (r.getIdFilial() == id)
				break;
		}
		if (pos >= 0)
			spinnerFilial.setSelection(pos, true);
	}

	private void setContato(int id) {
		int pos = -1;
		for (Contato r : dadosContato) {
			pos++;
			if (r.getIdContato() == id)
				break;
		}
		if (pos >= 0)
			spinnerContato.setSelection(pos, true);
	}

	private void setProduto(int id) {
		int pos = -1;
		for (Produto r : modeloDados.getBdProduto()) {
			pos++;
			if (r.getIdProduto() == id)
				break;
		}
		if (pos >= 0)
			spinnerProduto.setSelection(pos, true);
	}

	protected double getPrecoUnitario() {
		if (editPrecoUnitario.getText().toString().length() == 0)
			return 0;
		return Double.parseDouble(editPrecoUnitario.getText().toString());
	}

	protected int getQuantidade() {
		if (editQuantidade.getText().toString().length() == 0)
			return 0;
		return Integer.parseInt(editQuantidade.getText().toString());
	}

	protected double getDesconto() {
		if (editDesconto.getText().toString().length() == 0)
			return 0;
		return Double.parseDouble(editDesconto.getText().toString());
	}

	private void preparaSpinnerProduto() {
		ArrayAdapter<Produto> aa = new ArrayAdapter<Produto>(this,
				android.R.layout.simple_spinner_item,
				modeloDados.getBdProduto());
		spinnerProduto.setAdapter(aa);

		spinnerProduto.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				double precoUnitario = modeloDados.getBdProduto().get(position)
						.getPrecoUnidade();
				editPrecoUnitario.setText(Double.toString(precoUnitario));
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void preparaSpinnerCliente() {
		ArrayAdapter<Cliente> aa = new ArrayAdapter<Cliente>(this,
				android.R.layout.simple_spinner_item,
				modeloDados.getBdCliente());
		spinnerCliente.setAdapter(aa);

		spinnerCliente.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int idCliente = modeloDados.getBdCliente().get(position)
						.getIdCliente();
				preparaSpinnerFilial(idCliente);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	protected void preparaSpinnerFilial(int idCliente) {
		dadosFilial = modeloDados.getFilialPorCliente(idCliente);
		ArrayAdapter<Filial> aa = new ArrayAdapter<Filial>(this,
				android.R.layout.simple_spinner_item, dadosFilial);
		spinnerFilial.setAdapter(aa);

		spinnerFilial.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int idFilial = dadosFilial.get(position).getIdFilial();
				preparaSpinnerContato(idFilial);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void preparaSpinnerContato(int idFilial) {
		dadosContato = modeloDados.getContatoPorFilial(idFilial);
		ArrayAdapter<Contato> aa = new ArrayAdapter<Contato>(this,
				android.R.layout.simple_spinner_item, dadosContato);
		spinnerContato.setAdapter(aa);
	}

	public void inicializarDados() {
		modeloDados = GerenciadorModelo.getInstance();
	}

}
