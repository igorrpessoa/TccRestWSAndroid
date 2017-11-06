package igor.com.br.tccrestwsandroid.activity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.custom.RadarMarkerView;
import igor.com.br.tccrestwsandroid.entity.Usuario;
import retrofit2.Retrofit;

/**
 * Created by Igor on 14/02/2017.
 */

public class PerfilActivity extends BaseActivity {

    @BindView(R.id.lbl_nome)
    TextView lblNome;
    private Context mContext;
    private RadarChart mChart;
    private Retrofit retrofit;

    private Usuario usuarioLogado;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.minha_toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnAtividade = (Button) toolbar.findViewById(R.id.btn_atividades);
        btnAtividade.setVisibility(Button.GONE);
        Button btnPerfil = (Button) toolbar.findViewById(R.id.btn_perfil);
        btnPerfil.setVisibility(Button.GONE);
        TextView btnSair = (TextView) toolbar.findViewById(R.id.btn_logout);
        btnSair.setVisibility(Button.GONE);

        mContext = this;
        String json = getSharedPreferences(mContext,mContext.getString(R.string.usuario_logado));
        usuarioLogado = new Gson().fromJson(json,Usuario.class);
        if(usuarioLogado != null){
            lblNome.setText(usuarioLogado.getNome());
        }
        configuraRadarChart();
    }

    private void configuraRadarChart(){
        mChart = (RadarChart) findViewById(R.id.radar_chart);
        mChart.setBackgroundColor(Color.WHITE);

        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
            mChart.setWebColor(Color.BLUE);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.BLUE);
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(80f);
//        xAxis.setYOffset(0f);
//        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Artistico", "Intelecto", "Social", "Saude"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);
        xAxis.setGranularity(1f);

        YAxis yAxis = mChart.getYAxis();
//        yAxis.setTypeface(mTfLight);
//        yAxis.setLabelCount(4, false);
//        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
//        yAxis.setDrawLabels(false);
        yAxis.setGranularity(10f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTypeface(mTfLight);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);


        setData();
    }

    public void setData() {

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();

        if(usuarioLogado.getPerfil() != null) {
            entries1.add(new RadarEntry(usuarioLogado.getPerfil().getArtistico().floatValue()));
            entries1.add(new RadarEntry(usuarioLogado.getPerfil().getIntelecto().floatValue()));
            entries1.add(new RadarEntry(usuarioLogado.getPerfil().getSocial().floatValue()));
            entries1.add(new RadarEntry(usuarioLogado.getPerfil().getSaude().floatValue()));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Perfil");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        Typeface myType = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        RadarData data = new RadarData(sets);
        data.setValueTypeface(myType);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);
        mChart.invalidate();
    }

}
