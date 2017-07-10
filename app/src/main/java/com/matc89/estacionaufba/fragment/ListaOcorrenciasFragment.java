package com.matc89.estacionaufba.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.interfaces.OnOcorrenciaInteractionListener;
import com.matc89.estacionaufba.meta.EstacionaUFBAListAdapter;

import java.util.List;

public class ListaOcorrenciasFragment extends Fragment implements IOcorrenciaSchema {

    private List<Ocorrencia> ocorrencias;
    private OnOcorrenciaInteractionListener ocorrenciaListener;

    public ListaOcorrenciasFragment() {
        // Required empty public constructor
    }

    public static ListaOcorrenciasFragment newInstance() {
        ListaOcorrenciasFragment fragment = new ListaOcorrenciasFragment();
        return fragment;
    }

    public static ListaOcorrenciasFragment newInstance(long userId) {
        ListaOcorrenciasFragment fragment = new ListaOcorrenciasFragment();
        Bundle args = new Bundle();
        args.putLong(COLUMN_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_display).setVisible(true);
        menu.findItem(R.id.action_toggle_display).setIcon(R.drawable.ic_action_display_map);
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_ocorrencias, container, false);

        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        databaseHandler.open();

        ocorrencias = databaseHandler.getOcorrenciaDAO().list(getArguments());
        ListView ocorrenciasList = (ListView) view.findViewById(R.id.lista_ocorrencias);
        EstacionaUFBAListAdapter adapter = new EstacionaUFBAListAdapter(ocorrencias, getActivity());
        ocorrenciasList.setAdapter(adapter);

        ocorrenciasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Ocorrencia ocorrencia = ocorrencias.get(position);
                if (ocorrenciaListener != null) {
                    ocorrenciaListener.mostrarOcorrencia(ocorrencia);
                }
            }
        });

        databaseHandler.close();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOcorrenciaInteractionListener) {
            ocorrenciaListener = (OnOcorrenciaInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnOcorrenciaInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ocorrenciaListener = null;
    }
}
