package com.matc89.estacionaufba.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.OnOcorrenciaInteractionListener;


public class UpdateOcorrenciaFragment extends Fragment {

    private Ocorrencia mOcorrencia;
    private Context mContext;
    protected OnOcorrenciaInteractionListener ocorrenciaListener;

    public UpdateOcorrenciaFragment() {
        // Required empty public constructor
    }

    public static UpdateOcorrenciaFragment newInstance(Ocorrencia sale) {
        UpdateOcorrenciaFragment fragment = new UpdateOcorrenciaFragment();
        fragment.setArguments(Ocorrencia.toBundle(sale));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOcorrencia = Ocorrencia.parse(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_ocorrencia, container, false);

        EditText ocorrenciaTitulo = (EditText) view.findViewById(R.id.editText_ocorrencia_titulo);
        EditText ocorrenciaPlacaCarro = (EditText) view.findViewById(R.id.editText_ocorrencia_placa_carro);
        Spinner  ocorrenciaModeloCarro = (Spinner) view.findViewById(R.id.spinner_ocorrencia_modelo_carro);
        EditText ocorrenciaDescricao = (EditText) view.findViewById(R.id.editText_ocorrencia_descricao);
        EditText ocorrenciaLocal = (EditText) view.findViewById(R.id.editText_ocorrencia_local);

        ((Button) view.findViewById(R.id.button_update_ocorrencia)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                View view = (View) button.getParent();
                EditText ocorrenciaTitulo = (EditText) view.findViewById(R.id.editText_ocorrencia_titulo);
                EditText ocorrenciaPlacaCarro = (EditText) view.findViewById(R.id.editText_ocorrencia_placa_carro);
                Spinner ocorrenciaModeloCarro = (Spinner) view.findViewById(R.id.spinner_ocorrencia_modelo_carro);
                EditText ocorrenciaDescricao = (EditText) view.findViewById(R.id.editText_ocorrencia_descricao);
                EditText ocorrenciaLocal = (EditText) view.findViewById(R.id.editText_ocorrencia_local);

                //Validando campos vazios
                if (ocorrenciaTitulo.getText().length() == 0) {
                    ocorrenciaTitulo.requestFocus();
                    ocorrenciaTitulo.setError(mContext.getString(R.string.required_field));
                } else if (ocorrenciaPlacaCarro.getText().length() == 0) {
                    ocorrenciaPlacaCarro.requestFocus();
                    ocorrenciaPlacaCarro.setError(mContext.getString(R.string.required_field));
                } else if (ocorrenciaModeloCarro.getSelectedItemPosition() != 0){
                    ocorrenciaModeloCarro.requestFocus();
                } else if (ocorrenciaDescricao.getText().length() == 0){
                    ocorrenciaDescricao.requestFocus();
                    ocorrenciaDescricao.setError(mContext.getString(R.string.required_field));
                } else if (ocorrenciaLocal.getText().length() == 0) {
                    ocorrenciaLocal.requestFocus();
                    ocorrenciaLocal.setError(mContext.getString(R.string.required_field));
                } else {
                    mOcorrencia.setTitulo(ocorrenciaTitulo.getText().toString().trim());
                    mOcorrencia.setDescricao(ocorrenciaDescricao.getText().toString().trim());
                    mOcorrencia.setPlacaCarro(ocorrenciaPlacaCarro.getText().toString().trim());
                    mOcorrencia.setLocal(ocorrenciaLocal.getText().toString().trim());

                    if (ocorrenciaListener != null) {
                        ocorrenciaListener.atualizarOcorrencia(mOcorrencia);
                    }
                }
            }
        });

        //Setando os valores dos componentes
        ocorrenciaTitulo.setText(mOcorrencia.getTitulo());
        ocorrenciaLocal.setText(mOcorrencia.getLocal());

        Spinner spinn = (Spinner) this.getActivity().
                findViewById(R.id.spinner_ocorrencia_modelo_carro);

        ocorrenciaModeloCarro.setSelection(getPositionOfItem(spinn, mOcorrencia.getModeloCarro()));
        ocorrenciaDescricao.setText(mOcorrencia.getDescricao());
        ocorrenciaPlacaCarro.setText(mOcorrencia.getPlacaCarro());

        return view;
    }

    Integer getPositionOfItem(Spinner spinner, String value){

        Adapter adpt = spinner.getAdapter();

        for(int i = 0; i < adpt.getCount(); i++){

            String adptItem = (String) adpt.getItem(i);

            if(value.equals(adptItem)){
                return i;
            }
        }

        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnOcorrenciaInteractionListener) {
            ocorrenciaListener = (OnOcorrenciaInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnOcorrenciaInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        ocorrenciaListener = null;
    }

}
