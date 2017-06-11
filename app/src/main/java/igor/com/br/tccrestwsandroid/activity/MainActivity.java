package igor.com.br.tccrestwsandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import igor.com.br.tccrestwsandroid.R;

import igor.com.br.tccrestwsandroid.adapter.AtividadesSugeridasAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {


    private Retrofit retrofit;
    private Context mContext;

    private AtividadesSugeridasAdapter atividadesSugeridasAdapter;
    @BindView(R.id.listview_atividades_sugeridas)
    public ListView listViewAtividadesSugeridas;

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
//        this.setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mContext = this;
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        Usuario usuarioLogado = new Gson().fromJson(json,Usuario.class);
        configuraAdapter();
    }

    public void configuraAdapter(){
        List<Atividade> listaAtividadesSugeridas = new ArrayList<>();
        listaAtividadesSugeridas.add(new Atividade(0,"Futebol",null));
        atividadesSugeridasAdapter = new AtividadesSugeridasAdapter(mContext,listaAtividadesSugeridas);
        listViewAtividadesSugeridas.setAdapter(atividadesSugeridasAdapter);
    }

    protected void onBtnAtividadeClick(){
        Intent intent = new Intent(mContext,AtividadesActivity.class);
        startActivity(intent);
    }

    protected void onBtnPerfilClick(){
        Intent intent = new Intent(mContext, PerfilActivity.class);
        startActivity(intent);
    }


}
