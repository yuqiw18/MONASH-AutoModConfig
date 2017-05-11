package yuqi.amc.JsonData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yuqi on 11/5/17.
 */

public class Payment {

    private long id;
    private long customerId;
    private String type;
    private String info1;
    private String info2;
    private String info3;
    private String info4;
    private Integer isPrimary;

    public Payment(){}


    public static Payment jsonToPayment(JSONObject jsonObject){
        Payment payment = null;
        try {





        }catch (Exception e){




        }

        return payment;
    }


    public static Payment strJsonToPayment(String json){
        Payment payment = new Payment();
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            payment.setId(jsonObject.getLong("PAYMENT_RECORD"));
            payment.setCustomerId(jsonObject.getLong("CUSTOMER_ID"));
            payment.setType(jsonObject.getString("PAYMENT_TYPE"));
            payment.setInfo1(jsonObject.getString("PAYMENT_INFO1"));
            payment.setInfo2(jsonObject.getString("PAYMENT_INFO2"));
            payment.setInfo3(jsonObject.getString("PAYMENT_INFO3"));
            payment.setInfo4(jsonObject.getString("PAYMENT_INFO4"));
            payment.setPrimary(jsonObject.getInt("PAYMENT_PRIMARY"));

        }catch (Exception e){
            e.printStackTrace();
            payment = null;
        }

        return payment;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getInfo3() {
        return info3;
    }

    public void setInfo3(String info3) {
        this.info3 = info3;
    }

    public String getInfo4() {
        return info4;
    }

    public void setInfo4(String info4) {
        this.info4 = info4;
    }

    public Integer isPrimary() {
        return isPrimary;
    }

    public void setPrimary(Integer primary) {
        isPrimary = primary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
