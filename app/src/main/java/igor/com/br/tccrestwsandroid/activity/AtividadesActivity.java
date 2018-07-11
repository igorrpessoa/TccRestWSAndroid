package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.interfaces.ComplementoInterface;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.adapter.AtividadesAdapter;
import igor.com.br.tccrestwsandroid.adapter.ComplementoAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioAtividadeInterface;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AtividadesActivity extends BaseActivity {

    private AtividadesAdapter atividadesAdapter;
    private ComplementoAdapter complementoAdapter;

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
    @BindView(R.id.auto_edit_complemento)
    public AutoCompleteTextView editComplemento;

    private List<UsuarioAtividadeVo> listaAtividades = new ArrayList<>();
    private List<AtividadeVo> listaAtividadesSugeridas = new ArrayList<>();
    private List<Complemento> listaComplementosSugeridos = new ArrayList<>();
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
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false,true);
        listarAtividadesSugeridas();
        configuraAdapterComplemento();

        if(usuarioLogado.getPerfil() == null || usuarioLogado.getPerfil().getId() == null || usuarioLogado.getPerfil().getId() == 0) {
            mostraDialogTutorial();
        }
    }

    public void mostraDialogTutorial(){
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Cadastro de Atividades");
        //define a mensagem
        builder.setMessage(
                "Nesta tela as atividades podem ser cadastradas. \n" +
                        "Elas são compostas por dois principais parâmetros: Atividades e Complementos. \n" +
                        "Para cadastrá-la, você usará palavras chave que representem a atividade. \n" +
                        "O nome da atividade é muito importante! \n" +
                        "Foram cadastradas algumas atividades base no banco de dados, e a sua nomenclatura deve ser seguida para que o sistema possa entender o que foi feito. \n" +
                        "Evite palavras compostas como 'Jogar Futebol'. \n" +
                        "Simplifique e cadastre 'Futebol'. \n" +
                        "Adicione o 'Jogar' no complemento. \n" +
                        "Se jogou em um parque, ou jogou com seus amigos, adicione essas informações no complemento com 'parque' e 'amigos'. \n" +
                        "Evite nomes de pessoas ou lugares, apenas generalize. \n" +
                        "Os complementos não são obrigatórios! \n" +
                        "As suas atividades cadastradas podem ser sugeridas a outras pessoas =). \n");

        //define um botão como positivo
        builder.setPositiveButton("Entendi!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }


    public void listarAtividadesExecutadas(){
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        Call<List<UsuarioAtividadeVo>> call = i.selectAllUsuarioAtividade(usuarioLogado);
        call.enqueue(new Callback<List<UsuarioAtividadeVo>>() {
            @Override
            public void onResponse(Call<List<UsuarioAtividadeVo>> call, Response<List<UsuarioAtividadeVo>> response) {
                if(response!= null){
                    listaAtividades = response.body();
                    configuraAdapterUsuarioAtividade();
                }

            }

            @Override
            public void onFailure(Call<List<UsuarioAtividadeVo>> call, Throwable t) {
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

                }
            });
        }
        dialog.dismiss();
    }

    public void configuraAdapterComplemento(){
        if(listaComplemento == null){
            listaComplemento = new ArrayList<>();
        }
        complementoAdapter = new ComplementoAdapter(mContext, listaComplemento);
        listViewComplemento.setAdapter(complementoAdapter);
        complementoAdapter.notifyDataSetChanged();
    }

    public void listarAtividadesSugeridas(){
        AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
        AtividadeVo filtro = new AtividadeVo();
        filtro.setValido(1);
        Call<List<AtividadeVo>> call = i.selecionaAtividades(filtro);
//        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<AtividadeVo>>() {
            @Override
            public void onResponse(Call<List<AtividadeVo>> call, Response<List<AtividadeVo>> response) {
                if(response!= null){
                    listaAtividadesSugeridas = response.body();
                    configuraAdapterAtividadesSugeridas(listaAtividadesSugeridas);
                    listarComplementosSugeridos();
                }

            }

            @Override
            public void onFailure(Call<List<AtividadeVo>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void configuraAdapterAtividadesSugeridas(final List<AtividadeVo> atividades){
        final List<String> sugestoens = new ArrayList<>();
        editAtividade.setAdapter(getAtividadeAdapter(mContext,atividades));
        editAtividade.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                editAtividade.showDropDown();
                return false;
            }
        });
        editAtividade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    editAtividade.setText(((AppCompatTextView) view).getText());
                }
            }
        });
    }
    private ArrayAdapter<String> getAtividadeAdapter(Context context,List<AtividadeVo> a) {
        List<AtividadeVo> atividades = a;
        List<String> suggestions = new ArrayList<>();
        for (AtividadeVo atividade : atividades) {
            if(atividade.getAtividade().getNome() != null && !atividade.getAtividade().getNome().isEmpty() ){
                suggestions.add(atividade.getAtividade().getNome());
            }
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
    }
    public void listarComplementosSugeridos(){
        ComplementoInterface i  = retrofit.create(ComplementoInterface.class);
        Complemento filtro = new Complemento();
        filtro.setValido(1);
        Call<List<Complemento>> call = i.selectAllComplemento(filtro);
        //dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<Complemento>>() {
            @Override
            public void onResponse(Call<List<Complemento>> call, Response<List<Complemento>> response) {
                if(response!= null){
                    listaComplementosSugeridos = response.body();
                    configuraAdapterComplementosSugeridos(listaComplementosSugeridos);
                    listarAtividadesExecutadas();
                }
            }

            @Override
            public void onFailure(Call<List<Complemento>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void configuraAdapterComplementosSugeridos(final List<Complemento> complementos){
        editComplemento.setAdapter(getComplementoAdapter(mContext,complementos));
        editComplemento.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                editComplemento.showDropDown();
                return false;
            }
        });
        editComplemento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    editComplemento.setText(((AppCompatTextView) view).getText());
                }
            }
        });

    }


    private ArrayAdapter<String> getComplementoAdapter(Context context,List<Complemento> c) {
        List<Complemento> complementos = c;
        List<String> suggestions = new ArrayList<>();
        for (Complemento complemento : complementos) {
            if(complemento.getNome() != null && !complemento.getNome().isEmpty() ){
                suggestions.add(complemento.getNome());
            }
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
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
                            atividadesString += "#";
                            atividadesString += atividade.getAtividade().getNome();
                        }
                        if(atividade.getComplementos() != null) {
                            for (Complemento c : atividade.getComplementos()) {
                                if (c.getValido() == null || c.getValido().equals(0)) {
                                    atividadesString += "#";
                                    atividadesString += c.getNome();
                                    atividadesString += " ";
                                }
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

    public void setAtividadeSelecionada(int position) {
        this.atividadeSelecionada = listaAtividades.get(position).getAtividade();
        editAtividade.setText(listaAtividades.get(position).getAtividade().getNome());
        List<Complemento> complementos = listaAtividades.get(position).getComplementos();
        listaComplemento.clear();
        if(complementos != null) {
            for(Complemento c : complementos)
                if(c != null)
                    listaComplemento.add(c);
        }
        complementoAdapter.notifyDataSetChanged();
        atividadesAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.img_add)
    public void adicionarComplemento() {
        String complemento = editComplemento.getText().toString();
        if(!complemento.isEmpty() && !complemento.equals(" ")) {
            listaComplemento.add(new Complemento(complemento));
            configuraAdapterComplemento();
            editComplemento.setText("");
        }
    }

    public void removeComplemento(int position) {
        listaComplemento.remove(position);
        configuraAdapterComplemento();
        editComplemento.setText("");
    }

    public void confirmaDelete(final UsuarioAtividadeVo vo){
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirma Exclusão");
        //define a mensagem
        builder.setMessage("Tem certeza que gostaria de excluir essa atividade?\n");

        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                deleteAtividade(vo);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void deleteAtividade(final UsuarioAtividadeVo vo){
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        vo.setUsuario(usuarioLogado);
        Call<Usuario> call = i.deleteUsuarioAtividade(vo);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                dialog.dismiss();
                if(response!= null){
                    usuarioLogado = response.body();
                    setSharedPreferences(mContext,mContext.getString(R.string.usuario_logado),new Gson().toJson(usuarioLogado));

                    int removeIndex = -1;
                    for(UsuarioAtividadeVo ua :listaAtividades){
                        if(ua.getAtividade().getId().equals(vo.getAtividade().getId())){
                            removeIndex = listaAtividades.indexOf(ua);
                        }
                    }
                    listaAtividades.remove(removeIndex);
                    atividadesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(AtividadesActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}
