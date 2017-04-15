package yuqiwang.automobilemodificationconfigurator;

import android.util.Log;
import java.lang.reflect.Field;

/**
 * Created by yuqi on 14/4/17.
 */

public final class Utility {

    public static int getResourceID(String name, Class<?> c) {

        name = name.replaceAll("\\s","");
        name = name.replaceAll("-","_");
        name = name.toLowerCase();

        try {
            Field idField = c.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception ex) {

            Log.e("ERROR", "RESOURCE NOT FOUND");
            return -1;
        }
    }
}
