package igor.com.br.tccrestwsandroid.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.R;

import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.entity.Perfil;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import igor.com.br.tccrestwsandroid.interfaces.AtividadeInterface;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;
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
    @BindView(R.id.layout_sugestao)
    public LinearLayout layoutSugestão;
    @BindView(R.id.layout_tutorial)
    public LinearLayout layoutTutorial;
    @BindView(R.id.lbl_tutorial)
    public TextView lblTutorial;
    @BindView(R.id.switch_customizado)
    public Switch switchCustomizado;
    @BindView(R.id.layout_customizado)
    public LinearLayout layoutCustomizado;

    @BindView(R.id.seekBar_saude)
    public SeekBar seekBarSaude;
    @BindView(R.id.seekBar_intelecto)
    public SeekBar seekBarIntelecto;
    @BindView(R.id.seekBar_artistico)
    public SeekBar seekBarArtistico;
    @BindView(R.id.seekBar_social)
    public SeekBar seekBarSocial;

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
        lblTutorial.setText("O aplicativo se trata do desenvolvimento de um TCC, que visa sugerir possíveis atividades ao usuário de acordo com as suas atividades executadas no dia a dia. \n" +
                "Para cadastrar sua atividade, acesse a tela de atividades pelo botão superior direito na barra de navegação. \n" +
                "A partir do momento em que cadastrar a sua primeira atividade, um perfil será gerado para você, podendo ser acessado pelo botão central na barra de navegação. \n" +
                "Quando seu perfil for gerado, será possível pedir a sugestão de uma atividade nessa mesma tela, apertando o botão 'Sugerir Atividade'.");
        if(usuarioLogado.getPerfil() != null && usuarioLogado.getPerfil().getId() != null &&  usuarioLogado.getPerfil().getId() != 0){
            layoutSugestão.setVisibility(View.VISIBLE);
            layoutTutorial.setVisibility(View.GONE);

            seekBarArtistico.setProgress(usuarioLogado.getPerfil().getArtistico().intValue()/10);
            seekBarSaude.setProgress(usuarioLogado.getPerfil().getSaude().intValue()/10);
            seekBarIntelecto.setProgress(usuarioLogado.getPerfil().getIntelecto().intValue()/10);
            seekBarSocial.setProgress(usuarioLogado.getPerfil().getSocial().intValue()/10);
        }else{
            layoutSugestão.setVisibility(View.GONE);
            layoutTutorial.setVisibility(View.VISIBLE);
        }
    }

    @OnCheckedChanged(R.id.switch_customizado)
    public void switchOnOff(){
        if(switchCustomizado.isChecked()){
            layoutCustomizado.setVisibility(View.VISIBLE);
        }else{
            layoutCustomizado.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_sugestao)
    public void onBtnSugestao(){
        Usuario usuarioSugestao = usuarioLogado;
        if(switchCustomizado.isChecked()){
            Perfil perfilAspirado = new Perfil();
            perfilAspirado.setArtistico(seekBarArtistico.getProgress()*10.0);
            perfilAspirado.setSocial(seekBarSocial.getProgress()*10.0);
            perfilAspirado.setSaude(seekBarSaude.getProgress()*10.0);
            perfilAspirado.setIntelecto(seekBarIntelecto.getProgress()*10.0);
            perfilAspirado.setId(usuarioLogado.getPerfil().getId());
            usuarioSugestao.setPerfil(perfilAspirado);
        }
        AtividadeInterface i  = retrofit.create(AtividadeInterface.class);
        Call<UsuarioAtividadeVo> call = i.sugestaoAtividade(usuarioSugestao);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<UsuarioAtividadeVo>() {
            @Override
            public void onResponse(Call<UsuarioAtividadeVo> call, Response<UsuarioAtividadeVo> response) {
                dialog.dismiss();
                if(response!= null){
                    UsuarioAtividadeVo atividadeSugerida = response.body();
                    showDialog(atividadeSugerida);
                }

            }

            @Override
            public void onFailure(Call<UsuarioAtividadeVo> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog(UsuarioAtividadeVo atividadeSugerida){

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_atividade_sugerida);
        dialog.setTitle("Atividade Sugerida");

        // set the custom dialog components - text, image and button
        TextView txtAtividadeSugerida = (TextView) dialog.findViewById(R.id.lbl_atividade_sugerida);
        if(atividadeSugerida == null){
            atividadeSugerida = new UsuarioAtividadeVo();
            txtAtividadeSugerida.setText("Não foi encontrado nenhuma atividade com seu perfil =/");
        }else{
            String atividadesString = "";
            Atividade atividade = atividadeSugerida.getAtividade();
            List<Complemento> complementos =atividadeSugerida.getComplementos();

            atividadesString += "#";
            atividadesString += atividade.getNome();

            if(complementos != null) {
                for (Complemento c : complementos) {
                    if (c.getNome() != null) {
                        atividadesString += "#";
                        atividadesString += c.getNome();
                        atividadesString += " ";
                    }
                }
            }

            txtAtividadeSugerida.setText(atividadesString);
        }
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
