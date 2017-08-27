package com.matc89.estacionaufba.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.activity.MainActivity;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.enums.JsonOperation;
import com.matc89.estacionaufba.enums.JsonType;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.meta.Constants;
import com.matc89.estacionaufba.meta.EstacionaUFBAFunctions;
import com.matc89.estacionaufba.meta.HandleLocationIntentService;
import com.matc89.estacionaufba.util.JsonAdapter;
import com.matc89.estacionaufba.util.LoadCarsTask;
import com.matc89.estacionaufba.util.Mask;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.matc89.estacionaufba.enums.JsonType.BRANDS;
import static com.matc89.estacionaufba.enums.JsonType.VEHICLES;

public class NovaOcorrenciaFragment extends Fragment implements IOcorrenciaSchema, View.OnClickListener {
    private static final int UPDATE_LOCAL_ADDRESS = 0;
    /*public static final String VEHICLES = "//fipeapi.appspot.com/api/1/carros/veiculos/%d.json";
    public static final String BRANDS = "http://fipeapi.appspot.com/api/1/carros/marcas.json";*/
    private Context mContext;
    private View mForm;
    private Ocorrencia mOcorrencia;
    private ResultReceiver mReceiver;
    private Handler mHandler;
    private Location mLocation;
    private EditText mEditTextLocalOcorrencia;

    public NovaOcorrenciaFragment() {
        // Required empty public constructor
    }

