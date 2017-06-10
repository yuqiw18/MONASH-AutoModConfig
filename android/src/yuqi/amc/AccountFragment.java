package yuqi.amc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import yuqi.amc.JsonData.Customer;

public class AccountFragment extends Fragment implements OnClickListener {

    private TextView labelAccountAddress;
    private TextView labelAccountEmail;
    private Button btnAccountEditAddress;
    private Button btnAccountEditLogin;

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
        labelAccountEmail = (TextView) view.findViewById(R.id.labelAccountEmail);

        labelAccountAddress = (TextView) view.findViewById(R.id.labelAccountAddress);

        btnAccountEditAddress = (Button) view.findViewById(R.id.btnAccountChangeAddress);
        btnAccountEditLogin = (Button) view.findViewById(R.id.btnAccountChangeDetail);

        labelAccountName.setText(sharedPreferences.getString("name", null));
        labelAccountEmail.setText(sharedPreferences.getString("email", null));

        btnAccountEditAddress.setOnClickListener(this);
        btnAccountEditLogin.setOnClickListener(this);

        refreshUI();

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnAccountChangeAddress){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_address, null);

            final EditText editAddress = (EditText)dialogView.findViewById(R.id.textEditAddress);
            final EditText editSuburb = (EditText)dialogView.findViewById(R.id.textEditSuburb);
            final EditText editPostcode = (EditText)dialogView.findViewById(R.id.textEditPostcode);
            final EditText editState = (EditText)dialogView.findViewById(R.id.textEditState);
            final Spinner spinnerCountry = (Spinner)dialogView.findViewById(R.id.spinnerEditCountry);
            final Button btnEditApply = (Button)dialogView.findViewById(R.id.btnEditApply);

            editAddress.setText(sharedPreferences.getString("address", ""));
            editSuburb.setText(sharedPreferences.getString("suburb", ""));
            editPostcode.setText(String.valueOf(sharedPreferences.getInt("postcode", 00000000)));
            editState.setText(sharedPreferences.getString("state", ""));

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

            btnEditApply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = editAddress.getText().toString().trim();
                    String suburb = editSuburb.getText().toString().trim();
                    String postcode = editPostcode.getText().toString().trim();
                    String state = editState.getText().toString().trim();
                    String country = spinnerCountry.getSelectedItem().toString();

                    if (address.isEmpty()){
                        promptMessage(getString(R.string.msg_reg_no_address));
                        return;
                    }

                    if (suburb.isEmpty()){
                        promptMessage(getString(R.string.msg_reg_no_suburb));
                        return;
                    }

                    if (postcode.isEmpty()){
                        promptMessage(getString(R.string.msg_reg_no_postcode));
                        return;
                    }

                    if (state.isEmpty()){
                        promptMessage(getString(R.string.msg_reg_no_state));
                        return;
                    }

                    if (country.equals("PLEASE SELECT")){
                        promptMessage(getString(R.string.msg_reg_no_country));
                        return;
                    }

                    long id = sharedPreferences.getLong("id", 0);
                    String name = sharedPreferences.getString("name",null);
                    String email = sharedPreferences.getString("email",null);
                    String password = sharedPreferences.getString("password",null);

                    Customer customer = new Customer(id, name, password, email, address, suburb, Integer.valueOf(postcode), state, country);

                    btnEditApply.setEnabled(false);
                    btnEditApply.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

                    new UpdateInformation().execute(customer);

                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

        }else if (id == R.id.btnAccountChangeDetail){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_login_info, null);

            final EditText editEmail = (EditText)dialogView.findViewById(R.id.textEditEmail);
            final EditText editPassword = (EditText)dialogView.findViewById(R.id.textEditPassword);
            final EditText editConfirmPassword = (EditText)dialogView.findViewById(R.id.textEditConfirmPassword);
            final Button btnEditUpdate = (Button)dialogView.findViewById(R.id.btnEditUpdate);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();

            btnEditUpdate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editEmail.getText().toString().trim();
                    String password = editPassword.getText().toString();
                    String passwordConfirm = editConfirmPassword.getText().toString();

                    if (!email.isEmpty()) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            promptMessage(getString(R.string.msg_reg_invalid_email));
                            return;
                        }
                    }else {
                        email = sharedPreferences.getString("email",null);
                    }

                    if (!password.isEmpty()){
                        if (password.length() < 8 ){
                            promptMessage(getString(R.string.msg_reg_invalid_password));
                            return;
                        }

                        if(!passwordConfirm.equals(password)){
                            promptMessage(getString(R.string.msg_reg_unmatched_password));
                            return;
                        }

                    }else {
                        password = sharedPreferences.getString("password", null);
                    }

                    long id = sharedPreferences.getLong("id", 0);

                    Customer customer = new Customer(id,
                            sharedPreferences.getString("name",null),
                            password,
                            email,
                            sharedPreferences.getString("address", null),
                            sharedPreferences.getString("suburb", null),
                            sharedPreferences.getInt("postcode", 0000),
                            sharedPreferences.getString("state", null),
                            sharedPreferences.getString("country", null));

                    btnEditUpdate.setEnabled(false);
                    btnEditUpdate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

                    new UpdateInformation().execute(customer);

                    alertDialog.dismiss();

                }
            });

            alertDialog.show();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_account));
    }

    private class UpdateInformation extends AsyncTask<Object,Void,Integer>{

        Customer customer = null;
        @Override
        protected Integer doInBackground(Object... param) {
            customer = (Customer) param[0];
            return HttpManager.updateData("customer",param[0]);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            int status = HttpManager.processResponseCode(responseCode);
            switch (status){
                case -1:
                    promptMessage(getString(R.string.msg_reg_server_fail));
                    break;
                case 0:
                    promptMessage(getString(R.string.msg_reg_server_error));
                    break;
                case 1:
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("id",customer.getId());
                    editor.putString("name",customer.getName());
                    editor.putString("email",customer.getEmail());
                    editor.putString("password",customer.getPassword());
                    editor.putString("address",customer.getAddress());
                    editor.putString("suburb",customer.getSuburb());
                    editor.putInt("postcode",customer.getPostcode());
                    editor.putString("state",customer.getState());
                    editor.putString("country",customer.getCountry());
                    editor.putBoolean("isSignedIn",true);
                    editor.commit();
                    refreshUI();
                    promptMessage(getString(R.string.msg_update_success));
                    break;
            }
        }
    }

    private void promptMessage(String message){
        Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void refreshUI(){
        String address = sharedPreferences.getString("address", "NO ADDRESS") + "\n"
                + sharedPreferences.getString("suburb", "SUBURB")+ " " + sharedPreferences.getString("state", "STATE")
                + " " + sharedPreferences.getInt("postcode", 0000) + "\n"
                + sharedPreferences.getString("country", "COUNTRY");
        labelAccountAddress.setText(address);
        labelAccountEmail.setText(sharedPreferences.getString("email", null));
    }
}
