package com.matc89.estacionaufba.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.OnOcorrenciaInteractionListener;
import com.matc89.estacionaufba.util.JsonModelAdapter;
import com.matc89.estacionaufba.util.LoadCarsTask;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import static com.matc89.estacionaufba.enums.JsonType.BRANDS;
import static com.matc89.estacionaufba.enums.JsonType.VEHICLES;


public class UpdateOcorrenciaFragment extends Fragment {

    private Ocorrencia mOcorrencia;
    private Context mContext;
    private Map<Integer, String> modelElements;
    private Map<Integer, String> brandList;
    private Boolean update;
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

        configSpinners(view);

        EditText ocorrenciaTitulo = (EditText) view.findViewById(R.id.editText_ocorrencia_titulo);
        EditText ocorrenciaPlacaCarro = (EditText) view.findViewById(R.id.editText_ocorrencia_placa_carro);
        final Spinner  ocorrenciaMarcaCarro = (Spinner) view.findViewById(R.id.spinner_ocorrencia_montadora_carro);
        final Spinner  ocorrenciaModeloCarro = (Spinner) view.findViewById(R.id.spinner_ocorrencia_modelo_carro);
        EditText ocorrenciaDescricao = (EditText) view.findViewById(R.id.editText_ocorrencia_descricao);
        EditText ocorrenciaLocal = (EditText) view.findViewById(R.id.editText_ocorrencia_local);

        ((Button) view.findViewById(R.id.button_update_ocorrencia)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                View view = (View) button.getParent();
                EditText ocorrenciaTitulo = (EditText) view.findViewById(R.id.editText_ocorrencia_titulo);
                EditText ocorrenciaPlacaCarro = (EditText) view.findViewById(R.id.editText_ocorrencia_placa_carro);
                Spinner ocorrenciaMarcaCarro = (Spinner) view.findViewById(R.id.spinner_ocorrencia_montadora_carro);
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
                } else if (ocorrenciaMarcaCarro.getSelectedItemPosition() != 0){
                    ocorrenciaMarcaCarro.requestFocus();
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
        ocorrenciaMarcaCarro.setSelection(getPositionOfItem(ocorrenciaMarcaCarro, mOcorrencia.getMarcaCarro()));
        ocorrenciaTitulo.setText(mOcorrencia.getTitulo());
        ocorrenciaLocal.setText(mOcorrencia.getLocal());
        ocorrenciaDescricao.setText(mOcorrencia.getDescricao());
        ocorrenciaPlacaCarro.setText(mOcorrencia.getPlacaCarro());

        return view;
    }

    Integer getPositionOfItem(Spinner spinner, String value){

        for(int i = 0; i < spinner.getCount(); i++){

            String adptItem = (String) spinner.getItemAtPosition(i);

            if(value.startsWith(adptItem) || value.equals(adptItem)){
                return i;
            }
        }

        return null;
    }

    private void configSpinners(View view){
        View mForm = view.findViewById(R.id.form_container);

        final Spinner montaSpinn = (Spinner) mForm.findViewById(R.id.spinner_ocorrencia_montadora_carro);
        final Spinner modelSpinn = (Spinner) mForm.findViewById(R.id.spinner_ocorrencia_modelo_carro);

        LoadCarsTask loadCarsTask = new LoadCarsTask(BRANDS.toString());
        loadCarsTask.execute();
        brandList = null;
        try {
            brandList = loadCarsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JsonModelAdapter brandAdapter = new JsonModelAdapter(this.getActivity(), brandList);

        final Activity thisAct = this.getActivity();

        montaSpinn.setAdapter(brandAdapter);
        montaSpinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final LoadCarsTask loadCarsTask = new LoadCarsTask(String.format(VEHICLES.toString(), getElementIdByPosition(position)), modelSpinn, mOcorrencia.getModeloCarro());

                loadCarsTask.execute();

                try {

                    modelElements = loadCarsTask.get();

                    if(montaSpinn.getSelectedItem().equals("Select")){
                        modelSpinn.setEnabled(false);
                        modelSpinn.setSelection(0);
                        return;
                    }

                    JsonModelAdapter modelAdapter = new JsonModelAdapter(thisAct, modelElements);
                    modelSpinn.setAdapter(modelAdapter);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                modelSpinn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Integer getElementIdByPosition(int pos){

        try {
            LoadCarsTask load = new LoadCarsTask(BRANDS.toString());
            load.execute();

            Map<Integer, String> mapList = load.get();

            int i = 0;

            for(Integer num : mapList.keySet()){
                if(i == pos){
                    return num;
                }
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
