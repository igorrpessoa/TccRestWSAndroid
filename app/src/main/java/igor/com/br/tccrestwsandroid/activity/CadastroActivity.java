package igor.com.br.tccrestwsandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import igor.com.br.tccrestwsandroid.AuxiliaryFuzzyfication;
import igor.com.br.tccrestwsandroid.Constantes;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.RetrofitUtil;
import igor.com.br.tccrestwsandroid.custom.RadarMarkerView;
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

/**
 * Created by Igor on 14/02/2017.
 */

public class CadastroActivity extends BaseActivity {

    private Context mContext;

    private Usuario usuarioLogado;
    private Atividade atividadeSelecionada = null;
    private Complemento complementoSelecionado = null;
    private Map<Integer,String> resposta = new HashMap();
    private Perfil perfil;
    private UsuarioAtividade usuarioAtividade;
    private Retrofit retrofit;

    @BindView(R.id.btn_esquerda)
    public Button btnEsquerda;
    @BindView(R.id.btn_direita)
    public Button btnDireita;
    @BindView(R.id.btn_finalizar)
    public Button btnFinalizar;
    @BindView(R.id.lbl_pergunta)
    public TextView lblPergunta;
    @BindView(R.id.lbl_satisfacao)
    public TextView lblSatisfacao;
    @BindView(R.id.linear_radio)
    public LinearLayout linearRadio;
    @BindView(R.id.linear_satisfacao)
    public LinearLayout linearSatisfacao;
    @BindView(R.id.linear_frequencia)
    public LinearLayout linearFrequencia;
    @BindView(R.id.linear_social)
    public LinearLayout linearSocial;
    @BindView(R.id.linear_fisica)
    public LinearLayout linearFisica;

    @BindView(R.id.lbl_paginas)
    public TextView lblPaginas;
    @BindView(R.id.lbl_numero_dias)
    public TextView lblNumeroDias;
    @BindView(R.id.lbl_numero_pessoas)
    public TextView lblNumeroPessoas;
    @BindView(R.id.lbl_fisica_minutos)
    public TextView lblFisicaMinutos;

    @BindView(R.id.seekBar_frequencia)
    public SeekBar seekBarFrequencia;
    @BindView(R.id.seekBar_satisfacao)
    public SeekBar seekBarSatisfacao;
    @BindView(R.id.seekBar_social)
    public SeekBar seekBarSocial;
    @BindView(R.id.seekBar_fisica)
    public SeekBar seekBarFisica;
    @BindView(R.id.radio_sim)
    public RadioButton radioSim;
    @BindView(R.id.radio_nao)
    public RadioButton radioNao;
    @BindView(R.id.radio_talvez)
    public RadioButton radioTalvez;
    @BindView(R.id.radio_natural)
    public RadioButton radioNatural;
    @BindView(R.id.radio_moderada)
    public RadioButton radioModerada;
    @BindView(R.id.radio_vigorosa)
    public RadioButton radioVigorosa;

