package yuqi.amc;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.view.View.OnClickListener;
import yuqi.amc.MapDialogFragment.OnMapDetachListener;

public class Checkout extends AppCompatActivity implements OnClickListener,OnMapDetachListener {

    private TextView labelCheckoutAddress;
    private TextView labelCheckoutPaymentMethod;
    private ListView listCheckoutItem;
    private Button btnChangeAddress;
    private Button btnChangePayment;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        labelCheckoutAddress = (TextView) findViewById(R.id.labelCheckoutAddress);

        String address = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getInt("postcode", 0000)
                + ", " + sharedPreferences.getString("state", "STATE") + "\n"
                + sharedPreferences.getString("country", "COUNTRY");

        labelCheckoutAddress.setText(address);

        listCheckoutItem = (ListView) findViewById(R.id.listCheckoutItems);

        btnChangeAddress = (Button) findViewById(R.id.btnCheckoutPickServiceCenter);
        btnChangePayment = (Button) findViewById(R.id.btnCheckoutChangePaymentMethod);

        btnChangeAddress.setOnClickListener(this);
        btnChangePayment.setOnClickListener(this);

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
            mapDialogFragment.show(getFragmentManager(), "ServiceCenterMap");

        }else if (id == R.id.btnCheckoutChangePaymentMethod){

        }
    }

    private class FetchCart extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onDetected() {
        //getFragmentManager().findFragmentByTag("ServiceCenterMap").
        btnChangeAddress.setEnabled(true);
        btnChangeAddress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
    }
}
