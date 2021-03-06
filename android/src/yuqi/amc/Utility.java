package yuqi.amc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Utility {

    public static final String IMAGE_SOURCE = "http://amc.yuqi.dev/img/";
    public static final String IMAGE_FORMAT = ".png";
    public static final String SERVER_TIMEZONE = "GMT-6";

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

    public static String getFormattedPrice(String strPrice){
        double price = Double.valueOf(strPrice);
        return "$" + String.format("%.2f", price);
    }


    public static String UTCtoLocal(long timestamp){

        try {
            Date date = new Date(timestamp);

            DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            localFormat.setTimeZone(TimeZone.getDefault());

            return localFormat.format(date);

        }catch (Exception e){
            return null;
        }
    }

    public static String formatBookingDate(String strDate){
        try{
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }catch (Exception e){
            return "1970-01-01";
        }
    }

    public static String formatBookingTime(Integer time){

        switch (time){
            case 8:
                return "08:00:00";
            case 11:
                return "11:00:00";
            case 2:
                return "14:00:00";
            default:
                return "00:00:00";
        }

    }
}


