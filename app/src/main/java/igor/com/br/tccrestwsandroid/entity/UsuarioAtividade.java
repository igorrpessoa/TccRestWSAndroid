package igor.com.br.tccrestwsandroid.entity;

public class UsuarioAtividade {

	public UsuarioAtividade(){}
	
	public UsuarioAtividade(Integer id, Usuario usuario, Atividade atividade, Double frequencia, Double satisfacao) {
		this.id = id;
		this.usuario = usuario;
		this.atividade = atividade;
		this.frequencia = frequencia;
		this.satisfacao = satisfacao;
	}
	
	private Integer id;
	private Usuario usuario;
	private Atividade atividade;
	private Double frequencia;
	private Double satisfacao;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Atividade getAtividade() {
		return atividade;
	}
	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}
	public Double getFrequencia() {
		return frequencia;
	}
	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}
	public Double getSatisfacao() {
		return satisfacao;
	}
	public void setSatisfacao(Double satisfacao) {
		this.satisfacao = satisfacao;
	}
}
