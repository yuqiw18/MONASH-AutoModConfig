package yuqi.amc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import yuqi.amc.SQLiteData.Badge;
import yuqi.amc.SQLiteData.Brand;
import yuqi.amc.SQLiteData.Model;

// Launcher as the LibGDX entry point
public class AndroidLauncher extends AndroidApplication {

	private DatabaseHelper databaseHelper;
	private int progress = 0;
	private int dbToProcess = 0;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Entry point statement
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(), config);

        // Initialise the database if no local records
		databaseHelper = new DatabaseHelper(getApplicationContext());

        // Get the number of database to process
		dbToProcess = HttpManager.DATABASE_TABLE.length;

        // If the database is empty then down load all the databases
		if (databaseHelper.isEmpty()){
			for (int i =0; i < dbToProcess; i++){
				new initDatabase().execute(HttpManager.DATABASE_TABLE[i]);
			}
		}else {
            doneInit();
		}
	}

	// Initialise local database
	private class initDatabase extends AsyncTask<String, Void, String> {

		String table;

		@Override
		protected String doInBackground(String... params){
			table = params[0];
			return HttpManager.requestData(table, null);
		}

		@Override
		protected void onPostExecute(String result){

			// If has content then convert the json info to data that can be stored in sqlite database
			if (result!=null){
				try{
					JSONArray contents = new JSONArray(result);
					for(int i = 0; i < contents.length(); i++){
						JSONObject tempJSON = contents.getJSONObject(i);
						long id = tempJSON.getLong("ID");
						String name = tempJSON.getString("NAME");

						// Determine which data should be stored in which database
						switch (table){
							case "brand":
								Brand brand = new Brand(id, name, tempJSON.getString("BRAND_ORIGIN"));
								databaseHelper.addData(brand);
								Log.e("Loaded: ",brand.getName());
								break;
							case "model":
								Model model = new Model(id, name, tempJSON.getString("BODY_TYPE"), tempJSON.getString("DRIVE_TYPE"), tempJSON.getString("BRAND_NAME"));
								databaseHelper.addData(model);
								Log.e("Loaded: ",model.getName());
								break;
							case "badge":
								Badge badge = new Badge(id, name, tempJSON.getString("BADGE_YEAR"), tempJSON.getString("MODEL_NAME"));
								databaseHelper.addData(badge);
								Log.e("Loaded: ",badge.getName());
								break;
							default:
								break;
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			progress ++;
			// If finish downloading
			if (progress == dbToProcess){
                doneInit();
			}
		}
	}

	// Complete the initialisation and go to the main activity
	private void doneInit() {
		// If there has no content to be downloaded, wait for 2 seconds
		if (progress == 0) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					Intent main = new Intent(AndroidLauncher.this, MainMenu.class);

					// Set up main activity
					main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					startActivity(main);
				}
			}, 2000);
		} else {
			// After downloading
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					Intent main = new Intent(AndroidLauncher.this, MainMenu.class);

					// Set up main activity
					main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					startActivity(main);
				}
			}, 1000);
		}
	}
}
