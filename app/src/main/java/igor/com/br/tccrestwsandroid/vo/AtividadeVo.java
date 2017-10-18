package igor.com.br.tccrestwsandroid.vo;

import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;

/**
 * Created by Igor on 24/09/2017.
 */

public class AtividadeVo {

    private Atividade atividade;
    private List<Complemento> complementos;
    private Integer valido;

    public Atividade getAtividade() {
        return atividade;
    }
    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }
    public List<Complemento> getComplementos() {
        return complementos;
    }
    public void setComplementos(List<Complemento> complementos) {
        this.complementos = complementos;
    }

    public Integer getValido() {
        return valido;
    }

    public void setValido(Integer valido) {
        this.valido = valido;
    }
}
