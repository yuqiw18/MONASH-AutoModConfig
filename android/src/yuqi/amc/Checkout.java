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
import android.widget.Toast;

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
    private TextView labelCheckoutBookingTime;

    private TextView labelBtnPlaceOrder;

    private TextView labelCheckoutGrandTotal;
    private TextView labelCheckoutItemNum;
    private TextView labelCheckoutSubTotal;
    private TextView labelCheckoutPostageFee;
    private TextView labelCheckoutInstallationFee;

    private ListView listCheckoutItem;
    private Button btnCancelBooking;
    private Button btnChangeAddress;
    private SharedPreferences sharedPreferences;
    private ArrayList<Part> cartList;

    private double transactionPrice = 0;
    private double subTotal = 0;
    private double instFee = 0;
    private double postage = 0;
    private String transactionDetail = "";
    private String transactionAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setTitle(getString(R.string.title_checkout));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        layoutCheckoutBooking = (RelativeLayout) findViewById(R.id.layoutCheckoutBooking);

        labelCheckoutAddress = (TextView) findViewById(R.id.labelCheckoutAddress);
        labelCheckoutBookingTime = (TextView) findViewById(R.id.labelCheckoutBookingTime);
        labelCheckoutGrandTotal = (TextView) findViewById(R.id.labelCheckoutGrandTotal);

        labelCheckoutItemNum = (TextView) findViewById(R.id.labelCheckoutItemNum);
        labelCheckoutSubTotal = (TextView) findViewById(R.id.labelCheckoutSubTotal);
        labelCheckoutPostageFee = (TextView) findViewById(R.id.labelCheckoutPostageFee);
        labelCheckoutInstallationFee = (TextView) findViewById(R.id.labelCheckoutInstFee);

        listCheckoutItem = (ListView) findViewById(R.id.listCheckoutItems);

        labelBtnPlaceOrder = (TextView) findViewById(R.id.labelBtnPlaceOrder);

        btnChangeAddress = (Button) findViewById(R.id.btnCheckoutPickServiceCenter);
        btnCancelBooking = (Button) findViewById(R.id.btnCheckoutCancelBooking);

        btnChangeAddress.setOnClickListener(this);
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

            btnChangeAddress.setEnabled(false);
            btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

            MapDialogFragment mapDialogFragment = new MapDialogFragment();
            mapDialogFragment.show(getFragmentManager(), "ServiceCenterMap");

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
                            labelCheckoutInstallationFee.setText(getString(R.string.ui_checkout_inst_fee_not_apply));
                            instFee = 0;
                            calculateGrandTotal();
                        }
                    });
            builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else if (id == R.id.labelBtnPlaceOrder){
            Toast.makeText(getBaseContext(),"YES", Toast.LENGTH_SHORT).show();
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
            calculateCartValues();
        }
    }

    @Override
    public void onClose() {
        btnChangeAddress.setEnabled(true);
        btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
    }

    @Override
    public void onCenterSelect(Center center, String date, Integer time) {
        labelCheckoutAddress.setText(center.getFormattedAddress());
        labelCheckoutBookingTime.setText(getString(R.string.ui_checkout_service_status) + "\n" + Utility.formatDate(date) + "\n" + formatTime(time));
        instFee = center.getPrice();
        calculateGrandTotal();
        labelCheckoutInstallationFee.setText(Utility.getFormattedPrice(instFee));
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

    private void calculateCartValues(){
        for (Part p: cartList) {
            transactionPrice += p.getPrice();
            transactionDetail += p.toString() + "; ";
        }
        labelCheckoutItemNum.setText(getString(R.string.ui_checkout_item_count) + cartList.size());
        transactionAddress = labelCheckoutAddress.getText().toString();

        subTotal = transactionPrice;
        labelCheckoutSubTotal.setText(Utility.getFormattedPrice(subTotal));

        calculateGrandTotal();

        Log.e("Cart Price", String.valueOf(transactionPrice));
        Log.e("Cart Item", transactionDetail);
    }


    private void calculateGrandTotal(){
        transactionPrice = subTotal + postage + instFee;
        labelCheckoutGrandTotal.setText(getString(R.string.ui_check_grand_total) + Utility.getFormattedPrice(transactionPrice));
    }
}
