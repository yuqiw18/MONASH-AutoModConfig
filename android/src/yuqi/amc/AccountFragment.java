package yuqi.amc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yuqi.amc.JsonData.Payment;

public class AccountFragment extends Fragment implements OnClickListener {

    private TextView labelAccountAddress;
    private TextView labelAccountPaymentMethod;
    private Button btnAccountEditInfo;
    private Button btnAccountPaymentMethod;

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

        btnAccountPaymentMethod = (Button) view.findViewById(R.id.btnAccountAddPaymentMethod);
        btnAccountEditInfo = (Button) view.findViewById(R.id.btnAccountChangeAddress);

        labelAccountName.setText(sharedPreferences.getString("name", null));
        labelAccountEmail.setText(sharedPreferences.getString("email", null));

        String address = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getString("state", "STATE")
                + " " + sharedPreferences.getInt("postcode", 0000) + "\n"
                + sharedPreferences.getString("country", "COUNTRY");
        labelAccountAddress.setText(address);

        btnAccountEditInfo.setOnClickListener(this);
        btnAccountPaymentMethod.setOnClickListener(this);


        new GetPaymentInfo().execute(String.valueOf(sharedPreferences.getLong("id", 0)));

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnAccountChangeAddress){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_forget_password, null);

            EditText editAddress = (EditText)dialogView.findViewById(R.id.textEditAddress);
            EditText editSuburb = (EditText)dialogView.findViewById(R.id.textEditSuburb);
            EditText editPostcode = (EditText)dialogView.findViewById(R.id.textEditPostcode);
            EditText editState = (EditText)dialogView.findViewById(R.id.textEditState);
            Spinner spinnerCountry = (Spinner)dialogView.findViewById(R.id.spinnerEditCountry);



            editAddress.setText(sharedPreferences.getString("address", null));
            editSuburb.setText(sharedPreferences.getString("suburb", null));
            editPostcode.setText(sharedPreferences.getInt("postcode", 0));
            editState.setText(sharedPreferences.getString("state", null));


            ArrayAdapter<String> countryDataAdapter;
            String[] countryList = getResources().getStringArray(R.array.country_values);
            List<String> data = Arrays.asList(countryList);

            countryDataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
            countryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCountry.setAdapter(countryDataAdapter);

            String valueToSet = sharedPreferences.getString("country", null);
            if (!valueToSet.equals(null)){
                int spinnerPosition = countryDataAdapter.getPosition(valueToSet);
                spinnerCountry.setSelection(spinnerPosition);
            }

            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();

            alertDialog.show();



        }else if (id == R.id.btnAccountAddPaymentMethod){


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
                    btnAccountPaymentMethod.setText(getString(R.string.ui_btn_account_payment_edit));

                }else {
                    labelAccountPaymentMethod.setText(getString(R.string.ui_account_no_payment));
                    btnAccountPaymentMethod.setText(getString(R.string.ui_btn_account_payment_add));
                }
            }else {
                labelAccountPaymentMethod.setText(getString(R.string.ui_account_no_payment));
                btnAccountPaymentMethod.setText(getString(R.string.ui_btn_account_payment_add));
            }
        }
    }


    private class ChangeAddress extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    private class ChangePassword extends AsyncTask<String,Void,String>{

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
