package yuqiwang.automobilemodificationconfigurator.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */



public class Part extends DataStruct {

    public static final String CREATE_STATEMENT = "CREATE TABLE PART " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "PART_NAME TEXT NOT NULL," +
            "PART_TYPE TEXT NOT NULL," +
            "PART_PRICE NUMERIC NOT NULL," +
            "PART_STOCK INTEGER NOT NULL," +
            "BADGE_NAME TEXT NOT NULL," +
            "FOREIGN KEY(BADGE_NAME) REFERENCES BADGE(BADGE_NAME)" +
            ");";

    enum PART_TYPE {BONNET, BUMPER, SPOILER, RIM, EXHAUST, ENGINE, LIGHTING, TYRE, ACCESSORY, RESPRAY}

    private PART_TYPE partType;
    private double price;
    private int stock;
    private String badgeName;

    public Part(long id, String name, PART_TYPE partType, double price, int stock, String badgeName ){
        super(id, name);
        this.partType = partType;
        this.price = price;
        this.stock = stock;
        this.badgeName = badgeName;
    }

    public PART_TYPE getPartType(){
        return partType;
    }

    public double getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public String getBadgeName(){
        return badgeName;
    }
}
