package igor.com.br.tccrestwsandroid.interfaces;


import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Igor on 13/02/2017.
 */

public interface AtividadeInterface {

    @POST("AtividadeService/sugestaoAtividade")
    Call<UsuarioAtividadeVo> sugestaoAtividade(@Body Usuario user);

    @POST("AtividadeService/salvarAtividade")
    Call<Atividade> salvarAtividade(@Body UsuarioAtividade usuarioAtividade);

    @POST("AtividadeService/buscaAtividade")
    Call<AtividadeVo> buscaAtividade(@Body AtividadeVo atividade);

    @POST("AtividadeService/selectAtividades")
    Call<List<AtividadeVo>> selecionaAtividades(@Body AtividadeVo atividade);
}
