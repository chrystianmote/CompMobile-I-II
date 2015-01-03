package br.com.alexromanelli.android.dados;

public class Filial {
	private static int SEQUENCIAL = 0;

	private int idFilial;
	private int idCliente;
	private String estado;
	private String cidade;
	private String bairro;
	private String endereco;
	private String numero;
	private String complemento;
	private String telefone;
	private String fax;
	private String eMail;

	public Filial(int idCliente, String estado, String cidade, String bairro,
			String endereco, String numero, String complemento,
			String telefone, String fax, String eMail) {
		super();
		this.idFilial = Filial.SEQUENCIAL++;
		this.idCliente = idCliente;
		this.estado = estado;
		this.cidade = cidade;
		this.bairro = bairro;
		this.endereco = endereco;
		this.numero = numero;
		this.complemento = complemento;
		this.telefone = telefone;
		this.fax = fax;
		this.eMail = eMail;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public int getIdFilial() {
		return idFilial;
	}

	@Override
	public String toString() {
		return "Filial [cidade=" + cidade + ", bairro=" + bairro + "]";
	}

}
