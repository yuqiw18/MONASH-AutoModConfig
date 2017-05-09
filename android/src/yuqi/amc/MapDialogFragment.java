package yuqi.amc;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import yuqi.amc.JsonData.Servicing;
import yuqi.amc.JsonDataAdapter.JsonDataType;
import yuqi.amc.JsonDataAdapter.JsonAdapterMode;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000;  // 2 Seconds

    private MapDialogInteractionListener mapDialogInteractionListener;
    private GoogleMap googleMap;
    private Location location;
    private GoogleApiClient googleApiClient;
    private Marker marker;
    private double currentLat, currentLng;
    private ListView centerListView;
    private ArrayList<Servicing> serviceCenterList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.dialog_service_center, null);

        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        marker = null;
        MapView mapView = (MapView) dialogView.findViewById(R.id.map_fragment);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        centerListView = (ListView) dialogView.findViewById(R.id.listServiceCenters);

        centerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lat = serviceCenterList.get(position).getLatitude();
                double lng = serviceCenterList.get(position).getLongitude();
                CameraUpdate target = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12);
                googleMap.animateCamera(target);
            }
        });


        return dialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        catch (SecurityException secEx) {
            Toast.makeText(getContext(), getString(R.string.dialog_map_service_center_no_access), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    findMyLocation();
                    new ShowNearbyServiceCenter().execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                }
            }, 500);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String snippet = marker.getSnippet();
        Servicing selectedCenter = null;

        if (snippet!=null && !snippet.isEmpty()){
            for (Servicing s: serviceCenterList){
                if (s.getId() == getCode(snippet)){
                    selectedCenter = s;
                }
            }

            if (selectedCenter!=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_service_center_detail, null);

                TextView labelName = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailName);
                TextView labelAddress = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailAddress);
                TextView labelScore = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailScore);
                TextView labelDescription = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailDesc);
                Button btnPickDate = (Button) dialogView.findViewById(R.id.btnServiceCenterDetailDatepicker);
                Button btnPickTime = (Button) dialogView.findViewById(R.id.btnServiceCenterDetailTimepicker);
                Button btnConfirm = (Button) dialogView.findViewById(R.id.btnConfirmBooking);

                String name = selectedCenter.getName();
                String address = selectedCenter.getAddress();
                double avgScore = selectedCenter.getAvgScore();

                labelName.setText(name);
                labelAddress.setText(address);
                if (avgScore!=-1d){
                    labelScore.setText("Rating: " + avgScore);
                }else {
                    labelScore.setText("No Rating");
                }
                labelDescription.setText(selectedCenter.getDetail());

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }



        }

        return false;
    }


    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onDetach() {
        googleApiClient.disconnect();
        mapDialogInteractionListener.onDetected();
        super.onDetach();
    }

    public interface MapDialogInteractionListener{
        void onDetected();
        void onAddressSelect();
    }

    @Override
    public void onAttach(Context context) {
        mapDialogInteractionListener = (MapDialogInteractionListener) getActivity();
        super.onAttach(context);
    }

    class ShowNearbyServiceCenter extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return RestClient.requestData("service", params);
        }

        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getContext(), result, JsonDataType.SERVICING, JsonAdapterMode.NULL );
            serviceCenterList = (ArrayList<Servicing>)((ArrayList<?>)jsonDataAdapter.getDataList());
            centerListView.setAdapter(jsonDataAdapter);

            for (Servicing s: serviceCenterList) {
                double lat = s.getLatitude();
                double lng = s.getLongitude();
                String name = s.getName();
                long id = s.getId();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).snippet("Center Code #" + id ));
            }
        }
    }

    // Find my location on the map
    private void findMyLocation(){
        if (location != null && googleMap != null) {

            // Create a new LatLng based on our new location
            currentLat = location.getLatitude();
            currentLng = location.getLongitude();
            LatLng newLocation = new LatLng(currentLat, currentLng);

            // Remove the old marker
            if (marker != null) { marker.remove(); }

            // Place a marker to indicate my new location
            marker = googleMap.addMarker(new MarkerOptions().position(newLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12));
        }
    }

    // Extract center code from snippet
    private static long getCode(String snippet){
        return Long.valueOf(snippet.substring(snippet.indexOf("#") + 1));
    }

}

