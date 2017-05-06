package yuqi.amc;

import android.util.Log;
import java.lang.reflect.Field;

/**
 * Created by yuqi on 14/4/17.
 */

public final class Utility {

    public static final String IMAGE_SOURCE = "http://yuqi.ninja/amc/img/";
    public static final String IMAGE_FORMAT = ".png";

    // Convert string name to resource ID
    public static int getResourceID(String name, Class<?> c) {
        name = stringConvert(name);
        try {
            Field idField = c.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception ex) {

            Log.e("ERROR", "RESOURCE NOT FOUND");
            return -1;
        }
    }

    // Format string
    public static String stringConvert(String input){
        input = input.replaceAll("\\s","_");
        input = input.replaceAll("-","_");
        input = input.toLowerCase();
        return input;
    }

    public static String getImageAddress(String name){
        return IMAGE_SOURCE+ stringConvert(name)+ IMAGE_FORMAT;
    }

}


