package yuqi.amc.JsonData;

import org.json.JSONObject;
import java.text.DecimalFormat;

// This class contains the data structure of a service center
public class Center {

    private long id;
    private double latitude;
    private double longitude;
    private String name;
    private String address;
    private String detail;
    private double price;
    private Double avgScore;
    private double distance;

    public Center(){}

    // Convert json to object
    public static Center jsonToServiceCenter(JSONObject jsonObject){
        Center center = new Center();
        try {
            center.setId(jsonObject.getLong("CENTER_ID"));
            center.setLatitude(jsonObject.getDouble("CENTER_LAT"));
            center.setLongitude(jsonObject.getDouble("CENTER_LNG"));
            center.setName(jsonObject.getString("CENTER_NAME"));
            center.setAddress(jsonObject.getString("CENTER_ADDRESS"));
            center.setDetail(jsonObject.getString("CENTER_DETAIL"));
            center.setPrice(jsonObject.getDouble("CENTER_PRICE"));
            center.setDistance(jsonObject.getDouble("CENTER_DISTANCE"));
            Long totalScore = jsonObject.getLong("CENTER_SCORE");
            Long rateNum = jsonObject.getLong("CENTER_RATE_NUM");
            if (rateNum!= 0){
                center.setAvgScore((double)totalScore/(double)rateNum);
            }else {
                center.setAvgScore(-1.0d);
            }
        }catch (Exception e){
            e.printStackTrace();
            center = null;
        }
        return center;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        this.avgScore =  Double.valueOf(decimalFormat.format(avgScore));
    }

    public void setDistance(double distance){
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        this.distance = Double.valueOf(decimalFormat.format(distance));
    }

    public double getDistance(){ return distance; }

    public String getFormattedAddress(){
        String[] s = address.split(",");
        return s[0].toUpperCase().trim() + "\n" + s[1].toUpperCase().trim() + "\n" + s[2].toUpperCase().trim();
    }
}
