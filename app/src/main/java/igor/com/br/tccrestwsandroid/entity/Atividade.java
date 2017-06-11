package igor.com.br.tccrestwsandroid.entity;

public class Atividade {

	public Atividade(){}
	public Atividade(int id, String nome, Perfil perfil) {
		super();
		this.id = id;
		this.nome = nome;
		this.perfil = perfil;
	}
	private Integer id;
	private String nome;
	private Perfil perfil;
	
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
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
