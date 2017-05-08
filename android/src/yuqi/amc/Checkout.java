package yuqi.amc;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Checkout extends AppCompatActivity {

    private TextView labelCheckoutAddress;
    private TextView labelCheckoutPaymentMethod;
    private ListView listCartItem;
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

        Button btnChangeAddress = (Button) findViewById(R.id.btnCheckoutChangeAddress);
        Button btnChangePayment = (Button) findViewById(R.id.btnCheckoutChangePaymentMethod);

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
}
