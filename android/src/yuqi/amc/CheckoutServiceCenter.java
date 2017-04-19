package yuqi.amc;

import android.content.Intent;
import android.location.Location;
//import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CheckoutServiceCenter extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, LocationListener {

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000; // 2 Seconds

    private static final LatLng LOCATION_CAULFIELD
            = new LatLng(-37.8770, 145.0443);
    private static final LatLng LOCATION_CLAYTON
            = new LatLng(-37.9150, 145.1300);
    private static final LatLng LOCATION_BERWICK
            = new LatLng(-38.0405, 145.3399);
    private static final LatLng LOCATION_PENINSULA
            = new LatLng(-38.1536, 145.1344);
    private static final LatLng LOCATION_PARKVILLE
            = new LatLng(-37.7838, 144.9587);
    private static final LatLng LOCATION_GIPPSLAND
            = new LatLng(-38.3112, 146.4294);

    private GoogleMap m_cGoogleMap;
    private Location m_cCurrentLocation;
    private GoogleApiClient m_cAPIClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_service_center);

        // Check to see if our APIClient is null.
        if(m_cAPIClient == null) {
            // Create our API Client and tell it to connect to Location Services
            m_cAPIClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        MapFragment mapFrag = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map_fragment);
        // Set up an asyncronous callback to let us know when the map has loaded
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Function is called once the map has fully loaded.
        // Set our map object to reference the loaded map
        m_cGoogleMap = googleMap;
        // Move the focus of the map to be on Caulfield Campus. 15 is for zoom
        m_cGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_CAULFIELD,15));
        // Call our Add Default Markers function
        // NOTE: In a proper application it may be better to load these from a DB
        AddDefaultMarkers();

        m_cGoogleMap.setOnMarkerClickListener(this);

    }

    private void AddDefaultMarkers() {
        // Create a series of markers for each campus with the title being the campus name
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_CAULFIELD).title("Monash Caulfield").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_CLAYTON).title("Monash Clayton"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_BERWICK).title("Monash Berwick"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_PENINSULA).title("Monash Peninsula"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_GIPPSLAND).title("Monash Gippsland"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_PARKVILLE).title("Monash Parkville").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Android 6.0 & up added security permissions
        // If the user rejects allowing access to location data then this try block
        // will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(m_cAPIClient, locationRequest, this);
        }
        catch (SecurityException secEx) {
            Toast.makeText(this, "ERROR: Please enable location services",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing for now. This function is called should the connection halt
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Do nothing for now. This function is called if the connection fails initially
    }



    private void ChangeMapLocation() {
        // Check to ensure map and location are not null
        if(m_cCurrentLocation != null && m_cGoogleMap != null) {
            // Create a new LatLng based on our new location
            LatLng newPos = new LatLng(m_cCurrentLocation.getLatitude(),
                    m_cCurrentLocation.getLongitude());
            // Change the map focus to be our new location
            m_cGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 15));
        }
    }

    @Override
    protected void onStart() {
        m_cAPIClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        m_cAPIClient.disconnect();
        super.onStop();
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.getTitle().toString().equals("Monash Caulfield"))
        {
//            Toast.makeText(this, "ERROR: Please enable location services",
//                    Toast.LENGTH_LONG).show();

            startActivity(new Intent(CheckoutServiceCenter.this, Previewer.class));

        }

        return true;
    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        // This is our function that is called whenever we change locations
        // Update our current location variable
        m_cCurrentLocation = location;
        ChangeMapLocation();
    }

    //MarkerDragListener
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
