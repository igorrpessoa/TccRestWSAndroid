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

public class CadastroLoginActivity extends BaseActivity {

    @BindView(R.id.edit_text_email)
    EditText editLogin;
    @BindView(R.id.edit_text_password)
    EditText editPassword;
    @BindView(R.id.edit_text_nome)
    EditText editNome;

    private Retrofit retrofit;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick(R.id.btn_cadastrar)
    public void btnCadastro() {
        Usuario u = new Usuario();
        String email = editLogin.getText().toString();
        String senha = editPassword.getText().toString();
        String nome = editNome.getText().toString();
        if (validaCadastro(nome,email,senha)) {
            u.setEmail(email);
            u.setNome(nome);
            u.setSenha(senha);
            retrofit = new RetrofitUtil().createRetrofit();
            UsuarioInterface i = retrofit.create(UsuarioInterface.class);
            Call<Usuario> call = i.saveUsuario(u);
            dialog = ProgressDialog.show(this, "", "Por favor aguarde...", false);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    dialog.dismiss();
                    if (response != null) {
                        Usuario usuarioLogado = response.body();
                        if (usuarioLogado != null && usuarioLogado.getId() != null) {
                            vaiParaLogin(usuarioLogado.getEmail(), editPassword.getText().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(CadastroLoginActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

    }
    public boolean validaCadastro(String nome, String email, String senha){
        boolean valido = true;
        if (nome.length()<0) {
            editNome.setError("Deve conter um nome!");
            valido = false;
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            editLogin.setError("Deve conter um email válido!");
            valido = false;
        }
        if (senha.length() < 6) {
            editPassword.setError("Deve conter uma senha de pelo menos 6 caracteres!");
            valido = false;
        }

        return valido;
    }

    public void vaiParaLogin(String email, String senha){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("EMAIL",email);
        returnIntent.putExtra("SENHA",senha);
        setResult(LoginActivity.RESULT_OK,returnIntent);
        finish();
    }

}
