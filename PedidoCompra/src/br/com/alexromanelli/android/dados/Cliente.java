package br.com.alexromanelli.android.dados;

public class Cliente {
	private static int SEQUENCIAL = 0;

	private int idCliente;
	private String razaoSocial;
	private String cnpj;

	public Cliente(String razaoSocial, String cnpj) {
		super();
		this.idCliente = Cliente.SEQUENCIAL++;
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return razaoSocial;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
