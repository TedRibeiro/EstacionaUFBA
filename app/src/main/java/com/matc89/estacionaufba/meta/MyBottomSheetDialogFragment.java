package com.matc89.estacionaufba.meta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.vo.Ocorrencia;

/**
 * Created by tedri on 07/07/2017.
 */

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    Ocorrencia mOcorrencia;

    public static MyBottomSheetDialogFragment newInstance(Ocorrencia ocorrencia) {
        MyBottomSheetDialogFragment f = new MyBottomSheetDialogFragment();
        f.setArguments(Ocorrencia.toBundle(ocorrencia));
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOcorrencia = Ocorrencia.parse(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modal_bottom_sheet, container, false);
        TextView ocorrenciaTitulo = (TextView) v.findViewById(R.id.textView_list_adapter_titulo_ocorrencia);
        TextView ocorrenciaLocal = (TextView) v.findViewById(R.id.textView_list_adapter_local);
        TextView ocorrenciaDateCreated = (TextView) v.findViewById(R.id.textView_list_adapter_dateCreated);
        ocorrenciaTitulo.setText(mOcorrencia.getTitulo());
        ocorrenciaLocal.setText(mOcorrencia.getLocal());
        ocorrenciaDateCreated.setText(mOcorrencia.getFormattedDateCreated());

        v.findViewById(R.id.floatingActionButton).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Botão de cadastrar nova promoção
            case R.id.floatingActionButton:
                this.dismiss();
                break;
        }
    }
}