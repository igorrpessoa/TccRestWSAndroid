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
import igor.com.br.tccrestwsandroid.entity.Complemento;

/**
 * Created by Igor on 16/03/2017.
 */

public class ComplementoAdapter extends ArrayAdapter<Complemento> {
    public ComplementoAdapter(Context context, List<Complemento> users) {
            super(context, 0, users);
        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Complemento complemento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_complemento, parent, false);
        }
        // Lookup view for data population
        TextView text = (TextView) convertView.findViewById(R.id.lbl_complemento);

    // Populate the data into the template view using the data object
        text.setText(complemento.getNome());
        // Return the completed view to render on screen
        return convertView;
    }

}