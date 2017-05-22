package yuqi.amc;

import android.util.Log;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuqi on 14/4/17.
 */

public final class Utility {

    public static final String IMAGE_SOURCE = "http://amc.yuqi.ninja/img/";
    public static final String IMAGE_FORMAT = ".png";

    // Convert string name to resource ID
    public static int getResourceID(String name, Class<?> c) {
        name = stringConvert(name);
        try {
            Field idField = c.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e("ERROR", "RESOURCE NOT FOUND");
            return -1;
        }
    }

    // Convert string to the predefined format
    public static String stringConvert(String input){
        input = input.replaceAll("\\s","_");
        input = input.replaceAll("-","_");
        input = input.toLowerCase();
        return input;
    }

    public static String getImageAddress(String name){
        return IMAGE_SOURCE+ stringConvert(name)+ IMAGE_FORMAT;
    }

    // Format date
    public static String formatDate(String strDate){
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
            return new SimpleDateFormat("dd MMM yyyy").format(date).toUpperCase();
        }catch (Exception e){
            return "1 JAN 1970";
        }
    }

    // Format float number to a proper price
    public static String getFormattedPrice(double price){
        return "$" + String.format("%.2f", price);
    }

    // SHA-512 Hashing
    private String Hash(String password){
        String generatedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

}


