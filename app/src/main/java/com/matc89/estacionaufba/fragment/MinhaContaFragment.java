package com.matc89.estacionaufba.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.interfaces.IUserSchema;

public class MinhaContaFragment extends Fragment implements IUserSchema {
    public MinhaContaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);
        Bundle args = getArguments();

        String minhaContaNome = args.getString(COLUMN_NOME, "");
        String minhaContaEmail = args.getString(COLUMN_EMAIL, "");
        String minhaContaPlacaCarro = args.getString(COLUMN_PLACA_CARRO, "");

        EditText editTextMinhaContaNome = (EditText) view.findViewById(R.id.editText_minha_conta_nome);
        EditText editTextMinhaContaEmail = (EditText) view.findViewById(R.id.editText_minha_conta_email);
        EditText editTextMinhaContaPlacaCarro = (EditText) view.findViewById(R.id.editText_minha_conta_placa_carro);

        editTextMinhaContaNome.setText(minhaContaNome);
        editTextMinhaContaEmail.setText(minhaContaEmail);
        editTextMinhaContaPlacaCarro.setText(minhaContaPlacaCarro);
        return view;
    }
}
