package yuqi.amc;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Checkout extends AppCompatActivity implements OnClickListener{

    private TextView labelCheckoutAddress;
    private TextView labelCheckoutPaymentMethod;
    private ListView listCheckoutItem;
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

        Button btnChangeAddress = (Button) findViewById(R.id.btnCheckoutPickServiceCenter);
        Button btnChangePayment = (Button) findViewById(R.id.btnCheckoutChangePaymentMethod);

        btnChangeAddress.setOnClickListener(this);
        btnChangePayment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCheckoutChangeAddress){







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
}
