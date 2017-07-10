package com.matc89.estacionaufba.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.meta.Constants;
import com.matc89.estacionaufba.meta.HandleLocationIntentService;
import com.matc89.estacionaufba.meta.MyBottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class MapaOcorrenciasFragment extends Fragment implements OnMapReadyCallback {

    private List<Ocorrencia> ocorrencias;
    private GoogleMap mMap;
    private ArrayList<LatLng> ocorrenciasPoints;
    private ArrayList<LatLng> markerPoints;
    private LatLng mCurrentLatLng;

    private ResultReceiver mReceiver;
    private Handler mHandler;
    private static final int UPDATE_ROUTE = 0;

    public MapaOcorrenciasFragment() {
        // Required empty public constructor
    }

    public static MapaOcorrenciasFragment newInstance() {
        MapaOcorrenciasFragment fragment = new MapaOcorrenciasFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        HandleLocationIntentService.startActionGetLastLocation(getContext(), new android.os.ResultReceiver(new android.os.Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Location location = resultData.getParcelable(Constants.LOCATION_DATA_EXTRA);
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, 12));
            }
        });
        markerPoints = new ArrayList<LatLng>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa_ocorrencias, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_display).setVisible(true);
        menu.findItem(R.id.action_toggle_display).setIcon(R.drawable.ic_action_display_list);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        databaseHandler.open();
        mMap = googleMap;
        ocorrencias = databaseHandler.getOcorrenciaDAO().list(getArguments());

        for (Ocorrencia ocorrencia : ocorrencias) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(ocorrencia.getLatitude(), ocorrencia.getLongitude()));
            marker.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getContext(), R.drawable.ic_map_icon)));
            marker.anchor((float) 0.2, (float) 0.2);
            Marker m = mMap.addMarker(marker);
            m.setTag(ocorrencia);
        }

        // Enable MyLocation Button in the Map
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest
                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("ERRO", "ATIVE A LOCALIZACAO");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        databaseHandler.close();
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Ocorrencia t1 = (Ocorrencia) marker.getTag();
                BottomSheetDialogFragment myBottomSheet = MyBottomSheetDialogFragment.newInstance(t1);

                myBottomSheet.show(getFragmentManager(), myBottomSheet.getTag());

                return true;
            }
        });
    }

    //TODO remover esse workaround quando corrigirem o BitmapDescriptor
    private static Bitmap getBitmap(Context context, @DrawableRes int vectorDrawableResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResId);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}