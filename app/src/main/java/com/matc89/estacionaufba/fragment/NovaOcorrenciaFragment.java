package com.matc89.estacionaufba.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.activity.MainActivity;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.interfaces.IOcorrenciaSchema;
import com.matc89.estacionaufba.meta.Constants;
import com.matc89.estacionaufba.meta.EstacionaUFBAConfigurations;
import com.matc89.estacionaufba.meta.EstacionaUFBAFunctions;
import com.matc89.estacionaufba.meta.HandleLocationIntentService;

public class NovaOcorrenciaFragment extends Fragment implements IOcorrenciaSchema, View.OnClickListener {
    private static final int UPDATE_LOCAL_ADDRESS = 0;
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

        view.findViewById(R.id.button_adicionar_ocorrencia).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Botão de cadastrar nova promoção
            case R.id.button_adicionar_ocorrencia:
                //Capturando componentes
                EditText editTextTituloNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_titulo);
                EditText editTextPlacaCarroNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_placa_carro);
                EditText editTextModeloCarroNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_modelo_carro);
                EditText editTextDescricaoNovaOcorrencia = (EditText) mForm.findViewById(R.id.editText_ocorrencia_descricao);

                //Capturando valores dos componentes
                String tituloNovaOcorrencia = editTextTituloNovaOcorrencia.getText().toString().trim();
                String placaCarroNovaOcorrencia = editTextPlacaCarroNovaOcorrencia.getText().toString().trim();
                String modeloCarroNovaOcorrencia = editTextModeloCarroNovaOcorrencia.getText().toString().trim();
                String descricaoNovaOcorrencia = editTextDescricaoNovaOcorrencia.getText().toString().trim();
                String localNovaOcorrencia = mEditTextLocalOcorrencia.getText().toString().trim();

                double latitude = 0;
                double longitude = 0;
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }

                // TODO Validar campos (máximo de caracteres)
                //Validando campos vazios
                if (tituloNovaOcorrencia.length() == 0) {
                    editTextTituloNovaOcorrencia.requestFocus();
                    editTextTituloNovaOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                } else if (placaCarroNovaOcorrencia.length() == 0) {
                    editTextTituloNovaOcorrencia.requestFocus();
                    editTextTituloNovaOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                } else if (localNovaOcorrencia.length() == 0) {
                    mEditTextLocalOcorrencia.requestFocus();
                    mEditTextLocalOcorrencia.setError(mContext.getString(R.string.campo_obrigatorio));
                } else {
                    //Cadastrando ocorrência
                    mOcorrencia = new Ocorrencia(tituloNovaOcorrencia, descricaoNovaOcorrencia, placaCarroNovaOcorrencia,
                            modeloCarroNovaOcorrencia, localNovaOcorrencia, latitude, longitude, 1, mOcorrencia.getUserId(),
                            EstacionaUFBAFunctions.getCurrentDateTime(), null);
                    if (DatabaseHandler.ocorrenciaDAO.addOcorrencia(mOcorrencia)) {
                        //Avisando que o cadastro obteve sucesso
                        Toast.makeText(mContext, mContext.getString(R.string.criacao_ocorrencia_sucedida), Toast
                                .LENGTH_SHORT).show();
                        ((MainActivity) mContext).replaceFragmentTo(new ListaOcorrenciasFragment(), true);
                    } else {
                        //Avisando que o cadastro teve erro
                        Toast.makeText(mContext, mContext.getString(R.string.criacao_ocorrencia_malsucedida), Toast
                                .LENGTH_SHORT).show();
                    }
                }
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

