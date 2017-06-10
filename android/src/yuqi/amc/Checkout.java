package yuqi.amc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import yuqi.amc.JsonData.Center;
import yuqi.amc.JsonData.Order;
import yuqi.amc.JsonData.Part;
import yuqi.amc.MapDialogFragment.MapDialogInteractionListener;
import yuqi.amc.JsonDataAdapter.JsonDataType;

public class Checkout extends AppCompatActivity implements OnClickListener, MapDialogInteractionListener {

    private RelativeLayout layoutCheckoutBooking;
    private TextView labelCheckoutAddress;
    private TextView labelCheckoutBookingTime;

    private TextView labelCheckoutGrandTotal;
    private TextView labelCheckoutItemNum;
    private TextView labelCheckoutSubTotal;
    private TextView labelCheckoutPostageFee;
    private TextView labelCheckoutInstallationFee;

    private ListView listCheckoutItem;
    private Button btnCancelBooking;
    private Button btnChangeAddress;
    private Button btnPlaceOrder;

    private AlertDialog alertDialog;

    private SharedPreferences sharedPreferences;
    private ArrayList<Part> cartList;

    // Default values
    private double transactionPrice = 0;
    private double subTotal = 0;
    private double instFee = 0;
    private double postage = 0;
    private String transactionDetail = "";
    private String transactionAddress = "";
    private String billingAddress = "";
    private long centerId = 0;
    private String bookingDateTime = "1970-01-01 00:00:00";

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

        btnChangeAddress = (Button) findViewById(R.id.btnCheckoutPickServiceCenter);
        btnCancelBooking = (Button) findViewById(R.id.btnCheckoutCancelBooking);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);

        btnChangeAddress.setOnClickListener(this);
        btnCancelBooking.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);


        layoutCheckoutBooking.setVisibility(View.GONE);

        loadDefaultAddress();

        // Fetch the cart based on the query (because the item may out of stock so it needs to be real-time)
        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){

            HashMap<String, Long> cart = (HashMap<String, Long>) incomingData.getSerializable("Cart");

            new FetchCart().execute(Cart.getQuery(cart));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // If customer decides to install the product
        if (id == R.id.btnCheckoutPickServiceCenter){

            // Grey out the button for repeated operation because it takes time to load the map fragment
            btnChangeAddress.setEnabled(false);
            btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

            // Load the map fragment for further operations
            MapDialogFragment mapDialogFragment = new MapDialogFragment();
            mapDialogFragment.show(getFragmentManager(), "ServiceCenterMap");

        }else if (id == R.id.btnCheckoutCancelBooking){
            // If the customer would like to remove the booking, display a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title_confirmation));
            builder.setMessage(getString(R.string.dialog_checkout_cancel_booking));
            builder.setCancelable(false);

            // If yes, remove the booking and reset values like installing fee
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

            // If no, just close the dialog
            builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else if(id == R.id.btnPlaceOrder){

            // Prepare the information required for placing an order
            long customerId = sharedPreferences.getLong("id", 0);

            Order order = new Order(customerId, transactionPrice, transactionDetail, billingAddress, bookingDateTime, centerId);

            new PlaceOrder().execute(order);

            // Create a dialog displaying processing condition
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_transaction_processing, null);
            builder.setView(dialogView);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    // Method for fetching cart items
    private class FetchCart extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.requestData("part", params);
        }
        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getBaseContext(), result, JsonDataType.PART);
            cartList = (ArrayList<Part>)((ArrayList<?>)jsonDataAdapter.getDataList());
            listCheckoutItem.setAdapter(jsonDataAdapter);
            calculateCartValues();
        }
    }

    // Method for placing an order
    private class PlaceOrder extends AsyncTask<Object,Void,Integer>{
        Order order = null;
        @Override
        protected Integer doInBackground(Object... params) {
            order = (Order) params[0];
            return HttpManager.createData("transaction", order);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            int status = HttpManager.processResponseCode(responseCode);
            alertDialog.dismiss();
            switch (status){
                case -1:
                    Toast.makeText(getBaseContext(), getString(R.string.msg_reg_server_fail), Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getBaseContext(), getString(R.string.msg_reg_server_fail), Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Intent intent = new Intent(Checkout.this, OrderInstruction.class);
                    // Bring MainMenu to the top stack. By doing this, clicking back button will not bring user to the register screen any more.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onClose() {
        btnChangeAddress.setEnabled(true);
        btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
    }

    // Listener for map fragment. Catch the information sent from the fragment
    @Override
    public void onCenterSelect(Center center, String date, Integer time) {

        // Get values
        centerId = center.getId();
        transactionAddress = center.getFormattedAddress();
        instFee = center.getPrice();
        bookingDateTime = Utility.formatBookingDate(date) + " " + Utility.formatBookingTime(time);

        // Format for display
        labelCheckoutAddress.setText(transactionAddress);
        labelCheckoutBookingTime.setText(getString(R.string.ui_checkout_service_status) + "\n" + Utility.formatDate(date) + "\n" + formatTime(time));
        labelCheckoutInstallationFee.setText(Utility.getFormattedPrice(instFee));

        calculateGrandTotal();
        layoutCheckoutBooking.setVisibility(View.VISIBLE);

    }

    // Reset the address to default one. It is used when customer cancels the booking
    private void loadDefaultAddress(){
        transactionAddress = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getString("state", "STATE")
                + " " + sharedPreferences.getInt("postcode", 0000) + "\n"
                + sharedPreferences.getString("country", "COUNTRY");
        labelCheckoutAddress.setText(transactionAddress);
        billingAddress = transactionAddress;
        centerId = 0;
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

    // Calculate the total value of the cart
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
    }

    private void calculateGrandTotal(){
        transactionPrice = subTotal + postage + instFee;
        labelCheckoutGrandTotal.setText(getString(R.string.ui_check_grand_total) + Utility.getFormattedPrice(transactionPrice));
    }

}
