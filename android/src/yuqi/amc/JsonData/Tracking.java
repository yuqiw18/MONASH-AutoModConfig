package yuqi.amc.JsonData;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tracking {

    private long id;
    private String courier;
    private String detail;

    public Tracking(){
        id = 0;
        courier = "Unknown";
        detail = "";
    }

    public static Tracking strJsonToTracking(String json){

        Tracking tracking = new Tracking();

        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            tracking.id = jsonObject.getLong("TRACKING_NUMBER");
            tracking.courier = jsonObject.getString("TRACKING_COURIER");
            tracking.detail = jsonObject.getString("TRACKING_DETAIL");

            Log.e("GET",tracking.courier);
            Log.e("GET",tracking.detail);
        }catch (Exception e){
            e.printStackTrace();
        }

        return tracking;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFormattedDetail(){

        Log.e("Detail", detail);
        String line[] = detail.split(";");
        if (line.length != 0){
            String formatted = "";

            for (int i = 0 ; i < line.length; i ++ ){
                formatted += line[i] + "\n";
            }

            return formatted;

        }else {
            return "No Tracking Info Available";
        }
    }
}
