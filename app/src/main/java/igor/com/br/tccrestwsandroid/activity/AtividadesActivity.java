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
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
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

    @BindView(R.id.edit_atividade)
    public EditText editAtividade;
    @BindView(R.id.edit_complemento)
    public EditText editComplemento;

    private List<Atividade> listaAtividades = new ArrayList<>();

    private Atividade atividadeSelecionada = null;
    private Complemento complementoSelecionado = null;
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

    public void listarAtividades(){
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        Call<List<Atividade>> call = i.selectAllUsuarioAtividade(usuarioLogado);
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
            }
        });
    }

    public void configuraAdapter(List<Atividade> atividades){
        if(atividades != null) {
            for (Atividade a : atividades)
                listaAtividades.add(new Atividade(0, a.getNome()));

            atividadesAdapter = new AtividadesAdapter(mContext, listaAtividades);
            listViewAtividades.setAdapter(atividadesAdapter);
        }
    }

    @OnClick(R.id.btn_criar_atividade)
    public void clickBtnFuzzyficar(){
        if(atividadeSelecionada == null){
            if (editAtividade.getText().length() > 0) {
                atividadeSelecionada = new Atividade();
                atividadeSelecionada.setNome(editAtividade.getText().toString());
            }
            if (editComplemento.getText().length() > 0) {
                complementoSelecionado = new Complemento();
                complementoSelecionado.setNome(editComplemento.getText().toString());
            }
        }
        if(atividadeSelecionada != null){
            Intent intent = new Intent(mContext,CadastroActivity.class);
            intent.putExtra("AtividadeSelecionada",new Gson().toJson(atividadeSelecionada));
            intent.putExtra("ComplementoSelecionado",new Gson().toJson(complementoSelecionado));
            startActivity(intent);
        }
    }

    public void setAtividadeSelecionada(int id) {
        this.atividadeSelecionada = listaAtividades.get(id);
    }
}
