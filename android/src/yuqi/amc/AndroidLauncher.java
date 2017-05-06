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

import yuqi.amc.SqliteData.Badge;
import yuqi.amc.SqliteData.Brand;
import yuqi.amc.SqliteData.Model;

// Splash screen recreated using libGDX
public class AndroidLauncher extends AndroidApplication {

	public static final int UPDATE_DB_REQUEST = 1;
	private DatabaseHelper databaseHelper;
	private int progress = 0;
	private int dbToProcess = 0;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		initialize(new Main(), config);

		databaseHelper = new DatabaseHelper(getApplicationContext());

		dbToProcess = RestClient.DATABASE_TABLE.length;

		if (databaseHelper.isEmpty()){
			for (int i =0; i < dbToProcess; i++){
				new initDatabase().execute(RestClient.DATABASE_TABLE[i]);
			}
		}else {
			goToMain();
		}
	}

	private class initDatabase extends AsyncTask<String, Void, String> {

		String identifier;

		@Override
		protected String doInBackground(String... params){
			identifier = params[0];
			return RestClient.requestData(identifier, null);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			progress ++;
			Log.e("Progress:", String.valueOf(progress) + "/" + dbToProcess);
		}

		@Override
		protected void onPostExecute(String result){
			if (result!=null){
				try{
					JSONArray contents = new JSONArray(result);
					for(int i = 0; i < contents.length(); i++){
						JSONObject tempJSON = contents.getJSONObject(i);
						long id = tempJSON.getLong("ID");
						String name = tempJSON.getString("NAME");

						switch (identifier){
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
//							case "part":
//								Part part = new Part(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("type"), tempJSON.getDouble("price"), tempJSON.getInt("stock"), tempJSON.getString("modelName"),tempJSON.getString("badgeName"));
//								databaseHelper.addData(part);
//								Log.e("Loaded: ",part.getName());
//								break;
							default:
								break;
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			// If finish downloading
			if (progress == dbToProcess){
				goToMain();
			}
		}
	}



//	private class FetchData extends AsyncTask<String, Void, String> {
//
//		String identifier;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... strings){
//
//			identifier = strings[1];
//
//			try {
//				URL downloadURL = new URL(strings[0]);
//				HttpURLConnection connection = (HttpURLConnection) downloadURL.openConnection();
//				InputStream inputStream = connection.getInputStream();
//				String result = "";
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//				StringBuilder stringBuilder = new StringBuilder();
//				while((result = bufferedReader.readLine())!=null){
//					stringBuilder.append(result);
//				}
//				progress ++;
//				Log.e("Progress:", String.valueOf(progress) + "/" + Utility.DATA_CONTENT.length);
//				return stringBuilder.toString();
//			}catch (Exception e){
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result){
//			if (result!=null){
//
//				try{
//					JSONArray contents = new JSONArray(result);
//
//					for(int i = 0; i < contents.length(); i++){
//
//						JSONObject tempJSON = contents.getJSONObject(i);
//
//						switch (identifier){
//
//							case "brand":
//								Brand brand = new Brand(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("origin"));
//								databaseHelper.addData(brand);
//								Log.e("Loaded: ",brand.getName());
//								break;
//							case "model":
//								Model model = new Model(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("bodyType"), tempJSON.getString("brandName"));
//								databaseHelper.addData(model);
//								Log.e("Loaded: ",model.getName());
//								break;
//							case "badge":
//								Badge badge = new Badge(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("year"), tempJSON.getString("modelName"));
//								databaseHelper.addData(badge);
//								Log.e("Loaded: ",badge.getName());
//								break;
//							case "part":
//								Part part = new Part(tempJSON.getLong("id"), tempJSON.getString("name"), tempJSON.getString("type"), tempJSON.getDouble("price"), tempJSON.getInt("stock"), tempJSON.getString("modelName"),tempJSON.getString("badgeName"));
//								databaseHelper.addData(part);
//								Log.e("Loaded: ",part.getName());
//								break;
//							default:
//								break;
//						}
//					}
//
//
//				}catch (Exception e){
//
//					e.printStackTrace();
//
//				}
//			}
//
//			// If finish downloading
//			if (progress == Utility.DATA_CONTENT.length){
//				goToMain();
//			}
//		}
//	}

	private void goToMain() {

		// Normal
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
			}, 3000);
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
			}, 3000);
		}
	}
}
