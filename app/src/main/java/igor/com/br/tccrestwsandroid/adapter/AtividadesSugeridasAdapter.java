package igor.com.br.tccrestwsandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import igor.com.br.tccrestwsandroid.entity.Atividade;
import igor.com.br.tccrestwsandroid.R;
import igor.com.br.tccrestwsandroid.vo.AtividadeVo;

/**
 * Created by Igor on 16/03/2017.
 */

public class AtividadesSugeridasAdapter extends ArrayAdapter<AtividadeVo> {
    private List<AtividadeVo> atividades;
    private LayoutInflater layoutInflater;
    public AtividadesSugeridasAdapter(Context context, List<AtividadeVo> a) {
        super(context, 0, a);
        atividades = new ArrayList<>();
        if(a!=null) {
            atividades.addAll(a);
        }
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AtividadeVo vo = getItem(position);

        Atividade atividade = vo.getAtividade();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_atividades_sugeridas, parent, false);
        }

        // Lookup view for data population
        TextView nome = (TextView) convertView.findViewById(R.id.lbl_nome_atividade);
        // Populate the data into the template view using the data object
        nome.setText(atividade.getNome());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((AtividadeVo)(resultValue)).getAtividade().getNome();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                ArrayList<AtividadeVo> suggestions = new ArrayList<>();
                for (AtividadeVo atividadeVo : atividades) {
                    if(atividadeVo.getAtividade().getNome().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(atividadeVo);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            ArrayList<AtividadeVo> filteredList = (ArrayList<AtividadeVo>) results.values;
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<AtividadeVo>) results.values);
            } else {
                // no filter, add entire original list back in
//                addAll(atividades);
            }
            notifyDataSetChanged();
        }

    };


}
