package igor.com.br.tccrestwsandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.activity.AtividadesActivity;
import igor.com.br.tccrestwsandroid.entity.Atividade;

/**
 * Created by Igor on 16/03/2017.
 */

public class AtividadesAdapter extends ArrayAdapter<Atividade> {
        public AtividadesAdapter(Context context, List<Atividade> users) {
            super(context, 0, users);
        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Atividade atividade = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_atividades, parent, false);
        }
        // Lookup view for data population
        RadioButton radio = (RadioButton) convertView.findViewById(R.id.radio_atividade);
        radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AtividadesActivity activity = (AtividadesActivity)getContext();
                activity.setAtividadeSelecionada(position);
            }
        });
        // Populate the data into the template view using the data object
        radio.setText(atividade.getNome());
        // Return the completed view to render on screen
        return convertView;
    }

}
