package igor.com.br.tccrestwsandroid.entity;

public class AtividadeComplemento {

	public AtividadeComplemento(){}
	public AtividadeComplemento(int id) {
		super();
		this.id = id;
	}
	private Integer id;
	private Atividade atividade;
	private Complemento complemento;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Complemento getComplemento() {
		return complemento;
	}

	public void setComplemento(Complemento complemento) {
		this.complemento = complemento;
	}
}
