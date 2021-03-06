package yuqi.amc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import yuqi.amc.JsonData.Customer;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView textRegName;
    private TextView textRegAddress;
    private TextView textRegSuburb;
    private TextView textRegPostcode;
    private TextView textRegState;
    private Spinner spinnerRegCountry;
    private TextView textRegEmail;
    private TextView textRegPassword;
    private TextView textRegPasswordConfirm;
    private Button btnRegister;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(getString(R.string.title_register));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        textRegName = (TextView)findViewById(R.id.textRegName);
        textRegAddress = (TextView)findViewById(R.id.textRegAddress);
        textRegSuburb = (TextView)findViewById(R.id.textRegSuburb);
        textRegPostcode = (TextView)findViewById(R.id.textRegPostcode);
        textRegState = (TextView)findViewById(R.id.textRegState);
        spinnerRegCountry = (Spinner)findViewById(R.id.spinnerRegCountry);
        textRegEmail = (TextView)findViewById(R.id.textRegEmail);
        textRegPassword = (TextView)findViewById(R.id.textRegPassword);
        textRegPasswordConfirm = (TextView)findViewById(R.id.textRegPasswordConfirm);
        btnRegister = (Button)findViewById(R.id.btnRegRegister);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Input validations
        if (v.getId() == R.id.btnRegRegister){
            String name = textRegName.getText().toString().trim();
            String address = textRegAddress.getText().toString().trim();
            String suburb = textRegSuburb.getText().toString().trim();
            String postcode = textRegPostcode.getText().toString().trim();
            String state = textRegState.getText().toString().trim();
            String country = spinnerRegCountry.getSelectedItem().toString();
            String email = textRegEmail.getText().toString().trim();
            String password = textRegPassword.getText().toString();
            String passwordConfirm = textRegPasswordConfirm.getText().toString();

            if (name.isEmpty()){
                promptMessage(getString(R.string.msg_reg_no_name));
                return;
            }

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

            if (email.isEmpty()){
                promptMessage(getString(R.string.msg_reg_no_email));
                return;
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                promptMessage(getString(R.string.msg_reg_invalid_email));
                return;
            }

            if (password.isEmpty()){
                promptMessage(getString(R.string.msg_reg_no_password));
                return;
            }else if (password.length() < 8 || password.length() > 24 ){
                promptMessage(getString(R.string.msg_reg_invalid_password));
                return;
            }

            if (passwordConfirm.isEmpty()){
                promptMessage(getString(R.string.msg_reg_no_password_confirm));
                return;
            }else if(!passwordConfirm.equals(password)){
                promptMessage(getString(R.string.msg_reg_unmatched_password));
                return;
            }

            // Once validated, create a customer object with all the infomation
            Customer customer = new Customer(0, name, password, email, address, suburb, Integer.valueOf(postcode), state, country);

            // Grey out the button to avoid duplicated registration
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

            // Pass the object to the method
            new RegisterAccount().execute(customer);

        }
    }

    // Async method for registering new users
    private class RegisterAccount extends AsyncTask<Object,Void,Integer>{
        Customer customer = null;
        @Override
        protected Integer doInBackground(Object... param) {
            customer = (Customer) param[0];
            return HttpManager.createData("customer",param[0]);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            int status = HttpManager.processResponseCode(responseCode);
            switch (status){
                case -1:
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    promptMessage(getString(R.string.msg_reg_server_fail));
                    break;
                case 0:
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                    promptMessage(getString(R.string.msg_reg_server_error));
                    break;
                case 1:
                    // Go to the Main Menu via Auto Login
                    Intent intent = new Intent(Register.this, Login.class);
                    // Put the new account login details
                    intent.putExtra("newEmail", customer.getEmail());
                    intent.putExtra("newPassword",customer.getPassword());
                    startActivity(intent);
                    break;
            }
        }
    }

    private void promptMessage(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}










