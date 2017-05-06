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

import org.json.JSONArray;
import org.json.JSONObject;

import yuqi.amc.JsonData.Customer;

public class Login extends AppCompatActivity implements OnClickListener {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("AMC Customer Login");
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            new validateLogin().execute();
        }else if (v.getId() == R.id.labelForgetPassword){
            // Display a dialog for customers to enter their email address
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
                            new requestPassword().execute(input);
                            alertDialog.dismiss();
                        }
                    }else {
                        Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_validate_empty), Toast.LENGTH_LONG).show();
                    }
                }
            });
            alertDialog.show();
        }else if (v.getId() == R.id.labelSignUp){
            startActivity(new Intent(this, Register.class));
        }
    }

    private class validateLogin extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result!=null && !result.isEmpty()){
                Customer customer = Customer.jsonToCustomer(result);
                if (customer!=null){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
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

    private class requestPassword extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            RestClient.requestData("request.password",params);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_requested), Toast.LENGTH_LONG).show();
        }
    }
}
