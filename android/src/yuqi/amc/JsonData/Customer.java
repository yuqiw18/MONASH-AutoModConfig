package yuqi.amc.JsonData;

import org.json.JSONArray;
import org.json.JSONObject;

// This class contains the data structure of a customer
public class Customer {

    private long id;
    private String name;
    private String password;
    private String email;
    private String address;
    private String suburb;
    private int postcode;
    private String state;
    private String country;

    public Customer(){}

    public Customer(long id, String name, String password, String email, String address, String suburb, int postcode, String state, String country){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.suburb = suburb;
        this.postcode = postcode;
        this.state = state;
        this.country = country;
    }

    // Convert json to object
    public static Customer jsonToCustomer(String json){
        Customer customer = new Customer();
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            customer.setId(jsonObject.getLong("CUSTOMER_ID"));
            customer.setName(jsonObject.getString("CUSTOMER_NAME"));
            customer.setEmail(jsonObject.getString("CUSTOMER_EMAIL"));
            customer.setPassword(jsonObject.getString("CUSTOMER_PASSWORD"));
            customer.setAddress(jsonObject.getString("CUSTOMER_ADDRESS"));
            customer.setSuburb(jsonObject.getString("CUSTOMER_SUBURB"));
            customer.setPostcode(jsonObject.getInt("CUSTOMER_POSTCODE"));
            customer.setState(jsonObject.getString("CUSTOMER_STATE"));
            customer.setCountry(jsonObject.getString("CUSTOMER_COUNTRY"));
        }catch (Exception e){
            e.printStackTrace();
            customer = null;
        }
        return customer;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormattedAddress(){
        return address + "\n" + suburb + " " + postcode + ", " + state + "\n" + country;
    }
}
