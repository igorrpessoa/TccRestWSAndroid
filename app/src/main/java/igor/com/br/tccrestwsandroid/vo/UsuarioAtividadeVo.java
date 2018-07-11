package igor.com.br.tccrestwsandroid.vo;

import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.AtividadeComplemento;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;

/**
 * Created by Igor on 10/09/2017.
 */

public class UsuarioAtividadeVo {
    private Integer id;
    private Usuario usuario;
    private List<AtividadeComplemento> atividadeComplementos;
    private List<Complemento> complementos;
    private Atividade atividade;
    private Double frequencia;
    private Double satisfacao;
    private Perfil perfil;
    private Double popularidade;
    private Double relacao;
    private String resposta;

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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    public List<Complemento> getComplementos() {
        return complementos;
    }
    public void setComplementos(List<Complemento> complementos) {
        this.complementos = complementos;
    }
    public Atividade getAtividade() {
        return atividade;
    }
    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }
    public List<AtividadeComplemento> getAtividadeComplemento() {
        return atividadeComplementos;
    }
    public void setAtividadeComplemento(List<AtividadeComplemento> atividadeComplementos) {
        this.atividadeComplementos = atividadeComplementos;
    }
    public Double getPopularidade() {
        return popularidade;
    }
    public void setPopularidade(Double popularidade) {
        this.popularidade = popularidade;
    }
    public Double getRelacao() {
        return relacao;
    }
    public void setRelacao(Double relacao) {
        this.relacao = relacao;
    }
    public String getResposta() {
        return resposta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
