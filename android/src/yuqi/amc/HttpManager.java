package yuqi.amc;

import android.util.Log;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// Http Manager used with Representational State Transfer
// GET, POST, PUT
// DELETE is not provided since this app has no implementation of any DELETE-related function
public class HttpManager {

    private HttpManager(){}

    // Server address
    private static final String BASE_URI = "http://amc.yuqi.dev/rest/";

    // Method(table) names for SQLite Database
    protected static final String DATABASE_TABLE[] = {"brand", "model", "badge"};

    // GET: Universal Method for getting data from server. It can takes any number of parameters by given the correct RESTful method name.
    protected static String requestData(String methodPath, String[] args){
        // Initialise
        URL url;
        HttpURLConnection connection = null;
        String content = "";
        StringBuilder parameters = new StringBuilder();
        // Make HTTP request
        try{
            // If it has parameters, generate the address for it
            // Format will be METHOD_NAME/PARAMETER1/PARAMETER2...
            if (args!=null && args.length!=0){
                for (int i =0 ; i< args.length; i ++){
                    parameters.append("/");
                    parameters.append(args[i]);
                }

                url = new URL(BASE_URI + methodPath + parameters.toString() );

            }else {

                // Do as normal if it doesn't provide any parameter
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
                // Read the input until there is no content
                content += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        // Return the content
        return content;
    }

    // POST: Universal method for creating any type of object
    protected static int createData(String methodPath, Object object){

        //
        HttpURLConnection connection = null;
        int responseCode = 200;

        try {
            // Use Gson to convert an object into Json
            Gson gson = new Gson();
            String strJsonObject = gson.toJson(object);
            URL url = new URL(BASE_URI + methodPath);

            Log.e("TEST JSON: ",strJsonObject.toString());

            // Create a connection and set the parameters
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(strJsonObject.getBytes().length);
            connection.setRequestProperty("Content-Type","application/json");

            PrintWriter out = new PrintWriter(connection.getOutputStream());

            // Send the content at once then close
            out.write(strJsonObject);
            out.flush();
            out.close();

            // Get the response code from server
            responseCode = connection.getResponseCode();
            Log.e("HTTP",String.valueOf(responseCode));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        return responseCode;
    }

    // PUT: Universal method for update any type of object
    // This method is almost same to the POST, only difference is that the Request Method is PUT
    protected static int updateData(String methodPath, Object object){

        HttpURLConnection connection = null;
        int responseCode = 200;

        try {
            Gson gson = new Gson();
            String strJsonObject = gson.toJson(object);
            URL url = new URL(BASE_URI + methodPath);

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

            responseCode = connection.getResponseCode();
            Log.e("HTTP",String.valueOf(responseCode));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        return responseCode;
    }

    // Process response codes
    protected static int processResponseCode(int responseCode){
        // For known response codes, 200/202/204 means the request is processed
        // 400/404 usually means the server has some problems
        // 500 means the request has problem, example can be repeated username for newly registered account
        // Other response codes are all classified as UNKNOWN ERROR since they rarely occur
        if (responseCode == 200 || responseCode == 202 || responseCode == 204){
            return 1;
        }else if (responseCode == 400 || responseCode == 404 || responseCode == 500){
            return 0;
        }else {
            return -1;
        }
    }

}
