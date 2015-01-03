package br.com.alexromanelli.android.dados;

public class Produto {
	private static int SEQUENCIAL = 0;

	private int idProduto;
	private String descricao;
	private double precoUnidade;

	public Produto(String descricao, double precoUnidade) {
		super();
		this.idProduto = Produto.SEQUENCIAL++;
		this.descricao = descricao;
		this.precoUnidade = precoUnidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getPrecoUnidade() {
		return precoUnidade;
	}

	public void setPrecoUnidade(double precoUnidade) {
		this.precoUnidade = precoUnidade;
	}

	public int getIdProduto() {
		return idProduto;
	}

	@Override
	public String toString() {
		return "Produto [idProduto=" + idProduto + ", descricao=" + descricao
				+ ", precoUnidade=" + precoUnidade + "]";
	}

}
