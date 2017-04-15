package yuqiwang.automobilemodificationconfigurator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Model;

public class SplashTest extends AppCompatActivity {

    public static final int UPDATE_DB_REQUEST = 1;
    public static final String JSON_DATA_ADDRESS = "http://yuqi.ninja/";
    public static final String JSON_DATA_SOURCE[] = {"brand", "model"};

    private DatabaseHelper databaseHelper;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_splash);

//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (databaseHelper.isEmpty()){
            for (int i =0; i < JSON_DATA_SOURCE.length; i++){

                new FetchData().execute(JSON_DATA_ADDRESS + JSON_DATA_SOURCE[i] + ".json", JSON_DATA_SOURCE[i]);
            }

        }else {
            goToMain();
        }

    }

    private class FetchData extends AsyncTask<String, Void, String>{

        String identifier;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings){

            identifier = strings[1];

            try {
                URL downloadURL = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) downloadURL.openConnection();
                InputStream inputStream = connection.getInputStream();
                String result = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((result = bufferedReader.readLine())!=null){
                    stringBuilder.append(result);
                }
                progress ++;
                Log.e("Progress:", String.valueOf(progress));
                return stringBuilder.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if (result!=null){

                try{
                    JSONArray contents = new JSONArray(result);

                    for(int i = 0; i < contents.length(); i++){

                        JSONObject tempJSON = contents.getJSONObject(i);

                        switch (identifier){

                            case "brand":
                                Brand brand = new Brand(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("origin"));
                                databaseHelper.addData(brand);
                                Log.e("Loaded: ",brand.getName());
                                break;
                            case "model":
                                Model model = new Model(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("bodyType"), tempJSON.getString("brandName"));
                                databaseHelper.addData(model);
                                break;
                            default:
                                break;
                        }
                    }


                }catch (Exception e){

                    e.printStackTrace();

                }
            }

            // If finish downloading
            if (progress == JSON_DATA_SOURCE.length){
                goToMain();
            }
        }
    }

    private void goToMain(){

        // Normal
        if (progress == 0){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent main = new Intent(SplashTest.this, Main.class);

                    // Set up main activity
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(main);
                }
            }, 5000);
        }else {
            // After downloading
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent main = new Intent(SplashTest.this, Main.class);

                    // Set up main activity
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(main);
                }
            }, 3000);
        }

    }

}