    public static NovaOcorrenciaFragment newInstance(long userId) {
        NovaOcorrenciaFragment fragment = new NovaOcorrenciaFragment();
        fragment.setArguments(Ocorrencia.toBundle(new Ocorrencia(userId)));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOcorrencia = Ocorrencia.parse(getArguments());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_LOCAL_ADDRESS) {
                    mEditTextLocalOcorrencia.setText((String) msg.obj);
                }
                super.handleMessage(msg);
            }
        };
        mReceiver = new LocationResultReceiver(mHandler);
        HandleLocationIntentService.startActionGetLastLocation(mContext, mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nova_ocorrencia, container, false);
        mForm = view.findViewById(R.id.form_container);
        mEditTextLocalOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_local);

        final Spinner montaSpinn = (Spinner) mForm.findViewById(R.id.spinner_ocorrencia_montadora_carro);
        final Spinner modelSpinn = (Spinner) mForm.findViewById(R.id.spinner_ocorrencia_modelo_carro);

        LoadCarsTask loadCarsTask = new LoadCarsTask(BRANDS.toString());
        loadCarsTask.execute();
        Map<Integer, String> list = null;
        try {
            list = loadCarsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JsonAdapter adapter = new JsonAdapter(this.getActivity(), list);

        final Activity thisAct = this.getActivity();

        montaSpinn.setAdapter(adapter);
        montaSpinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LoadCarsTask loadCarsTask = new LoadCarsTask(String.format(VEHICLES.toString(), getElementIdByPosition(position)));

                loadCarsTask.execute();

                JsonAdapter adapter = null;
                try {

                    Map<Integer, String> elements = loadCarsTask.get();

                    if(montaSpinn.getSelectedItem().equals("Select")){
                        modelSpinn.setEnabled(false);
                        modelSpinn.setSelection(0);
                        return;
                    }

                    adapter = new JsonAdapter(thisAct, elements);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                modelSpinn.setAdapter(adapter);
                modelSpinn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.button_adicionar_ocorrencia).setOnClickListener(this);
        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editTextPlacaCarroNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_placa_carro);
        editTextPlacaCarroNovaOcorrencia.addTextChangedListener(Mask.insert("aaa-0000", editTextPlacaCarroNovaOcorrencia));

        EditText editTextTituloNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_titulo);
        editTextTituloNovaOcorrencia.addTextChangedListener(Mask.insert(50, editTextTituloNovaOcorrencia));

        EditText editTextDescricaoNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_descricao);
        editTextDescricaoNovaOcorrencia.addTextChangedListener(Mask.insert(200, editTextDescricaoNovaOcorrencia));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Botão de cadastrar nova promoção
            case R.id.button_adicionar_ocorrencia:
                //Capturando componentes
                EditText editTextTituloNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_titulo);
                EditText editTextPlacaCarroNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_placa_carro);
                Spinner spinnerModeloCarroNovaOcorrencia = (Spinner) mForm.findViewById(R.id.spinner_ocorrencia_modelo_carro);
                EditText editTextDescricaoNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_descricao);

                //Capturando valores dos componentes
                String tituloNovaOcorrencia = editTextTituloNovaOcorrencia.getText().toString().trim();
                String placaCarroNovaOcorrencia = editTextPlacaCarroNovaOcorrencia.getText().toString().trim();
                String modeloCarroNovaOcorrencia = spinnerModeloCarroNovaOcorrencia.getSelectedItem().toString();
                String descricaoNovaOcorrencia = editTextDescricaoNovaOcorrencia.getText().toString().trim();
                String localNovaOcorrencia = mEditTextLocalOcorrencia.getText().toString().trim();

                double latitude = 0;
                double longitude = 0;
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }

                boolean error = false;

                //Validando campos vazios
                if (tituloNovaOcorrencia.length() == 0) {
                    editTextTituloNovaOcorrencia.requestFocus();
                    editTextTituloNovaOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                    error = true;
                }
                if (placaCarroNovaOcorrencia.length() == 0) {
                    editTextTituloNovaOcorrencia.requestFocus();
                    editTextTituloNovaOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                    error = true;
                } else if(placaCarroNovaOcorrencia.length() < 8){
                    editTextTituloNovaOcorrencia.requestFocus();
                    editTextTituloNovaOcorrencia.setError("Placa inválida");
                    error = true;
                }
                if (localNovaOcorrencia.length() == 0) {
                    mEditTextLocalOcorrencia.requestFocus();
                    mEditTextLocalOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                    error = true;
                }

                if(error){
                    break;
                }

                //Cadastrando ocorrência

                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                databaseHandler.open();

                mOcorrencia = new Ocorrencia(tituloNovaOcorrencia, descricaoNovaOcorrencia, placaCarroNovaOcorrencia,
                        modeloCarroNovaOcorrencia, localNovaOcorrencia, latitude, longitude, 1, mOcorrencia.getUserId(),
                        EstacionaUFBAFunctions.getCurrentDateTime(), null);
                if (databaseHandler.getOcorrenciaDAO().addOcorrencia(mOcorrencia)) {
                    //Avisando que o cadastro obteve sucesso
                    Toast.makeText(mContext, mContext.getString(R.string.criacao_ocorrencia_sucedida), Toast
                            .LENGTH_SHORT).show();
                    ((MainActivity) mContext).replaceFragmentTo(new ListaOcorrenciasFragment(), true);
                } else {
                    //Avisando que o cadastro teve erro
                    Toast.makeText(mContext, mContext.getString(R.string.criacao_ocorrencia_malsucedida), Toast
                            .LENGTH_SHORT).show();
                }
                databaseHandler.close();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    class LocationResultReceiver extends ResultReceiver {
        public LocationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            final String action = resultData.getString(Constants.ACTION_KEY);
            if (HandleLocationIntentService.ACTION_FETCH_ADDRESS.equals(action)) {

                String address = resultData.getString(Constants.RESULT_DATA_KEY);
                Message msg = mHandler.obtainMessage();
                msg.what = UPDATE_LOCAL_ADDRESS;
                msg.obj = address;
                mHandler.sendMessage(msg);

            } else if (HandleLocationIntentService.ACTION_GET_LAST_LOCATION.equals(action)) {
                Location location = resultData.getParcelable(Constants.LOCATION_DATA_EXTRA);
                mLocation = location;
                HandleLocationIntentService.startActionFetchAddress(mContext, location, mReceiver);
            }
        }
    }

}

