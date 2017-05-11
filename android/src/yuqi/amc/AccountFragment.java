package yuqi.amc;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import yuqi.amc.JsonData.Payment;

public class AccountFragment extends Fragment {

    private TextView labelAccountAddress;
    private TextView labelAccountPaymentMethod;
    private Button btnAccountEditInfo;
    private Button btnAccountPaymentMenthod;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView labelAccountName = (TextView) view.findViewById(R.id.labelAccountName);
        TextView labelAccountEmail = (TextView) view.findViewById(R.id.labelAccountEmail);

        labelAccountAddress = (TextView) view.findViewById(R.id.labelAccountAddress);
        labelAccountPaymentMethod = (TextView) view.findViewById(R.id.labelAccountPaymentMethod);

        btnAccountPaymentMenthod = (Button) view.findViewById(R.id.btnAccountAddPaymentMethod);
        btnAccountEditInfo = (Button) view.findViewById(R.id.btnAccountChangeAddress);

        labelAccountName.setText(sharedPreferences.getString("name", null));
        labelAccountEmail.setText(sharedPreferences.getString("email", null));

        String address = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getString("state", "STATE")
                + " " + sharedPreferences.getInt("postcode", 0000) + "\n"
                + sharedPreferences.getString("country", "COUNTRY");
        labelAccountAddress.setText(address);

        new GetPaymentInfo().execute(String.valueOf(sharedPreferences.getLong("id", 0)));

        return view;
    }

    private class FetchOrderList extends AsyncTask<String,Void,String>{

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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_account));
    }

    private class GetPaymentInfo extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return RestClient.requestData("payment/primary", params);
        }

        @Override
        protected void onPostExecute(String result) {

            Payment payment;

            if (result!=null && !result.isEmpty()){

                //Payment payment = Payment.strJsonToPayment(result);

                if ((payment = Payment.strJsonToPayment(result))!=null){


                    String paymentMethod = payment.getType() + "\n" +
                            payment.getInfo1() + "\n" + payment.getInfo2();

                    labelAccountPaymentMethod.setText(paymentMethod);

                }else {



                }


                btnAccountPaymentMenthod.setText(getString(R.string.ui_btn_account_payment_edit));
            }else {
                labelAccountPaymentMethod.setText(getString(R.string.ui_account_no_payment));
                btnAccountPaymentMenthod.setText(getString(R.string.ui_btn_account_payment_add));
            }
        }
    }

}
