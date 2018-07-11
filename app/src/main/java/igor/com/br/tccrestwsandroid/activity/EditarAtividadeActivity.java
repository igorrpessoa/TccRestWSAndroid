package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
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
import igor.com.br.tccrestwsandroid.adapter.AtividadesAdapter;
import igor.com.br.tccrestwsandroid.adapter.ComplementoAdapter;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.entity.UsuarioAtividade;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.interfaces.ComplementoInterface;
import igor.com.br.tccrestwsandroid.interfaces.UsuarioAtividadeInterface;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditarAtividadeActivity extends BaseActivity {

    private AtividadeVo atividadeSelecionada = null;
    private Retrofit retrofit;
    private Context mContext;
    private Usuario usuarioLogado;

    private AtividadesAdapter atividadesAdapter;
    private ComplementoAdapter complementoAdapter;
    @BindView(R.id.listview_complemento)
    public ListView listViewComplemento;

    @BindView(R.id.linear_frequencia2)
    public LinearLayout linearLayoutFrequencia;

    @BindView(R.id.auto_edit_atividade)
    public AutoCompleteTextView editAtividade;
    @BindView(R.id.auto_edit_complemento)
    public AutoCompleteTextView editComplemento;

    @BindView(R.id.seekBar_frequencia)
    public SeekBar seekBarFrequencia;
    @BindView(R.id.seekBar_satisfacao)
    public SeekBar seekBarSatisfacao;
    @BindView(R.id.lbl_numero_dias)
    public TextView lblNumeroDias;
    @BindView(R.id.radio_frequente_sim)
    public RadioButton radioSim;
    @BindView(R.id.radio_frequente_nao)
    public RadioButton radioNao;

    private List<AtividadeVo> listaAtividadesSugeridas = new ArrayList<>();
    private List<Complemento> listaComplementosSugeridos = new ArrayList<>();
    private List<Complemento> listaComplemento;
    private UsuarioAtividadeVo usuarioAtividadeVo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_atividade);
        ButterKnife.bind(this);
        mContext = this;
        retrofit = new RetrofitUtil().createRetrofit();
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        usuarioLogado = new Gson().fromJson(json,Usuario.class);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false,true);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("AtividadeSelecionada") != null) {
            json = bundle.getString("AtividadeSelecionada");
            atividadeSelecionada = new Gson().fromJson(json, AtividadeVo.class);
        }
        selectAtividadeSelecionada();
        listarAtividadesSugeridas();
        listarComplementosSugeridos();
        configuraAdapterComplemento();
        seekBarFrequencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblNumeroDias.setText(progress + "x por Mês");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void selectAtividadeSelecionada(){
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        UsuarioAtividadeVo vo = new UsuarioAtividadeVo();
        vo.setAtividade(atividadeSelecionada.getAtividade());
        vo.setComplementos(atividadeSelecionada.getComplementos());
        vo.setPerfil(atividadeSelecionada.getPerfil());
        Call<List<UsuarioAtividadeVo>> call = i.selectUsuarioAtividade(vo);
//        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<List<UsuarioAtividadeVo>>() {
            @Override
            public void onResponse(Call<List<UsuarioAtividadeVo>> call, Response<List<UsuarioAtividadeVo>> response) {
                if(response!= null) {
                    List<UsuarioAtividadeVo> list = response.body();
                    if(list != null && !list.isEmpty()){
                        usuarioAtividadeVo = list.get(0);
                    }
                }
                inicializa();
            }

            @Override
            public void onFailure(Call<List<UsuarioAtividadeVo>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditarAtividadeActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inicializa(){
        String respostas[] = usuarioAtividadeVo.getResposta().substring(1,usuarioAtividadeVo.getResposta().length()-1).split(", ");
        lblNumeroDias.setText(respostas[16]+"x por Mês");
        seekBarFrequencia.setProgress(Integer.parseInt(respostas[16]));
        if(respostas[15].equals("S")){
            radioSim.setChecked(true);
            radioNao.setChecked(false);
            linearLayoutFrequencia.setVisibility(LinearLayout.VISIBLE);
        }else{
            radioSim.setChecked(false);
            radioNao.setChecked(true);
            linearLayoutFrequencia.setVisibility(LinearLayout.GONE);
        }
        seekBarSatisfacao.setProgress(Integer.parseInt(respostas[14])/10);
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
                }

            }

            @Override
            public void onFailure(Call<List<AtividadeVo>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditarAtividadeActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
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
                dialog.dismiss();
                if(response!= null){
                    listaComplementosSugeridos = response.body();
                    configuraAdapterComplementosSugeridos(listaComplementosSugeridos);
                }
            }

            @Override
            public void onFailure(Call<List<Complemento>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditarAtividadeActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
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
    private ArrayAdapter<String> getComplementoAdapter(Context context, List<Complemento> c) {
        List<Complemento> complementos = c;
        List<String> suggestions = new ArrayList<>();
        for (Complemento complemento : complementos) {
            if(complemento.getNome() != null && !complemento.getNome().isEmpty() ){
                suggestions.add(complemento.getNome());
            }
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
    }
    public void configuraAdapterAtividadesSugeridas(final List<AtividadeVo> atividades){
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
        editAtividade.setText(atividadeSelecionada.getAtividade().getNome());
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
    public void configuraAdapterComplemento(){
        if(listaComplemento == null){
            listaComplemento = new ArrayList<>();
            listaComplemento.addAll(atividadeSelecionada.getComplementos());
        }
        complementoAdapter = new ComplementoAdapter(mContext, listaComplemento);
        listViewComplemento.setAdapter(complementoAdapter);
        complementoAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_salvar)
    public void btnSalvarAlteracoes(){
        UsuarioAtividadeInterface inter  = retrofit.create(UsuarioAtividadeInterface.class);
        usuarioAtividadeVo.setUsuario(usuarioLogado);
        usuarioAtividadeVo.setFrequencia(seekBarFrequencia.getProgress()*1.0);

        usuarioAtividadeVo.setSatisfacao(seekBarSatisfacao.getProgress()*10.0);
        if (editAtividade.getText().length() > 0) {
            Atividade atividade = new Atividade();
            atividade.setNome(editAtividade.getText().toString());
            usuarioAtividadeVo.setAtividade(atividade);
        }
        usuarioAtividadeVo.setComplementos(listaComplemento);
        String respostas[] = usuarioAtividadeVo.getResposta().substring(1,usuarioAtividadeVo.getResposta().length()-1).split(", ");

        respostas[14] = seekBarSatisfacao.getProgress()*10+"";
        respostas[15] = radioSim.isChecked() ? "S" : "N";
        respostas[16] = "" + seekBarFrequencia.getProgress();
        String resposta = "[";
        for(int i =0; i<respostas.length;i++) resposta+=respostas[i]+", ";
        resposta=resposta.substring(0,resposta.length()-2) + "]";
        usuarioAtividadeVo.setResposta(resposta);
        Call<UsuarioAtividadeVo> call = inter.editarUsuarioAtividade(usuarioAtividadeVo);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<UsuarioAtividadeVo>() {
            @Override
            public void onResponse(Call<UsuarioAtividadeVo> call, Response<UsuarioAtividadeVo> response) {
                dialog.dismiss();
                if(response!= null){
                    UsuarioAtividadeVo a = response.body();
                    usuarioLogado = a.getUsuario();
                    setSharedPreferences(mContext,mContext.getString(R.string.usuario_logado),new Gson().toJson(usuarioLogado));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UsuarioAtividadeVo> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditarAtividadeActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.radio_frequente_sim)
    public void radioSim(){
        radioSim.setChecked(true);
        radioNao.setChecked(false);
        linearLayoutFrequencia.setVisibility(LinearLayout.VISIBLE);

    }
    @OnClick(R.id.radio_frequente_nao)
    public void radioNao(){
        radioSim.setChecked(false);
        radioNao.setChecked(true);
        linearLayoutFrequencia.setVisibility(LinearLayout.GONE);
    }

    @OnClick(R.id.img_add)
    public void adicionarComplemento() {
        String complemento = editComplemento.getText().toString();
        if(!complemento.isEmpty() && !complemento.equals(" ")){
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
}
