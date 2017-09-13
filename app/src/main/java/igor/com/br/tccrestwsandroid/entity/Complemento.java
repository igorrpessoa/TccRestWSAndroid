package igor.com.br.tccrestwsandroid.entity;

public class Complemento {

	public Complemento(){}
	public Complemento(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	private Integer id;
	private String nome;

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
	
}
