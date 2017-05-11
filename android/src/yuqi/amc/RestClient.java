package yuqi.amc;

import android.util.Log;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {

    private RestClient(){}

    private static final String BASE_URI = "http://yuqi.ninja/rest/";
    protected static final String DATABASE_TABLE[] = {"brand", "model", "badge"};

    // GET: Universal Method for getting data from server. It can takes any number of parameters by given the correct RESTful method name.
    protected static String requestData(String methodPath, String[] args){
        //Initialise
        URL url;
        HttpURLConnection connection = null;
        String textResult = "";
        StringBuilder parameters = new StringBuilder();
        //Making HTTP request
        try{
            // If it has parameters, generate the address for them
            if (args!=null && args.length!=0){
                for (int i =0 ; i< args.length; i ++){
                    parameters.append("/");
                    parameters.append(args[i]);
                }

                url = new URL(BASE_URI + methodPath + parameters.toString() );

                // If it doesn't, do as normal
            }else {
                url = new URL(BASE_URI + methodPath);
            }

            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return textResult;
    }

    // POST: Universal method for creating any type of class
    protected static int createData(String methodPath, Object object){

        HttpURLConnection connection = null;
        int responseCode = 200;

        try {
            Gson gson = new Gson();
            String strJsonObject = gson.toJson(object);
            URL url = new URL(BASE_URI + methodPath);

            Log.e("TEST JSON: ",strJsonObject.toString());

            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(strJsonObject.getBytes().length);
            connection.setRequestProperty("Content-Type","application/json");

            PrintWriter out = new PrintWriter(connection.getOutputStream());

            out.write(strJsonObject);
            out.flush();
            out.close();

            responseCode = connection.getResponseCode();
            Log.e("HTTP",String.valueOf(responseCode));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        return responseCode;
    }

    // PUT: Update
    protected static String updateData(String methodPath, Object object){

        HttpURLConnection connection = null;
        String responseCode = "0";

        try {
            Gson gson = new Gson();
            String strJsonObject = gson.toJson(object);
            URL url = new URL(BASE_URI + methodPath + "/24717835");

            connection = (HttpURLConnection)url.openConnection();

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(strJsonObject.getBytes().length);
            connection.setRequestProperty("Content-Type","application/json");

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.write(strJsonObject);
            out.flush();
            out.close();
            //connection.getInputStream();

            responseCode = String.valueOf(connection.getResponseCode());
            Log.e("HTTP",responseCode);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        return responseCode;
    }

    // DELETE: Delete record from server by providing the method path and id
    protected static void deleteData(String methodPath, int id){

        methodPath = methodPath + "/" + id;

        HttpURLConnection connection = null;

        try {
            URL url = new URL(BASE_URI + methodPath);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            Log.i("error",new Integer(connection.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
    //RestClient.deleteData("/monashfriendfinder.profile/",24717837);

    protected static int processResponseCode(int responseCode){
        if (responseCode == 200 || responseCode == 202 || responseCode == 204){
            return 1;
        }else if (responseCode == 400 || responseCode == 500){
            return 0;
        }else {
            return -1;
        }
    }

}
