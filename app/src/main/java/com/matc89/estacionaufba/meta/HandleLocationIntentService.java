package com.matc89.estacionaufba.meta;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.matc89.estacionaufba.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tedri on 03/07/2017.
 */

public class HandleLocationIntentService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String ACTION_GET_LAST_LOCATION = "com.matc89.estacionaufba.meta.action.GET_LAST_LOCATION";
    public static final String ACTION_FETCH_ADDRESS = "com.matc89.estacionaufba.meta.action.FETCH_ADDRESS";
    public static final String EXTRA_LOCATION = "com.matc89.estacionaufba.meta.extra.LOCATION";
    private static final String TAG = HandleLocationIntentService.class.getSimpleName();
    private AppCompatActivity mContext;
    private GoogleApiClient mGoogleApiClient;
    private ResultReceiver mReceiver;


    public HandleLocationIntentService() {
        super("HandleLocationIntentService");
    }

    public static void startActionFetchAddress(Context context, Location location, ResultReceiver receiver) {
        Intent intent = new Intent(context, HandleLocationIntentService.class);
        intent.setAction(ACTION_FETCH_ADDRESS);
        intent.putExtra(Constants.RECEIVER, receiver);
        intent.putExtra(EXTRA_LOCATION, location);
        context.startService(intent);
    }

    public static void startActionGetLastLocation(Context context, ResultReceiver receiver) {
        Intent intent = new Intent(context, HandleLocationIntentService.class);
        intent.setAction(ACTION_GET_LAST_LOCATION);
        intent.putExtra(Constants.RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            final String action = intent.getAction();
            if (ACTION_FETCH_ADDRESS.equals(action)) {
                final Location location = intent.getParcelableExtra(EXTRA_LOCATION);
                handleActionFetchAddress(location);
            } else if (ACTION_GET_LAST_LOCATION.equals(action)) {
                handleActionGetLastLocation();
            }
        }
    }

    private void handleActionFetchAddress(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverAddressResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverAddressResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line" +
                    ".separator"), addressFragments));
        }
    }


    private void handleActionGetLastLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // Blank for a moment...
        } else {
            deliverLocationResultToReceiver(Constants.SUCCESS_RESULT, location);
        }
        ;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void deliverAddressResultToReceiver(int resultCode, String address) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_KEY, ACTION_FETCH_ADDRESS);
        bundle.putString(Constants.RESULT_DATA_KEY, address);
        mReceiver.send(resultCode, bundle);
    }

    private void deliverLocationResultToReceiver(int resultCode, Location location) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_KEY, ACTION_GET_LAST_LOCATION);
        bundle.putParcelable(Constants.LOCATION_DATA_EXTRA, location);
        mReceiver.send(0, bundle);

    }

}

