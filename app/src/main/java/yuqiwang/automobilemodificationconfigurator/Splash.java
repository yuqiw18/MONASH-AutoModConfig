package yuqiwang.automobilemodificationconfigurator;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;

public class Splash extends AppCompatActivity {

    public static final int UPDATE_DB_REQUEST = 1;
    public static final String JSON_UPDATE_SOURCE = "http://yuqi.ninja/brand.json";

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btn = (Button)findViewById(R.id.testBtn);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(Splash.this,ConfiguratorBrand.class));

            }




        });

        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (databaseHelper.isEmpty()){
            new FetchData().execute(JSON_UPDATE_SOURCE);
        }



       //startActivity(new Intent(this, ConfiguratorBrand.class));
    }

    private class FetchData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings){
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

                        Brand brand = new Brand(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("origin"));

                        databaseHelper.addData(brand);
                    }

                    Toast.makeText(getBaseContext(), "Downloaded!", Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                    e.printStackTrace();

                }
            }
        }

    }

}
