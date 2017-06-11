package igor.com.br.tccrestwsandroid.interfaces;


import igor.com.br.tccrestwsandroid.entity.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Igor on 13/02/2017.
 */

public interface UsuarioInterface {

    @POST("UsuarioService/selectUsuario")
    Call<Usuario> selectUsuario(@Body Usuario user);

    @POST("UsuarioService/saveUsuario")
    Call<Usuario> saveUsuario(@Body Usuario usuario);

    @POST("UsuarioService/login")
    Call<Usuario> login(@Body Usuario usuario);
}
