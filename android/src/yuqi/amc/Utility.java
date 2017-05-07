package yuqi.amc;

import android.util.Base64;
import android.util.Log;
import java.lang.reflect.Field;

/**
 * Created by yuqi on 14/4/17.
 */

public final class Utility {

    public static final String IMAGE_SOURCE = "http://yuqi.ninja/amc/img/";
    public static final String IMAGE_FORMAT = ".png";
    private static final String ENCODE_TYPE = "UTF-8";

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

    // Encoding
    protected static String Encode(String content){
        String encoded = null;
        try{
            byte[] data = content.getBytes(ENCODE_TYPE);
            encoded = Base64.encodeToString(data, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return encoded;
        }
    }

    protected static String Decode(String content){
        String decoded = null;
        try{
            byte[] data = Base64.decode(content, Base64.DEFAULT);
            decoded = new String(data, ENCODE_TYPE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return decoded;
        }
    }
}


