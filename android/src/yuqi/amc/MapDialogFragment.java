package yuqi.amc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog.OnDateSetListener;
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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Calendar;
import yuqi.amc.JsonData.Center;
import yuqi.amc.JsonDataAdapter.JsonDataType;

// Map dialog fragment for users to select the service center
public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, View.OnClickListener{

    // Map dialog listener
    public interface MapDialogInteractionListener{
        void onClose();
        void onCenterSelect(Center center, String date, Integer time);
    }

    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000;  // 2 Seconds

    private MapDialogInteractionListener mapDialogInteractionListener;
    private GoogleMap googleMap;
    private Location location;
    private GoogleApiClient googleApiClient;
    private Marker marker;

    private double currentLat, currentLng;

    private ListView centerListView;
    private ArrayList<Center> serviceCenterList;

    private static long MILLIS_PER_DAY = 86400000;
    private String bookingDate;
    private Integer bookingTime;

    private AlertDialog detailDialog;
    private Center selectedCenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set this fragment to the full screen
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.dialog_service_center_map, null);

        // Initial a new api client if it has not yet been created
        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        MapView mapView = (MapView) dialogView.findViewById(R.id.map_fragment);
        // Run the mapView
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        // Move the camera to the selected position on map when it is clicked in the list
        centerListView = (ListView) dialogView.findViewById(R.id.listServiceCenters);
        centerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lat = serviceCenterList.get(position).getLatitude();
                double lng = serviceCenterList.get(position).getLongitude();

                // Slowly move the camera to the target, better visual effect
                CameraUpdate target = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12);
                googleMap.animateCamera(target);
            }
        });

        // Reset the default user marker
        marker = null;

        return dialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        // When the map is ready to be used
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);

        // Locate the current location of the user and fetch
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


    // When the marker is selected
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Get the selected marker info (id)
        String snippet = marker.getSnippet();
        selectedCenter = null;

        // If the marker has an id (user marker has no id)
        // Get the center information by looking up the given id
        if (snippet!=null && !snippet.isEmpty()){
            for (Center s: serviceCenterList){
                if (s.getId() == Long.valueOf(snippet)){
                    selectedCenter = s;
                }
            }

            // If the center exists
            // Initialise the detail screen
            if (selectedCenter!=null){

                bookingDate = null;
                bookingTime = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_service_center_detail, null);

                TextView labelName = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailName);
                TextView labelAddress = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailAddress);
                TextView labelScore = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailScore);
                TextView labelDescription = (TextView) dialogView.findViewById(R.id.labelServiceCenterDetailDesc);
                ImageView imgServiceCenterDetail = (ImageView) dialogView.findViewById(R.id.imgServiceCenterDetail);

                // Load the image for the selected center
                Picasso.with(getActivity().getBaseContext()).load(Utility.getImageAddress("sc_" + selectedCenter.getId())).into(imgServiceCenterDetail);

                // Initialise the button for selecting servicing date and time
                final Button btnPickDate = (Button) dialogView.findViewById(R.id.btnServiceCenterDetailDatepicker);
                final Button btnPickTime = (Button) dialogView.findViewById(R.id.btnServiceCenterDetailTimepicker);
                Button btnConfirm = (Button) dialogView.findViewById(R.id.btnConfirmBooking);

                // Retrieve the information of selected center and display them on the UI
                String name = selectedCenter.getName();
                String address = selectedCenter.getAddress();
                double avgScore = selectedCenter.getAvgScore();

                labelName.setText(name);
                labelAddress.setText(address);

                // Calculate the rating
                if (avgScore!=-1d){
                    labelScore.setText("Rating: " + avgScore);
                }else {
                    labelScore.setText("No Rating");
                }
                labelDescription.setText(selectedCenter.getDetail());

                btnPickDate.setOnClickListener(this);
                btnPickTime.setOnClickListener(this);
                btnConfirm.setOnClickListener(this);

                // Display the detail dialog once everything is set up
                builder.setView(dialogView);
                detailDialog = builder.create();
                detailDialog.show();
            }
        }

        return true;
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onDetach() {
        googleApiClient.disconnect();
        mapDialogInteractionListener.onClose();
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        final View selectedView = v;
        int id = selectedView.getId();

        if (id == R.id.btnServiceCenterDetailDatepicker){

            // Use the current date reference
            Calendar calendar = Calendar.getInstance();
            long baseDate = System.currentTimeMillis();

            // The start date will be 7 days after today (due to the delivery time)
            long startDate = baseDate + MILLIS_PER_DAY * 7;

            // The end date will be 35 days after today
            // 35 - 7 = 28 so the servicing can be booked within approximately one month after the delivery
            long endDate = baseDate + MILLIS_PER_DAY * 35;

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    bookingDate = dayOfMonth + "-" +  (month+1) +"-" + year;
                    ((Button) selectedView).setText(bookingDate);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(startDate);
            datePickerDialog.getDatePicker().setMaxDate(endDate);

            // Diplay the dialog
            datePickerDialog.show();

        }else if (id == R.id.btnServiceCenterDetailTimepicker){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_time_picker, null);

            RadioButton rbtn8AM = (RadioButton) dialogView.findViewById(R.id.rbtn8AM);
            RadioButton rbtn11AM = (RadioButton) dialogView.findViewById(R.id.rbtn11AM);
            RadioButton rbtn2PM = (RadioButton) dialogView.findViewById(R.id.rbtn2PM);

            // Set the checked item
            if (bookingTime!=null){
                switch (bookingTime){
                    case 8:
                        rbtn8AM.setChecked(true);
                        break;
                    case 11:
                        rbtn11AM.setChecked(true);
                        break;
                    case 2:
                        rbtn2PM.setChecked(true);
                        break;
                }
            }

            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();

            // Check the item and display the formatted time
            rbtn8AM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        bookingTime = 8;
                        dialog.dismiss();
                        ((Button)selectedView).setText(getString(R.string.dialog_timepicker_8am));
                    }
                }
            });

            rbtn11AM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        bookingTime = 11;
                        dialog.dismiss();
                        ((Button)selectedView).setText(getString(R.string.dialog_timepicker_11am));
                    }
                }
            });

            rbtn2PM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        bookingTime = 2;
                        dialog.dismiss();
                        ((Button)selectedView).setText(getString(R.string.dialog_timepicker_2pm));
                    }
                }
            });

            dialog.show();

        }else if(id == R.id.btnConfirmBooking){

            // Validate whether users fill in all the required fields

            if (bookingDate==null){
                Toast.makeText(getContext(),getString(R.string.msg_booking_no_date),Toast.LENGTH_LONG).show();
                return;
            }

            if (bookingTime==null){
                Toast.makeText(getContext(),getString(R.string.msg_booking_no_time),Toast.LENGTH_LONG).show();
                return;
            }

            mapDialogInteractionListener.onCenterSelect(selectedCenter, bookingDate, bookingTime);
            detailDialog.dismiss();
            getDialog().dismiss();

        }

    }

    @Override
    public void onAttach(Context context) {
        mapDialogInteractionListener = (MapDialogInteractionListener) getActivity();
        super.onAttach(context);
    }

    // Fetch the nearby service centers by giving user's latitude and longitude
    class ShowNearbyServiceCenter extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.requestData("service", params);
        }

        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getContext(), result, JsonDataType.CENTER);
            serviceCenterList = (ArrayList<Center>)((ArrayList<?>)jsonDataAdapter.getDataList());
            centerListView.setAdapter(jsonDataAdapter);
            
            for (Center s: serviceCenterList) {
                double lat = s.getLatitude();
                double lng = s.getLongitude();
                long id = s.getId();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).snippet(String.valueOf(id)));
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

}

