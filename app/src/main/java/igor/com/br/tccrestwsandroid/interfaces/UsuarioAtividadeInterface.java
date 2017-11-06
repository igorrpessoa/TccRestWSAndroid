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
    Call<Usuario> fuzzyficar(@Body UsuarioAtividadeVo user);

    @POST("UsuarioAtividadeService/selectAllUsuarioAtividade")
    Call<List<AtividadeVo>> selectAllUsuarioAtividade(@Body Usuario user);
}
