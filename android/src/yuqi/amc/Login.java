package yuqi.amc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("AMC Customer Login");
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            new validateLogin().execute();
        }else if (v.getId() == R.id.labelForgetPassword){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forget_password, null);
            final EditText requrestEmail = (EditText)dialogView.findViewById(R.id.inputForgetPasswordEmail);
            Button btnSubmitRequest = (Button)dialogView.findViewById(R.id.btnForgetPasswordRequest);
            btnSubmitRequest.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = requrestEmail.getText().toString().trim();
                    if (!input.isEmpty()){
                        if(!Patterns.EMAIL_ADDRESS.matcher(input).matches()){
                            Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_invalid), Toast.LENGTH_LONG).show();
                        }else {

                        }
                    }else {
                        Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_validate_empty), Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setView(dialogView);
            builder.create().show();
        }else if (v.getId() == R.id.labelSignUp){


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
                Intent intent = new Intent(Login.this, MainMenu.class);
                // Bring MainMenu to the top stack. By doing this, clicking back button will not bring user to the login screen any more.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("JsonArray", result);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class requestPassword extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            RestClient.requestData("",params);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getBaseContext(), getString(R.string.dialog_forget_password_requested), Toast.LENGTH_LONG).show();
        }
    }
}
