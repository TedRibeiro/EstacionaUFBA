package com.matc89.estacionaufba.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.interfaces.OnOcorrenciaInteractionListener;
import com.matc89.estacionaufba.meta.EstacionaUFBAConfigurations;


public class OcorrenciaFragment extends Fragment implements IOcorrenciaSchema {

    private Ocorrencia mOcorrencia;
    private OnOcorrenciaInteractionListener ocorrenciaListener;

    public OcorrenciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ocorrencia instance of {@link Ocorrencia} class.
     * @return A new instance of fragment OcorrenciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OcorrenciaFragment newInstance(Ocorrencia ocorrencia) {
        OcorrenciaFragment fragment = new OcorrenciaFragment();
        fragment.setArguments(Ocorrencia.toBundle(ocorrencia));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mOcorrencia = Ocorrencia.parse(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ocorrencia, container, false);
        TextView ocorrenciaTitulo = (TextView) view.findViewById(R.id.ocorrencia_titulo);
        TextView ocorrenciaModeloCarro = (TextView) view.findViewById(R.id.ocorrencia_modelo_carro);
        TextView ocorrenciaPlacaCarro = (TextView) view.findViewById(R.id.ocorrencia_placa_carro);
        TextView ocorrenciaDescricao = (TextView) view.findViewById(R.id.ocorrencia_descricao);
        TextView ocorrenciaLocal = (TextView) view.findViewById(R.id.ocorrencia_local);
        TextView ocorrenciaDateCreated = (TextView) view.findViewById(R.id.ocorrencia_dateCreated);
        View buttons_container = (View) view.findViewById(R.id.buttons_container);
        ocorrenciaTitulo.setText(mOcorrencia.getTitulo());
        ocorrenciaModeloCarro.setText(mOcorrencia.getModeloCarro());
        ocorrenciaPlacaCarro.setText(mOcorrencia.getPlacaCarro());
        ocorrenciaDescricao.setText(mOcorrencia.getDescricao());
        ocorrenciaLocal.setText(mOcorrencia.getLocal());
        ocorrenciaDateCreated.setText(mOcorrencia.getFormattedDateCreated());

        long loggedUser = ((Activity) ocorrenciaListener).getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION, 0).getLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, 0);

        if (loggedUser != mOcorrencia.getUserId()) {
            buttons_container.setVisibility(View.GONE);
        } else {
            ((Button) view.findViewById(R.id.ocorrencia_btn_editar)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ocorrenciaListener != null) {
                        ocorrenciaListener.editarOcorrencia(mOcorrencia);
                    }
                }
            });
            ((Button) view.findViewById(R.id.ocorrencia_btn_deletar)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ocorrenciaListener != null) {
                        ocorrenciaListener.deletarOcorrencia(mOcorrencia);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOcorrenciaInteractionListener) {
            ocorrenciaListener = (OnOcorrenciaInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOcorrenciaInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ocorrenciaListener = null;
    }

}