    private Map<Integer,String> perguntas;
    private int indexPergunta=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        mContext = this;
        retrofit = new RetrofitUtil().createRetrofit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.minha_toolbar);
        Button btnAtividade = (Button) toolbar.findViewById(R.id.btn_atividades);
        btnAtividade.setVisibility(Button.GONE);
        Button btnPerfil = (Button) toolbar.findViewById(R.id.btn_perfil);
        btnPerfil.setVisibility(Button.GONE);

        Bundle bundle = getIntent().getExtras();

        String usuarioJson = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        usuarioLogado = new Gson().fromJson(usuarioJson,Usuario.class);

        if(bundle.getString("AtividadeSelecionada") != null) {
            String json = bundle.getString("AtividadeSelecionada");
            atividadeSelecionada = new Gson().fromJson(json, Atividade.class);
        }
        if(bundle.getString("ComplementoSelecionado") != null) {
            String json = bundle.getString("ComplementoSelecionado");
            complementoSelecionado = new Gson().fromJson(json, Complemento.class);
        }
        inicializa();
        usuarioAtividade = new UsuarioAtividade();
        perguntas = new HashMap<>();
        preparaTela();
    }

    public void inicializa(){
        perfil = new Perfil();
        perfil.setArtistico(0.0);
        perfil.setIntelecto(0.0);
        perfil.setSaude(0.0);
        perfil.setSocial(0.0);
        lblNumeroDias.setText("0x por Mês");
        lblNumeroPessoas.setText("0 Pessoas");
        lblSatisfacao.setText("0");
        lblFisicaMinutos.setText("0");
    }

    private void preparaTela(){
        btnEsquerda.setVisibility(Button.GONE);

        //SAUDE
        perguntas.put(Constantes.Perguntas.SAUDE1.getValor(),"Você fez uma atividade física?");
        perguntas.put(Constantes.Perguntas.SAUDE2.getValor(),"Quanto durou e qual foi a intensidade da atividade?");
        perguntas.put(Constantes.Perguntas.SAUDE3.getValor(),"Você descansou após ou durante a atividade?");
        perguntas.put(Constantes.Perguntas.SAUDE4.getValor(),"Você relaxou mentamente exercendo a atividade?");
        perguntas.put(Constantes.Perguntas.SAUDE5.getValor(),"Você fez a atividade na natureza ou em um lugar com bastante vegetação?");
        //SOCIAL
        perguntas.put(Constantes.Perguntas.SOCIAL.getValor(),"Quantas pessoas participaram?");
        //INTELECTO
        perguntas.put(Constantes.Perguntas.INTELECTO1.getValor(),"Você aprendeu algo novo?");
        perguntas.put(Constantes.Perguntas.INTELECTO2.getValor(),"Precisou pensar rapidamente?");
        perguntas.put(Constantes.Perguntas.INTELECTO3.getValor(),"Teve que guardar ou guardou informações?");
        perguntas.put(Constantes.Perguntas.INTELECTO4.getValor(),"Você usou de estratégia/planejamento?");
        //ARTISTICO
        perguntas.put(Constantes.Perguntas.ARTISTICO1.getValor(),"O que você sentiu? Se expressou de alguma maneira?");
        perguntas.put(Constantes.Perguntas.ARTISTICO2.getValor(),"Os seus sentidos foram estimulados durante a atividade?");
        perguntas.put(Constantes.Perguntas.ARTISTICO3.getValor(),"Criou algo novo pra você?");
        perguntas.put(Constantes.Perguntas.ARTISTICO4.getValor(),"Teve que usar da sua imaginação?");
        //SATISFACAO
        perguntas.put(Constantes.Perguntas.SATISFACAO.getValor(),"Qual a sua satisfação fazendo a atividade?");
        //FREQUENCIA
        perguntas.put(Constantes.Perguntas.FREQUENCIA.getValor(),"Quantas vezes por mês você a pratica?");

        atualizaPergunta();

        seekBarSatisfacao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblSatisfacao.setText(progress + "/100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarSocial.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblNumeroPessoas.setText(progress + " Pessoas");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        seekBarFisica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblFisicaMinutos.setText(progress + " minutos");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.btn_direita)
    public void setBtnDireita(){
        montarResposta();
        //em caso de não ser esporte, avança mais uma pergunta
        if(indexPergunta == Constantes.Perguntas.SAUDE1.getValor() && resposta.get(indexPergunta).equals("N")){
            indexPergunta++;
        }
        indexPergunta++;
        if(btnEsquerda.getVisibility() == (Button.GONE)){
            btnEsquerda.setVisibility(Button.VISIBLE);
        }
        if(indexPergunta == Constantes.Perguntas.FREQUENCIA.getValor()){
            btnDireita.setVisibility(Button.GONE);
            btnFinalizar.setVisibility(Button.VISIBLE);
        }else {
            lblPaginas.setText((indexPergunta + 1) + "/" + perguntas.size());
        }
        atualizaPergunta();
    }

    @OnClick(R.id.btn_esquerda)
    public void setBtnEsquerda(){
        indexPergunta--;
        //em caso de não ser esporte, retorna mais uma pergunta
        if(indexPergunta == Constantes.Perguntas.SAUDE1.getValor() && resposta.get(indexPergunta).equals("N")){
            indexPergunta--;
        }
        if(btnDireita.getVisibility() == (Button.GONE)){
            btnDireita.setVisibility(Button.VISIBLE);
            btnFinalizar.setVisibility(Button.GONE);
        }
        if(indexPergunta == Constantes.Perguntas.SAUDE1.getValor()){
            btnEsquerda.setVisibility(Button.GONE);
        }
        lblPaginas.setText((indexPergunta+1) + "/" + perguntas.size());
        atualizaPergunta();
    }

    @OnClick(R.id.btn_finalizar)
    public void setBtnFinalizar(){
        montarResposta();
        retrofit = new RetrofitUtil().createRetrofit();
        UsuarioAtividadeInterface i  = retrofit.create(UsuarioAtividadeInterface.class);
        usuarioAtividade.setUsuario(usuarioLogado);
        usuarioAtividade.setAtividade(atividadeSelecionada);
        AuxiliaryFuzzyfication fuzzy = new AuxiliaryFuzzyfication();
        fuzzy.setUsuarioAtividade(usuarioAtividade);
        List<Complemento> complementoList = new ArrayList<>();
        complementoList.add(complementoSelecionado);
        fuzzy.setComplementos(complementoList);
        Call<Perfil> call = i.fuzzyficar(fuzzy);
        dialog = ProgressDialog.show(this, "","Por favor aguarde...", false);
        call.enqueue(new Callback<Perfil>() {
            @Override
            public void onResponse(Call<Perfil> call, Response<Perfil> response) {
                dialog.dismiss();
                if(response!= null){

                }
            }

            @Override
            public void onFailure(Call<Perfil> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Não foi possível acessar o servidor. Verifique sua internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void atualizaPergunta(){
        lblPergunta.setText(perguntas.get(indexPergunta));
        if(indexPergunta == Constantes.Perguntas.SOCIAL.getValor()) {
            linearRadio.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearFisica.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.VISIBLE);
        }else if(indexPergunta == Constantes.Perguntas.SAUDE2.getValor()) {
            linearRadio.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.GONE);
            linearFisica.setVisibility(LinearLayout.VISIBLE);
        }else if(indexPergunta == Constantes.Perguntas.SATISFACAO.getValor()){
            linearRadio.setVisibility(LinearLayout.GONE);
            linearFisica.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.VISIBLE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }else if(indexPergunta == Constantes.Perguntas.FREQUENCIA.getValor()){
            linearRadio.setVisibility(LinearLayout.GONE);
            linearFisica.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.VISIBLE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }else{
            linearRadio.setVisibility(LinearLayout.VISIBLE);
            linearFisica.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }
    }

    public void montarResposta(){
        int progress = -1;
        //Social
       if(indexPergunta == Constantes.Perguntas.SOCIAL.getValor()){
            progress = seekBarSocial.getProgress();
            resposta.put(indexPergunta, progress + "");
            if(progress <= 3){
                perfil.setSocial(25.);
            }else if(progress <= 6){
                perfil.setSocial(50.);
            }else if(progress <= 9){
                perfil.setSocial(75.);
            }else{
                perfil.setSocial(100.);
            }
       }else if(indexPergunta == Constantes.Perguntas.SATISFACAO.getValor()){
            progress = seekBarSatisfacao.getProgress();
            resposta.put(indexPergunta,progress + "");
            usuarioAtividade.setSatisfacao(progress + 0.0);
       }else if(indexPergunta == Constantes.Perguntas.FREQUENCIA.getValor()){
            progress = seekBarFrequencia.getProgress();
            resposta.put(indexPergunta, progress + "");
            usuarioAtividade.setFrequencia(progress+0.0);
       }else if(indexPergunta == Constantes.Perguntas.SAUDE1.getValor()){
            resposta.put(indexPergunta,radioNao.isChecked() ? "N" : radioSim.isChecked()? "S" : "T");
       }else {
           double pontos=0;
           if(indexPergunta == Constantes.Perguntas.SAUDE2.getValor()){
               pontos = seekBarFisica.getProgress();
               if(radioModerada.isChecked()){
                   pontos = pontos/2;
               }else if(radioNatural.isChecked()){
                   pontos = pontos/3;
               }
               resposta.put(indexPergunta,radioModerada.isChecked() ? "Moderada" + pontos : radioNatural.isChecked()? "Natural" + pontos: "Vigorosa"+ pontos);
           }else if(indexPergunta > Constantes.Perguntas.SAUDE2.getValor()) {
               resposta.put(indexPergunta, radioNao.isChecked() ? "N" : radioSim.isChecked() ? "S" : "T");
               if (radioNao.isChecked()) {
                   pontos = 0;
               } else if (radioSim.isChecked()) {
                   pontos = 25;
               } else {
                   pontos = 12.5;
               }
           }
            if(indexPergunta <= Constantes.Perguntas.SAUDE5.getValor()){
                perfil.setSaude(perfil.getSaude() + pontos);
            }else if(indexPergunta <= Constantes.Perguntas.INTELECTO4.getValor()){
                perfil.setIntelecto(perfil.getIntelecto() + pontos);
            }else if(indexPergunta <= Constantes.Perguntas.ARTISTICO4.getValor()){
                perfil.setArtistico(perfil.getArtistico() + pontos);
            }
        }
    }

    @OnClick(R.id.radio_sim)
    public void radioSim(){
        radioNao.setChecked(false);
        radioTalvez.setChecked(false);
    }
    @OnClick(R.id.radio_nao)
    public void radioNao(){
        radioSim.setChecked(false);
        radioTalvez.setChecked(false);
    }
    @OnClick(R.id.radio_talvez)
    public void setRadioTalvez(){
        radioSim.setChecked(false);
        radioNao.setChecked(false);
    }

    @OnClick(R.id.radio_moderada)
    public void radioModerada(){
        radioNatural.setChecked(false);
        radioVigorosa.setChecked(false);
    }
    @OnClick(R.id.radio_natural)
    public void radioNatural(){
        radioVigorosa.setChecked(false);
        radioModerada.setChecked(false);
    }
    @OnClick(R.id.radio_vigorosa)
    public void setRadioVigorosa(){
        radioModerada.setChecked(false);
        radioNatural.setChecked(false);
    }
}
