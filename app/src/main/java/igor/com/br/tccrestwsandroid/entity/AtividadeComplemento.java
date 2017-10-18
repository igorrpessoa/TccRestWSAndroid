package igor.com.br.tccrestwsandroid.entity;

import java.util.List;

public class AtividadeComplemento {

	public AtividadeComplemento(){}
	public AtividadeComplemento(int id) {
		super();
		this.id = id;
	}
	private Integer id;
	private Atividade atividade;
	private List<Complemento> complemento;

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

	public List<Complemento> getComplemento() {
		return complemento;
	}

	public void setComplemento(List<Complemento> complemento) {
		this.complemento = complemento;
	}
}
