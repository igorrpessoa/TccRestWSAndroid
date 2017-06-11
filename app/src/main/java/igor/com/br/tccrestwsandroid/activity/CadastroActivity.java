package igor.com.br.tccrestwsandroid.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.custom.RadarMarkerView;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import retrofit2.Retrofit;

/**
 * Created by Igor on 14/02/2017.
 */

public class CadastroActivity extends BaseActivity {

    private Context mContext;

    private Usuario usuarioLogado;
    private Atividade atividadeSelecionada;

    @BindView(R.id.btn_esquerda)
    public Button btnEsquerda;
    @BindView(R.id.btn_direita)
    public Button btnDireita;
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
    @BindView(R.id.lbl_paginas)
    public TextView lblPaginas;
    @BindView(R.id.lbl_numero_dias)
    public TextView lblNumeroDias;
    @BindView(R.id.lbl_numero_pessoas)
    public TextView lblNumeroPessoas;

    @BindView(R.id.seekBar_frequencia)
    public SeekBar seekBarFrequencia;
    @BindView(R.id.seekBar_satisfacao)
    public SeekBar seekBarSatisfacao;
    @BindView(R.id.seekBar_social)
    public SeekBar seekBarSocial;

    private List<String> perguntas;
    private int indexPergunta=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        mContext = this;

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

        perguntas = new ArrayList<>();
        preparaTela();
    }

    private void preparaTela(){
        btnEsquerda.setVisibility(Button.GONE);

        perguntas.add("Você fez uma atividade física?");
        perguntas.add("Você descansou após ou durante a atividade?");
        perguntas.add("Você relaxou mentamente exercendo a atividade?");
        perguntas.add("Onde você fez a atividade? Em um local fechado ou ao ar livre?");
        perguntas.add("Quantas pessoas participaram?");
        perguntas.add("Você aprendeu algo novo?");
        perguntas.add("Precisou pensar rapidamente?");
        perguntas.add("Teve que guardar ou guardou informações?");
        perguntas.add("Você usou de estratégia/planejamento?");
        perguntas.add("O que você sentiu? Se expressou de alguma maneira?");
        perguntas.add("Os seus sentidos foram estimulados durante a atividade?");
        perguntas.add("Criou algo novo pra você?");
        perguntas.add("Teve que usar da sua imaginação?");
        perguntas.add("Qual a sua satisfação fazendo a atividade?");
        perguntas.add("Quantas vezes por mês você a pratica?");

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
    }

    @OnClick(R.id.btn_direita)
    public void setBtnDireita(){
        indexPergunta++;
        if(btnEsquerda.getVisibility() == (Button.GONE)){
            btnEsquerda.setVisibility(Button.VISIBLE);
        }
        if(indexPergunta == 14){
            btnDireita.setVisibility(Button.GONE);
        }

        lblPaginas.setText((indexPergunta+1) + "/" + perguntas.size());
        atualizaPergunta();
    }

    @OnClick(R.id.btn_esquerda)
    public void setBtnEsquerda(){
        indexPergunta--;
        if(btnDireita.getVisibility() == (Button.GONE)){
            btnDireita.setVisibility(Button.VISIBLE);
        }
        if(indexPergunta == 0){
            btnEsquerda.setVisibility(Button.GONE);
        }
        lblPaginas.setText((indexPergunta+1) + "/" + perguntas.size());
        atualizaPergunta();
    }

    public void atualizaPergunta(){
        lblPergunta.setText(perguntas.get(indexPergunta));
        if(indexPergunta == 4) {
            linearRadio.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.VISIBLE);
        }else if(indexPergunta == 13){
            linearRadio.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.VISIBLE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }else if(indexPergunta == 14){
            linearRadio.setVisibility(LinearLayout.GONE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.VISIBLE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }else{
            linearRadio.setVisibility(LinearLayout.VISIBLE);
            linearSatisfacao.setVisibility(LinearLayout.GONE);
            linearFrequencia.setVisibility(LinearLayout.GONE);
            linearSocial.setVisibility(LinearLayout.GONE);
        }
    }
}
