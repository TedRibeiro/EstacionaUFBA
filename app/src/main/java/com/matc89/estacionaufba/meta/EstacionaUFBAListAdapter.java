package com.matc89.estacionaufba.meta;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.vo.Ocorrencia;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tedri on 02/07/2017.
 */

public class EstacionaUFBAListAdapter extends BaseAdapter {

    private final List<Ocorrencia> ocorrencias;
    private final Activity activity;

    public EstacionaUFBAListAdapter(List<Ocorrencia> ocorrencias, Activity activity) {
        this.ocorrencias = ocorrencias;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return ocorrencias.size();
    }

    @Override
    public Object getItem(int position) {
        return ocorrencias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.list_adapter, parent, false);

        Ocorrencia ocorrencia = ocorrencias.get(position);

        TextView tituloOcorrencia = (TextView) view.findViewById(R.id.textView_list_adapter_titulo_ocorrencia);
        TextView local = (TextView) view.findViewById(R.id.textView_list_adapter_local);
        TextView dateCreated = (TextView) view.findViewById(R.id.textView_list_adapter_dateCreated);

        tituloOcorrencia.setText(ocorrencia.getTitulo());
        local.setText(ocorrencia.getLocal());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(ocorrencia.getDateCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd/MM hh:mm");
        String date = format.format(newDate);

        dateCreated.setText(date);
        return view;
    }
}
