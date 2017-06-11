package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edit_text_email)
    EditText editLogin;
    @BindView(R.id.edit_text_password)
    EditText editPassword;

    private Retrofit retrofit;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        Usuario usuarioLogado = new Gson().fromJson(json,Usuario.class);
        if(usuarioLogado != null){
            vaiParaMain();
        }
    }

    private void vaiParaMain(){
        Intent intent = new Intent(mContext,MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void btnLoginClick() {
        Usuario u = new Usuario();
        u.setEmail(editLogin.getText().toString());
        u.setSenha(editPassword.getText().toString());
        retrofit = new RetrofitUtil().createRetrofit();
        UsuarioInterface i  = retrofit.create(UsuarioInterface.class);
        Call<Usuario> call = i.login(u);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                dialog.dismiss();
                if(response!= null){
                    Usuario usuarioLogado = response.body();
                    if(usuarioLogado != null && usuarioLogado.getId() != null){
                        Intent intent = new Intent(mContext,MainActivity.class);
                        setSharedPreferences(mContext,mContext.getString(R.string.usuario_logado),new Gson().toJson(usuarioLogado));
                        startActivity(intent);
                    }else{
                        editPassword.setError("Senha incorreta");
                    }
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


}
