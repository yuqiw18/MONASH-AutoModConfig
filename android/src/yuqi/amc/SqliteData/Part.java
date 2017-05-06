package yuqi.amc.SqliteData;

/**
 * Created by ClayW on 19/04/2017.
 */

public class Part extends DataStruct {

    public static final String CREATE_STATEMENT = "CREATE TABLE PART " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "PART_NAME TEXT NOT NULL," +
            "PART_TYPE TEXT NOT NULL," +
            "PART_PRICE NUMERIC NOT NULL," +
            "PART_STOCK INTEGER NOT NULL," +
            "MODEL_NAME TEXT NOT NULL," +
            "BADGE_NAME TEXT NOT NULL" +
            ");";

    //enum PART_TYPE {BONNET, BUMPER, SPOILER, RIM, EXHAUST, ENGINE, LIGHTING, TYRE, ACCESSORY, RESPRAY}

    //private PART_TYPE partType;

    private String type;
    private double price;
    private int stock;
    private String modelName;
    private String badgeName;

    public Part(long id, String name, String type, double price, int stock, String modelName, String badgeName ){
        super(id, name);
        //this.partType = partType;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.modelName = modelName;
        this.badgeName = badgeName;
    }

//    public PART_TYPE getPartType(){
//        return partType;
//    }
    public String getType(){ return type; }

    public double getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public String getModelName(){ return modelName; }

    public String getBadgeName(){
        return badgeName;
    }

}

