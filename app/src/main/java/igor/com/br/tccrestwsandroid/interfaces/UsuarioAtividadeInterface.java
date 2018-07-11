package igor.com.br.tccrestwsandroid.interfaces;


import java.util.List;

import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Igor on 13/02/2017.
 */

public interface UsuarioAtividadeInterface {

    @POST("UsuarioAtividadeService/fuzzyficar")
    Call<Usuario> fuzzyficar(@Body UsuarioAtividadeVo ua);

    @POST("UsuarioAtividadeService/selectAllUsuarioAtividade")
    Call<List<UsuarioAtividadeVo>> selectAllUsuarioAtividade(@Body Usuario user);

    @POST("UsuarioAtividadeService/editarUsuarioAtividade")
    Call<UsuarioAtividadeVo> editarUsuarioAtividade(@Body UsuarioAtividadeVo ua);

    @POST("UsuarioAtividadeService/selectUsuarioAtividade")
    Call<List<UsuarioAtividadeVo>> selectUsuarioAtividade(@Body UsuarioAtividadeVo ua);

    @POST("UsuarioAtividadeService/deleteUsuarioAtividade")
    Call<Usuario> deleteUsuarioAtividade(@Body UsuarioAtividadeVo atividade);
}
