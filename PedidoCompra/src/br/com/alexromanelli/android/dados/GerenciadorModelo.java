package br.com.alexromanelli.android.dados;

import java.util.ArrayList;

public class GerenciadorModelo {

	// singleton
	public static GerenciadorModelo instance;

	public static GerenciadorModelo getInstance() {
		if (instance == null) {
			instance = new GerenciadorModelo();
			instance.carregaDadosVolateis();
		}
		return instance;
	}

	// repositÃ³rios de dados
	private ArrayList<Produto> bdProduto;
	private ArrayList<Cliente> bdCliente;
	private ArrayList<Filial> bdFilial;
	private ArrayList<Contato> bdContato;
	private ArrayList<Pedido> bdPedido;

	public ArrayList<Produto> getBdProduto() {
		return bdProduto;
	}

	public ArrayList<Cliente> getBdCliente() {
		return bdCliente;
	}

	public ArrayList<Filial> getBdFilial() {
		return bdFilial;
	}

	public ArrayList<Contato> getBdContato() {
		return bdContato;
	}

	public ArrayList<Pedido> getBdPedido() {
		return bdPedido;
	}

	public ArrayList<Filial> getFilialPorCliente(int idCliente) {
		ArrayList<Filial> dados = new ArrayList<Filial>();
		for (Filial r : bdFilial)
			if (r.getIdCliente() == idCliente)
				dados.add(r);
		return dados;
	}

	public ArrayList<Contato> getContatoPorFilial(int idFilial) {
		ArrayList<Contato> dados = new ArrayList<Contato>();
		for (Contato r : bdContato)
			if (r.getIdFilial() == idFilial)
				dados.add(r);
		return dados;
	}

	public GerenciadorModelo() {
		bdProduto = new ArrayList<Produto>();
		bdCliente = new ArrayList<Cliente>();
		bdFilial = new ArrayList<Filial>();
		bdContato = new ArrayList<Contato>();
		bdPedido = new ArrayList<Pedido>();
	}

	public void carregaDadosVolateis() {
		carregaProdutos();
		carregaClientes();
		carregaFiliais();
		carregaContatos();
		carregaPedidos();
	}

	public int buscaIdClientePorRazaoSocial(String razaoSocial) {
		int idCliente = -1;
		for (Cliente c : bdCliente)
			if (c.getRazaoSocial() == razaoSocial) {
				idCliente = c.getIdCliente();
				break;
			}
		return idCliente;
	}

	public int buscaIdProdutoPorDescricao(String descricao) {
		int idProduto = -1;
		for (Produto p : bdProduto)
			if (p.getDescricao() == descricao) {
				idProduto = p.getIdProduto();
				break;
			}
		return idProduto;
	}

	public String buscaDescricaoProdutoPorId(int idProduto) {
		String descricao = "";
		for (Produto p : bdProduto)
			if (p.getIdProduto() == idProduto) {
				descricao = p.getDescricao();
				break;
			}
		return descricao;
	}

	public double buscaPrecoUnidadeProdutoPorId(int idProduto) {
		double precoUnidade = 0.0;
		for (Produto p : bdProduto)
			if (p.getIdProduto() == idProduto) {
				precoUnidade = p.getPrecoUnidade();
				break;
			}
		return precoUnidade;
	}

	private int buscaIdContatoPorNome(String nome) {
		int idContato = -1;
		for (Contato c : bdContato)
			if (c.getNome() == nome) {
				idContato = c.getIdContato();
				break;
			}
		return idContato;
	}

	private int buscaIdFilialPorIdClientePorCidade(int idCliente, String cidade) {
		int idFilial = -1;
		for (Filial f : bdFilial)
			if (f.getCidade() == cidade) {
				idFilial = f.getIdFilial();
				break;
			}
		return idFilial;
	}

