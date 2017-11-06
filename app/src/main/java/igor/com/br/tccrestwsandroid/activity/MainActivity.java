package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.R;

import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.adapter.AtividadesSugeridasAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioAtividadeInterface;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {


    private Retrofit retrofit;
    private Context mContext;
    private Usuario usuarioLogado;
    @BindView(R.id.btn_sugestao)
    public Button btnSugestao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.minha_toolbar);
        Button btnAtividade = (Button) toolbar.findViewById(R.id.btn_atividades);
        btnAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnAtividadeClick();
            }
        });
        Button btnPerfil = (Button) toolbar.findViewById(R.id.btn_perfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnPerfilClick();
            }
        });
        TextView btnSair = (TextView) toolbar.findViewById(R.id.btn_logout);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnLogoffClick();
            }
        });
        mContext = this;
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        usuarioLogado = new Gson().fromJson(json,Usuario.class);
        retrofit = new RetrofitUtil().createRetrofit();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sugestao)
    public void onBtnSugestao(){
        AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
        Call<AtividadeVo> call = i.sugestaoAtividade(usuarioLogado);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<AtividadeVo>() {
            @Override
            public void onResponse(Call<AtividadeVo> call, Response<AtividadeVo> response) {
                dialog.dismiss();
                if(response!= null){
                    AtividadeVo atividadeSugerida = response.body();

                }

            }

            @Override
            public void onFailure(Call<AtividadeVo> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onBtnAtividadeClick(){
        Intent intent = new Intent(mContext,AtividadesActivity.class);
        startActivity(intent);
    }

    protected void onBtnPerfilClick(){
        Intent intent = new Intent(mContext, PerfilActivity.class);
        startActivity(intent);
    }

    protected void onBtnLogoffClick(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        setSharedPreferences(mContext,mContext.getString(R.string.usuario_logado),null);
        startActivity(intent);
        finish();
    }
}
