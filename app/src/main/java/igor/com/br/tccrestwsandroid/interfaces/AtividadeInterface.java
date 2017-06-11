package igor.com.br.tccrestwsandroid.interfaces;


import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Igor on 13/02/2017.
 */

public interface AtividadeInterface {

    @POST("AtividadeService/selectAllAtividade")
    Call<List<Atividade>> selectAllAtividade(@Body Usuario user);

}
