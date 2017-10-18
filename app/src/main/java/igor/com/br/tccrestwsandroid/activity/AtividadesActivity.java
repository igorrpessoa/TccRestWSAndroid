package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.adapter.AtividadesSugeridasAdapter;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.adapter.AtividadesAdapter;
import igor.com.br.tccrestwsandroid.adapter.ComplementoAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioAtividadeInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AtividadesActivity extends BaseActivity {

    private AtividadesAdapter atividadesAdapter;
    private ComplementoAdapter complementoAdapter;
    private AtividadesSugeridasAdapter atividadesSugeridasAdapter;

    @BindView(R.id.listview_atividades)
    public ListView listViewAtividades;
    @BindView(R.id.listview_complemento)
    public ListView listViewComplemento;
    @BindView(R.id.btn_criar_atividade)
    public Button btn_criar_atividade;
    @BindView(R.id.img_add)
    public ImageView btn_add;

    @BindView(R.id.auto_edit_atividade)
    public AutoCompleteTextView editAtividade;
    @BindView(R.id.edit_complemento)
    public EditText editComplemento;

    private List<AtividadeVo> listaAtividades = new ArrayList<>();
    private List<AtividadeVo> listaAtividadesSugeridas = new ArrayList<>();
    private List<Complemento> listaComplemento = new ArrayList<>();

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
        listarAtividadesExecutadas();
    }

    public void listarAtividadesExecutadas(){
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        Call<List<AtividadeVo>> call = i.selectAllUsuarioAtividade(usuarioLogado);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<AtividadeVo>>() {
            @Override
            public void onResponse(Call<List<AtividadeVo>> call, Response<List<AtividadeVo>> response) {
                dialog.dismiss();
                if(response!= null){
                    listaAtividades = response.body();
                    configuraAdapterUsuarioAtividade();
                    listarAtividadesSugeridas();
                }

            }

            @Override
            public void onFailure(Call<List<AtividadeVo>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void configuraAdapterUsuarioAtividade(){
        if(listaAtividades != null) {
            atividadesAdapter = new AtividadesAdapter(mContext, listaAtividades);
            listViewAtividades.setAdapter(atividadesAdapter);
            listViewAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editAtividade.setText(listaAtividades.get(position).getAtividade().getNome());
                    List<Complemento> complementos = listaAtividades.get(position).getComplementos();
                    listaComplemento.clear();
                    listaComplemento.addAll(complementos);
                    complementoAdapter.notifyDataSetChanged();
                    atividadesAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    public void configuraAdapterComplemento(List<Complemento> complementos){
        complementoAdapter = new ComplementoAdapter(mContext, complementos);
        listViewComplemento.setAdapter(complementoAdapter);
        complementoAdapter.notifyDataSetChanged();
    }

    public void listarAtividadesSugeridas(){
        AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
        AtividadeVo filtro = new AtividadeVo();
        filtro.setValido(1);
        Call<List<AtividadeVo>> call = i.selecionaAtividades(filtro);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<AtividadeVo>>() {
            @Override
            public void onResponse(Call<List<AtividadeVo>> call, Response<List<AtividadeVo>> response) {
                dialog.dismiss();
                if(response!= null){
                    listaAtividadesSugeridas = response.body();
                    configuraAdapterAtividadesSugeridas(listaAtividadesSugeridas);
                }

            }

            @Override
            public void onFailure(Call<List<AtividadeVo>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void configuraAdapterAtividadesSugeridas(List<AtividadeVo> atividades){
        atividadesSugeridasAdapter = new AtividadesSugeridasAdapter(mContext, atividades);
        editAtividade.setAdapter(atividadesSugeridasAdapter);
    }

    @OnClick(R.id.btn_criar_atividade)
    public void criarAtividade(){
        if(atividadeSelecionada == null){
            if (editAtividade.getText().length() > 0) {
                atividadeSelecionada = new Atividade();
                atividadeSelecionada.setNome(editAtividade.getText().toString());
            }
        }
        if(atividadeSelecionada != null){

            AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
            final AtividadeVo vo = new AtividadeVo();
            vo.setAtividade(atividadeSelecionada);
            vo.setComplementos(listaComplemento);
            vo.setValido(1);
            Call<AtividadeVo> call = i.buscaAtividade(vo);
            dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
            call.enqueue(new Callback<AtividadeVo>() {
                @Override
                public void onResponse(Call<AtividadeVo> call, Response<AtividadeVo> response) {
                    dialog.dismiss();
                    if(response!= null){
                        AtividadeVo atividade = response.body();
                        String atividadesString = "";
                        if(atividade.getAtividade().getValido() == null || atividade.getAtividade().getValido().equals(0)){
                            atividadesString = atividade.getAtividade().getNome();
                            atividadesString  += "/";
                        }
                        for(Complemento c : atividade.getComplementos()){
                            if(c.getValido() == null || c.getValido().equals(0)){
                                atividadesString += c.getNome();
                                atividadesString  += "/";
                            }
                        }
                        if(atividadesString.isEmpty()) {
                            vaiParaCadastro();
                        }else{
                            abreAlertaAtividade(atividadesString);
                        }
                    }
                }
                @Override
                public void onFailure(Call<AtividadeVo> call, Throwable t) {
                    Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    private void abreAlertaAtividade(String atividadesString){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle("Atividade Escolhida não encontrada");
        builder1.setMessage("As atividades/complementos não foram encontrados. Gostaria de utiliza-las mesmo assim? \n Dica: não utilize preposições/plurais que possam divergir o significado genérico das palavras." + "\n" +
        atividadesString);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Continuar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vaiParaCadastro();
                    }
                });

        builder1.setNegativeButton(
                "Voltar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alerta = builder1.create();
        alerta.show();
    }

    public void vaiParaCadastro(){
        Intent intent = new Intent(mContext,CadastroActivity.class);
        intent.putExtra("AtividadeSelecionada",new Gson().toJson(atividadeSelecionada));
        intent.putExtra("ComplementoSelecionado",new Gson().toJson(listaComplemento));
        startActivity(intent);
    }

    public void setAtividadeSelecionada(int id) {
        this.atividadeSelecionada = listaAtividades.get(id).getAtividade();
    }


    @OnClick(R.id.img_add)
    public void adicionarComplemento() {
        String complemento = editComplemento.getText().toString();
        listaComplemento.add(new Complemento(complemento));
        configuraAdapterComplemento(listaComplemento);
        editComplemento.setText("");
    }
}
