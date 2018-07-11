package igor.com.br.tccrestwsandroid.vo;

import java.util.ArrayList;
import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Perfil;

/**
 * Created by Igor on 24/09/2017.
 */

public class AtividadeVo {

    private Atividade atividade;
    private List<Complemento> complementos;
    private Integer valido;
    private Perfil perfil;
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

    public AtividadeVo(Atividade atividade, List<Complemento> complementos, Integer valido, Perfil perfil) {
        this.atividade = atividade;
        this.complementos = complementos;
        this.valido = valido;
        this.perfil = perfil;
    }

    public AtividadeVo() {
    }
    public AtividadeVo(UsuarioAtividadeVo ua) {
        atividade = ua.getAtividade();
        List<Complemento> complementosAux = new ArrayList<>();
        for(Complemento c : ua.getComplementos()){
            if(c != null)
                complementosAux.add(c);
        }
        complementos = complementosAux;
        perfil = ua.getPerfil();
    }

    public Integer getValido() {
        return valido;
    }

    public void setValido(Integer valido) {
        this.valido = valido;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
