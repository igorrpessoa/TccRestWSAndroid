package igor.com.br.tccrestwsandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import igor.com.br.tccrestwsandroid.activity.EditarAtividadeActivity;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.activity.AtividadesActivity;
import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.entity.Complemento;
import igor.com.br.tccrestwsandroid.vo.UsuarioAtividadeVo;

/**
 * Created by Igor on 16/03/2017.
 */

public class AtividadesAdapter extends ArrayAdapter<UsuarioAtividadeVo> {

    private List<UsuarioAtividadeVo> atividades;
    private int posicaoSelecionada = -1;
    private Context mContext;
    public AtividadesAdapter(Context context, List<UsuarioAtividadeVo> users) {
        super(context, 0, users);
        atividades = users;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    try{


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_atividades, parent, false);
        }
        // Get the data item for this position
        final UsuarioAtividadeVo usuarioAtividadeVo = getItem(position);
        final AtividadeVo atividadeVo = new AtividadeVo(usuarioAtividadeVo);
        Atividade atividade = usuarioAtividadeVo.getAtividade();


        // Check if an existing view is being reused, otherwise inflate the view

        // Lookup view for data population
        RadioButton radio = (RadioButton) convertView.findViewById(R.id.radio_atividade);
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtividadesActivity activity = (AtividadesActivity)mContext;
                activity.setAtividadeSelecionada(position);
                posicaoSelecionada =position;
                notifyDataSetChanged();
            }
        });
        ImageView imgEdit = (ImageView) convertView.findViewById(R.id.img_edit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditarAtividadeActivity.class);
                intent.putExtra("AtividadeSelecionada",new Gson().toJson(atividadeVo));
                mContext.startActivity(intent);
            }
        });

        ImageView imgDelete = (ImageView) convertView.findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtividadesActivity activity = (AtividadesActivity)mContext;
                activity.confirmaDelete(usuarioAtividadeVo);
            }
        });

        radio.setChecked(posicaoSelecionada == position);
        List<Complemento> complementos = atividadeVo.getComplementos();
        String atividadeString=atividade.getNome();
        if(complementos != null && complementos.size() > 0) {
            for (Complemento c : complementos) {
                if(c != null) {
                    atividadeString += " #"+c.getNome();
                }
            }

            // Populate the data into the template view using the data object
        }
        radio.setText(atividadeString);
    }
    catch (Exception e){
        e.printStackTrace();
    }
        // Return the completed view to render on screen
        return convertView;
    }

}
