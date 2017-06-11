package igor.com.br.tccrestwsandroid.entity;

public class Perfil {

	private Integer id;
	private Double saude;
	private Double social;
	private Double intelecto;
	private Double artistico;

	public Perfil(){};

	public Perfil(Integer id, Double saude, Double social, Double intelecto, Double artistico) {
		this.id = id;
		this.saude = saude;
		this.social = social;
		this.intelecto = intelecto;
		this.artistico = artistico;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getSaude() {
		return saude;
	}

	public void setSaude(Double saude) {
		this.saude = saude;
	}

	public Double getSocial() {
		return social;
	}

	public void setSocial(Double social) {
		this.social = social;
	}

	public Double getIntelecto() {
		return intelecto;
	}

	public void setIntelecto(Double intelecto) {
		this.intelecto = intelecto;
	}

	public Double getArtistico() {
		return artistico;
	}

	public void setArtistico(Double artistico) {
		this.artistico = artistico;
	}
}
