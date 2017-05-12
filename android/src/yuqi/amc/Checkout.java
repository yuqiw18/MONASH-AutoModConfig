package yuqi.amc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import yuqi.amc.JsonData.Center;
import yuqi.amc.JsonData.Part;
import yuqi.amc.MapDialogFragment.MapDialogInteractionListener;
import yuqi.amc.JsonDataAdapter.JsonDataType;
import yuqi.amc.JsonDataAdapter.JsonAdapterMode;

public class Checkout extends AppCompatActivity implements OnClickListener, MapDialogInteractionListener {

    private RelativeLayout layoutCheckoutBooking;
    private TextView labelCheckoutAddress;
    private TextView labelCheckoutPaymentMethod;
    private TextView labelCheckoutBookingTime;
    private ListView listCheckoutItem;
    private Button btnCancelBooking;
    private Button btnChangeAddress;
    private Button btnChangePayment;
    private SharedPreferences sharedPreferences;
    private ArrayList<Part> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setTitle(getString(R.string.title_checkout));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        layoutCheckoutBooking = (RelativeLayout) findViewById(R.id.layoutCheckoutBooking);

        labelCheckoutAddress = (TextView) findViewById(R.id.labelCheckoutAddress);
        labelCheckoutPaymentMethod = (TextView) findViewById(R.id.labelCheckoutPaymentMethod);
        labelCheckoutBookingTime = (TextView) findViewById(R.id.labelCheckoutBookingTime);

        listCheckoutItem = (ListView) findViewById(R.id.listCheckoutItems);

        btnChangeAddress = (Button) findViewById(R.id.btnCheckoutPickServiceCenter);
        btnChangePayment = (Button) findViewById(R.id.btnCheckoutChangePaymentMethod);
        btnCancelBooking = (Button) findViewById(R.id.btnCheckoutCancelBooking);

        btnChangeAddress.setOnClickListener(this);
        btnChangePayment.setOnClickListener(this);
        btnCancelBooking.setOnClickListener(this);

        layoutCheckoutBooking.setVisibility(View.GONE);

        loadDefaultAddress();

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){

            HashMap<String, Long> cart = (HashMap<String, Long>) incomingData.getSerializable("Cart");

            new FetchCart().execute(Cart.getQuery(cart));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCheckoutPickServiceCenter){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            View dialogView = getLayoutInflater().inflate(R.layout.dialog_service_center, null);
//
            btnChangeAddress.setEnabled(false);
            btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
//
//            builder.setView(dialogView);
//            AlertDialog dialog = builder.create();
//            dialog.show();
            MapDialogFragment mapDialogFragment = new MapDialogFragment();
            //mapDialogFragment.getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mapDialogFragment.show(getFragmentManager(), "ServiceCenterMap");

        }else if (id == R.id.btnCheckoutChangePaymentMethod){



        }else if (id == R.id.btnCheckoutCancelBooking){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title_confirmation));
            builder.setMessage(getString(R.string.dialog_checkout_cancel_booking));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.dialog_yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadDefaultAddress();
                            layoutCheckoutBooking.setEnabled(false);
                            layoutCheckoutBooking.setVisibility(View.GONE);
                        }
                    });
            builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private class FetchCart extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return RestClient.requestData("part", params);
        }
        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getBaseContext(), result, JsonDataType.PART, JsonAdapterMode.CHECKOUT );
            cartList = (ArrayList<Part>)((ArrayList<?>)jsonDataAdapter.getDataList());
            listCheckoutItem.setAdapter(jsonDataAdapter);
        }
    }

    @Override
    public void onClose() {
        btnChangeAddress.setEnabled(true);
        btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
    }

    @Override
    public void onCenterSelect(Center center, String date, Integer time) {
        labelCheckoutAddress.setText(center.getFormmattedAddress());

        labelCheckoutBookingTime.setText(getString(R.string.ui_checkout_service_status) + "\n" + Utility.formateDate(date) + "\n" + formatTime(time));
        layoutCheckoutBooking.setVisibility(View.VISIBLE);
    }

    private void loadDefaultAddress(){
        String address = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getString("state", "STATE")
                + " " + sharedPreferences.getInt("postcode", 0000) + "\n"
                + sharedPreferences.getString("country", "COUNTRY");
        labelCheckoutAddress.setText(address);
    }

    private static String formatTime(Integer time){
        switch (time){
            case 8:
                return "8:00 AM";
            case 11:
                return "11:00 AM";
            case 2:
                return "2:00 PM";
            default:
                return "0:00 AM";
        }
    }

}
