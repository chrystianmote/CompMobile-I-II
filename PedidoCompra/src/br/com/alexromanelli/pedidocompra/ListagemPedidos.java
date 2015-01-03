package br.com.alexromanelli.pedidocompra;

import br.com.alexromanelli.android.dados.GerenciadorModelo;
import br.com.alexromanelli.android.dados.Pedido;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListagemPedidos extends ListActivity {

	private static final int ACTIVITY_EDIT = 0;
	private static final int ACTIVITY_CREATE = 1;
	private static int MENU_CADASTRAR_PEDIDO = Menu.FIRST;
	private static int MENU_SAIR = Menu.FIRST + 1;

	private GerenciadorModelo modeloDados;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listagem_pedidos);

		modeloDados = GerenciadorModelo.getInstance();

		ArrayAdapter<Pedido> aa = new ArrayAdapter<Pedido>(this,
				R.layout.linha_pedido, modeloDados.getBdPedido());
		setListAdapter(aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CADASTRAR_PEDIDO, Menu.NONE,
				R.string.listagem_pedidos_menuitem_cadastrar);
		menu.add(0, MENU_SAIR, Menu.NONE,
				R.string.listagem_pedidos_menuitem_sair);

		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Pedido p = modeloDados.getBdPedido().get(position);

		Intent i = new Intent(ListagemPedidos.this, CadastroPedido.class);

		i.putExtra(Pedido.ID_PEDIDO, p.getIdPedido());
		i.putExtra(Pedido.ID_CLIENTE, p.getIdCliente());
		i.putExtra(Pedido.ID_FILIAL, p.getIdFilial());
		i.putExtra(Pedido.ID_CONTATO, p.getIdContato());
		i.putExtra(Pedido.ID_PRODUTO, p.getIdProduto());
		i.putExtra(Pedido.QUANTIDADE, p.getQuantidade());
		i.putExtra(Pedido.DESCONTO, p.getDesconto());
		i.putExtra(Pedido.VALOR_PEDIDO, p.getValorPedido());

		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == CadastroPedido.CANCELADO)
			return;

		Bundle extras = intent.getExtras();

		switch (requestCode) { // TODO TRATAR RETORNO DE DADOS!
		case ACTIVITY_CREATE:
			break;
		case ACTIVITY_EDIT:
			int idPedido = extras.getInt(Pedido.ID_PEDIDO);
			break;
		}
	}
}
