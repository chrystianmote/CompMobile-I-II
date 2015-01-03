package br.com.alexromanelli.android.dados;

public class Pedido {
	public static String ID_PEDIDO = "idPedido";
	public static String ID_CLIENTE = "idCliente";
	public static String ID_FILIAL = "idFilial";
	public static String ID_CONTATO = "idContato";
	public static String ID_PRODUTO = "idProduto";
	public static String QUANTIDADE = "quantidade";
	public static String DESCONTO = "desconto";
	public static String VALOR_PEDIDO = "valorPedido";

	private static int SEQUENCIAL = 0;

	private int idPedido;
	private int idCliente;
	private int idFilial;
	private int idContato;
	private int idProduto;
	private int quantidade;
	private double desconto;
	private double valorPedido;

	public Pedido(int idCliente, int idFilial, int idContato, int idProduto,
			int quantidade, double desconto, double valorPedido) {
		super();
		this.idPedido = Pedido.SEQUENCIAL++;
		this.idCliente = idCliente;
		this.idFilial = idFilial;
		this.idContato = idContato;
		this.idProduto = idProduto;
		this.quantidade = quantidade;
		this.desconto = desconto;
		this.valorPedido = valorPedido;
	}

	@Override
	public String toString() {
		return "Pedido [idPedido=" + idPedido + ", idCliente=" + idCliente
				+ ", idProduto=" + idProduto + ", quantidade=" + quantidade
				+ ", desconto=" + desconto + ", valorPedido=" + valorPedido
				+ "]";
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(int idFilial) {
		this.idFilial = idFilial;
	}

	public int getIdContato() {
		return idContato;
	}

	public void setIdContato(int idContato) {
		this.idContato = idContato;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public double getValorPedido() {
		return valorPedido;
	}

	public void setValorPedido(double valorPedido) {
		this.valorPedido = valorPedido;
	}

	public int getIdPedido() {
		return idPedido;
	}

}
