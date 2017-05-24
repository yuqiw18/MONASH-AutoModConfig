package yuqi.amc.JsonData;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by ClayW on 6/05/2017.
 */

public class Order {

    private long id;
    private Timestamp datetime;
    private double price;
    private String detail;
    private String address;
    private long customerId;
    private String status;
    private Timestamp bookingDatetime;
    private long centerId;

    public Order(){}

    public Order(long customerId, double price, String detail, String address, Timestamp bookingDatetime, long centerId){
        this.id = -1;
        this.datetime = null;
        this.customerId = customerId;
        this.price = price;
        this.detail = detail;
        this.address = address;
        this.status = null;
        this.bookingDatetime = bookingDatetime;
        this.centerId = centerId;
    }

    // Convert json to object
    public static Order jsonToOrder(JSONObject jsonObject){
        Order order = new Order();
        try {
            order.setId(jsonObject.getLong("TRANSACTION_ID"));
            order.setDatetime(Timestamp.valueOf(jsonObject.getString("TRANSACTION_DATETIME")));
            order.setPrice(jsonObject.getDouble("TRANSACTION_PRICE"));
            order.setDetail(jsonObject.getString("TRANSACTION_DETAIL"));
            order.setCustomerId(jsonObject.getLong("CUSTOMER_ID"));
            order.setAddress(jsonObject.getString("TRANSACTION_ADDRESS"));
            order.setStatus(jsonObject.getString("TRANSACTION_STATUS"));

            //order.setBookingDatetime(Timestamp.valueOf(jsonObject.getString("BOOKING_DATETIME")));

            order.setBookingDatetime(null);

            order.setCenterId(jsonObject.getLong("CENTER_ID"));
        }catch (Exception e){
            e.printStackTrace();
            order = null;
        }
        return order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getBookingDatetime() {
        return bookingDatetime;
    }

    public void setBookingDatetime(Timestamp bookingDatetime) {
        this.bookingDatetime = bookingDatetime;
    }

    public long getCenterId() {
        return centerId;
    }

    public void setCenterId(long centerId) {
        this.centerId = centerId;
    }
}
