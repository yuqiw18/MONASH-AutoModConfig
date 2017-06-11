package yuqi.amc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import yuqi.amc.JsonData.Customer;

public class Login extends AppCompatActivity implements OnClickListener {

    private EditText textLoginEmail;
    private EditText textLoginPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("AMC Customer Login");
        textLoginEmail = (EditText) findViewById(R.id.inputLoginEmail);
        textLoginPassword = (EditText) findViewById(R.id.inputLoginPassword);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // This occurs for newly registered account
        Bundle incomingData = getIntent().getExtras();
        if (incomingData!=null) {
            // Set the entries and login automatically
            String email = incomingData.getString("newEmail");
            String password = incomingData.getString("newPassword");
            textLoginEmail.setText(email);
            textLoginPassword.setText(password);
            new ValidateLogin().execute(email, password);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            // Validate user inputs
            String email = textLoginEmail.getText().toString().trim();
            String password = textLoginPassword.getText().toString();
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
            }
            // Send info to server
            new ValidateLogin().execute(email,password);

        }else if (v.getId() == R.id.labelForgetPassword){
            // Display a dialog for users to enter their email address
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forget_password, null);
            final EditText requestEmail = (EditText)dialogView.findViewById(R.id.inputForgetPasswordEmail);
            Button btnSubmitRequest = (Button)dialogView.findViewById(R.id.btnForgetPasswordRequest);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            btnSubmitRequest.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Validate email address
                    String input = requestEmail.getText().toString().trim();
                    if (!input.isEmpty()){
                        // If it matches the pattern xx@xx.xx
                        if(!Patterns.EMAIL_ADDRESS.matcher(input).matches()){
                            Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_invalid), Toast.LENGTH_LONG).show();
                        }else {
                            // Send request to server
                            new RequestPassword().execute(input);
                            alertDialog.dismiss();
                        }
                    }else {
                        Toast.makeText(getBaseContext(), getString(R.string.msg_reg_no_email), Toast.LENGTH_LONG).show();
                    }
                }
            });
            alertDialog.show();
        }else if (v.getId() == R.id.labelSignUp){
            // Go to sign up screen
            startActivity(new Intent(this, Register.class));
        }
    }

    private class ValidateLogin extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            return HttpManager.requestData("login",params);
        }

        @Override
        protected void onPostExecute(String result) {
            // If receives response from the server
            if (result!=null && !result.isEmpty()){
                // Convert into an object
                Customer customer = Customer.jsonToCustomer(result);
                if (customer!=null){
                    // Store the values locally
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("id", customer.getId());
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

                    Intent intent = new Intent(Login.this, MainMenu.class);
                    // Bring MainMenu to the top stack. By doing this, clicking back button will not bring user to the login screen any more.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getBaseContext(), getString(R.string.ui_message_unknown_error), Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class RequestPassword extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            HttpManager.requestData("request.password",params);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            promptMessage(getString(R.string.dialog_forget_password_requested));
        }
    }

    private void promptMessage(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

}