	private void carregaPedidos() {
		int idCliente = buscaIdClientePorRazaoSocial("Distribuidora Dijkstra S.A.");
		int idFilial = buscaIdFilialPorIdClientePorCidade(idCliente,
				"Cachoeiro de Itapemirim");
		int idContato = buscaIdContatoPorNome("Edsger Dijkstra");
		int idProduto = buscaIdProdutoPorDescricao("Refrigerante Cactus-Cola 1,5l");
		double precoUnidade = buscaPrecoUnidadeProdutoPorId(idProduto);
		int quantidade = 100;
		double desconto = 0.1;
		double valorPedido = precoUnidade * quantidade * desconto;
		bdPedido.add(new Pedido(idCliente, idFilial, idContato, idProduto,
				quantidade, desconto, valorPedido));

		idCliente = buscaIdClientePorRazaoSocial("Distribuidora Dijkstra S.A.");
		idFilial = buscaIdFilialPorIdClientePorCidade(idCliente,
				"Cachoeiro de Itapemirim");
		idContato = buscaIdContatoPorNome("Niklaus Wirth");
		idProduto = buscaIdProdutoPorDescricao("Cerveja Duff Lata");
		precoUnidade = buscaPrecoUnidadeProdutoPorId(idProduto);
		quantidade = 200;
		desconto = 0.15;
		valorPedido = precoUnidade * quantidade * desconto;
		bdPedido.add(new Pedido(idCliente, idFilial, idContato, idProduto,
				quantidade, desconto, valorPedido));

		idCliente = buscaIdClientePorRazaoSocial("Distribuidora Dijkstra S.A.");
		idFilial = buscaIdFilialPorIdClientePorCidade(idCliente, "Marataí­zes");
		idContato = buscaIdContatoPorNome("Wo Song");
		idProduto = buscaIdProdutoPorDescricao("Água mineral 30l");
		precoUnidade = buscaPrecoUnidadeProdutoPorId(idProduto);
		quantidade = 50;
		desconto = 0.05;
		valorPedido = precoUnidade * quantidade * desconto;
		bdPedido.add(new Pedido(idCliente, idFilial, idContato, idProduto,
				quantidade, desconto, valorPedido));

		idCliente = buscaIdClientePorRazaoSocial("Distribuidora Dijkstra S.A.");
		idFilial = buscaIdFilialPorIdClientePorCidade(idCliente, "Marataí­zes");
		idContato = buscaIdContatoPorNome("Julia Bennel");
		idProduto = buscaIdProdutoPorDescricao("Suco de Besouro 1l");
		precoUnidade = buscaPrecoUnidadeProdutoPorId(idProduto);
		quantidade = 80;
		desconto = 0.1;
		valorPedido = precoUnidade * quantidade * desconto;
		bdPedido.add(new Pedido(idCliente, idFilial, idContato, idProduto,
				quantidade, desconto, valorPedido));
	}

	private void carregaContatos() {
		int idDijkstra = buscaIdClientePorRazaoSocial("Distribuidora Dijkstra S.A.");
		for (Filial f : bdFilial) {
			int idCliente = f.getIdCliente();
			int idFilial = f.getIdFilial();
			String cidade = f.getCidade();
			if (idCliente == idDijkstra && cidade == "Cachoeiro de Itapemirim") {
				bdContato.add(new Contato(idCliente, idFilial,
						"Edsger Dijkstra", 'M', "Diretor", "(28)0000-0000",
						"dijkstra@email.com"));
				bdContato.add(new Contato(idCliente, idFilial, "Niklaus Wirth",
						'M', "Gerente", "(28)0000-0001", "wirth@email.com"));
			} else if (idCliente == idDijkstra && cidade == "Marataí­zes") {
				bdContato.add(new Contato(idCliente, idFilial, "Wo Song", 'M',
						"Vendedor", "(28)0001-0000", "song@email.com"));
				bdContato.add(new Contato(idCliente, idFilial, "Julia Bennel",
						'F', "Vendedor", "(28)0001-0001", "bennel@email.com"));
			}
		}
	}

