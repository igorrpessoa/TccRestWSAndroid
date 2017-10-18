package igor.com.br.tccrestwsandroid.entity;

public class Complemento {

	public Complemento(){}
	public Complemento(String nome) {
		super();
		this.nome = nome;
	}
	private Integer id;
	private String nome;
	private Integer valido;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getValido() {
		return valido;
	}

	public void setValido(Integer valido) {
		this.valido = valido;
	}
}
