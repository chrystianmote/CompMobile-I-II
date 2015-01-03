package br.com.alexromanelli.android.dados;

public class Contato {
	private static int SEQUENCIAL = 0;

	private int idContato;
	private int idCliente;
	private int idFilial;
	private String nome;
	private char genero;
	private String funcao;
	private String telefone;
	private String eMail;

	public Contato(int idCliente, int idFilial, String nome, char genero,
			String funcao, String telefone, String eMail) {
		super();
		this.idContato = Contato.SEQUENCIAL++;
		this.idCliente = idCliente;
		this.idFilial = idFilial;
		this.nome = nome;
		this.genero = genero;
		this.funcao = funcao;
		this.telefone = telefone;
		this.eMail = eMail;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public char getGenero() {
		return genero;
	}

	public void setGenero(char genero) {
		this.genero = genero;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public int getIdContato() {
		return idContato;
	}

	@Override
	public String toString() {
		return nome;
	}

}