	private void carregaFiliais() {
		for (Cliente c : bdCliente) {
			int idCliente = c.getIdCliente();
			String razaoSocial = c.getRazaoSocial();
			if (razaoSocial == "Distribuidora Dijkstra S.A.") {
				bdFilial.add(new Filial(idCliente, "ES",
						"Cachoeiro de Itapemirim", "Aeroporto",
						"R. Sei lá o quê", "40", "-", "(28)3521-0001",
						"(28)3521-0002", "dijkstra.cachoeiro@email.com"));
				bdFilial.add(new Filial(idCliente, "ES", "Marataí­zes",
						"Arraias", "R. Antenor dos Santos Galante", "1230",
						"-", "(28)3532-0001", "(28)3532-0002",
						"dijkstra.marataizes@email.com"));
			} else if (razaoSocial == "Bar Knuth") {
				bdFilial.add(new Filial(idCliente, "ES", "Castelo", "Centro",
						"Av. Nossa Senhora da Penha", "1938271", "-",
						"(28)3500-0001", "(28)3500-0002",
						"knuth.castelo@email.com"));
				bdFilial.add(new Filial(idCliente, "ES",
						"Cachoeiro de Itapemirim", "Conduru",
						"R. Aquela ali por dentro", "2", "-", "(28)3532-0003",
						"(28)3532-0004", "knuth.conduru@email.com"));
			} else if (razaoSocial == "Restaurante Milenkovic") {
				bdFilial.add(new Filial(idCliente, "ES", "Piúma", "Centro",
						"R. Alguma coisa", "20", "-", "(28)3520-0000",
						"(28)3520-0001", "milenkovic.piuma@email.com"));
				bdFilial.add(new Filial(idCliente, "ES", "Iriri", "Centro",
						"R. Principal", "3", "-", "(28)3530-0001",
						"(28)3530-0002", "milenkovic.iriri@email.com"));
				bdFilial.add(new Filial(idCliente, "ES", "Anchieta",
						"CaÃ§Ãµes", "R. De lá", "13", "-", "(28)3534-0001",
						"(28)3534-0002", "milenkovic.anchieta@email.com"));
			} else if (razaoSocial == "Churrascaria Minkowski") {
				bdFilial.add(new Filial(idCliente, "ES",
						"Cachoeiro de Itapemirim", "União",
						"R. Botas de Judas", "189", "-", "(28)3522-0000",
						"(28)3522-0001", "mink.cachoeiro@email.com"));
				bdFilial.add(new Filial(idCliente, "ES", "Castelo", "Aracuí",
						"R. Única", "5", "-", "(28)3531-0001", "(28)3531-0002",
						"mink.aracui@email.com"));
				bdFilial.add(new Filial(idCliente, "ES", "Mimoso do Sul",
						"Centro", "R. Desconhecida", "7", "-", "(28)3534-0005",
						"(28)3534-0006", "mink.mimoso@email.com"));
			}
		}
	}

	private void carregaClientes() {
		bdCliente.add(new Cliente("Distribuidora Dijkstra S.A.",
				"000.000.000/0000-0"));
		bdCliente.add(new Cliente("Bar Knuth", "000.000.000/0000-0"));
		bdCliente.add(new Cliente("Restaurante Milenkovic",
				"000.000.000/0000-0"));
		bdCliente.add(new Cliente("Churrascaria Minkowski",
				"000.000.000/0000-0"));
	}

	private void carregaProdutos() {
		bdProduto.add(new Produto("Água mineral 30l", 15.0));
		bdProduto.add(new Produto("Refrigerante Cactus-Cola 1,5l", 3.5));
		bdProduto.add(new Produto("Cerveja Duff Lata", 2.0));
		bdProduto.add(new Produto("Suco de Besouro 1l", 3.0));
	}
}
