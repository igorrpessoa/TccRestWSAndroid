package igor.com.br.tccrestwsandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.R;

/**
 * Created by Igor on 16/03/2017.
 */

public class AtividadesSugeridasAdapter extends ArrayAdapter<Atividade> {
        public AtividadesSugeridasAdapter(Context context, List<Atividade> users) {
            super(context, 0, users);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Atividade atividade = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_atividades_sugeridas, parent, false);
        }
        // Lookup view for data population
        TextView nome = (TextView) convertView.findViewById(R.id.lbl_nome_atividade);
        TextView descricao = (TextView) convertView.findViewById(R.id.lbl_descricao_atividade);
        // Populate the data into the template view using the data object
        nome.setText(atividade.getNome());
        // Return the completed view to render on screen
        return convertView;
    }

}
