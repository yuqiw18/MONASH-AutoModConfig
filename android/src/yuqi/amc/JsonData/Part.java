package yuqi.amc.JsonData;

import org.json.JSONObject;

/**
 * Created by ClayW on 7/05/2017.
 */

public class Part {

    private long id;
    private String name;
    private String type;
    private Double price;
    private int stock;
    private String description;
    private String compatModel;
    private String compatBadge;
    private String value;

    public Part(){}

    //public Part(long id, String name, String type, Double price, int stock, String){}


    public static Part jsonToPart(JSONObject jsonObject){
        Part part = new Part();
        try {
            //JSONObject jsonObject = new JSONObject(jsonObj);
            part.setId(jsonObject.getLong("ID"));
            part.setName(jsonObject.getString("NAME"));
            part.setType(jsonObject.getString("PART_TYPE"));
            part.setPrice(jsonObject.getDouble("PART_PRICE"));
            part.setStock(jsonObject.getInt("PART_STOCK"));
            part.setDescription(jsonObject.getString("PART_DESCRIPTION"));
            part.setCompatModel(jsonObject.getString("PART_COMPAT_MODEL"));
            part.setCompatBadge(jsonObject.getString("PART_COMPAT_BADGE"));
            part.setValue(jsonObject.getString("PART_EXTRA_VALUE"));
        }catch (Exception e){
            e.printStackTrace();
            part = null;
        }
        return part;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompatModel() {
        return compatModel;
    }

    public void setCompatModel(String compatModel) {
        this.compatModel = compatModel;
    }

    public String getCompatBadge() {
        return compatBadge;
    }

    public void setCompatBadge(String compatBadge) {
        this.compatBadge = compatBadge;
    }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
