package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.adapter.AtividadesAdapter;
import igor.com.br.tccrestwsandroid.adapter.AtividadesSugeridasAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioAtividadeInterface;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AtividadesActivity extends BaseActivity {

    private AtividadesAdapter atividadesAdapter;
    @BindView(R.id.listview_atividades)
    public ListView listViewAtividades;
    @BindView(R.id.btn_criar_atividade)
    public Button btn_criar_atividade;
    private List<Atividade> listaAtividades = new ArrayList<>();

    private Atividade atividadeSelecionada = null;
    private Retrofit retrofit;
    private Context mContext;
    private Usuario usuarioLogado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        ButterKnife.bind(this);
        mContext = this;
        retrofit = new RetrofitUtil().createRetrofit();
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        usuarioLogado = new Gson().fromJson(json,Usuario.class);
        btn_criar_atividade.setEnabled(true);
        listarAtividades();
    }



    public void configuraAdapter(List<Atividade> atividades){
        for(Atividade a : atividades)listaAtividades.add(new Atividade(0,a.getNome(),null));

        atividadesAdapter = new AtividadesAdapter(mContext,listaAtividades);
        listViewAtividades.setAdapter(atividadesAdapter);
    }

    public void setAtividadeSelecionada(int id){
        atividadeSelecionada = listaAtividades.get(id);
    }

    public void listarAtividades(){
        AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
        Call<List<Atividade>> call = i.selectAllAtividade(usuarioLogado);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<Atividade>>() {
            @Override
            public void onResponse(Call<List<Atividade>> call, Response<List<Atividade>> response) {
                dialog.dismiss();
                if(response!= null){
                    List<Atividade> atividades = response.body();
                    configuraAdapter(atividades);
                }

            }

            @Override
            public void onFailure(Call<List<Atividade>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();

                final List<Atividade> atividades = new ArrayList<Atividade>();
                atividades.add(new Atividade(0,"Esporte",null));
                configuraAdapter(atividades);
            }
        });
    }

    @OnClick(R.id.btn_criar_atividade)
    public void clickBtnFuzzyficar(){
        if(atividadeSelecionada != null){
            Intent intent = new Intent(mContext,CadastroActivity.class);
            intent.putExtra("AtividadeSelecionada",new Gson().toJson(atividadeSelecionada));
            startActivity(intent);

        }
    }
}
