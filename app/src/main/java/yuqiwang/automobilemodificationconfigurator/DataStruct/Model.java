package yuqiwang.automobilemodificationconfigurator.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */

public class Model extends DataStruct {

    public static final String CREATE_STATEMENT = "CREATE TABLE MODEL " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "MODEL_NAME TEXT NOT NULL," +
            "MODEL_BODY_TYPE TEXT NOT NULL," +
            "BRAND_NAME TEXT NOT NULL," +
            "FOREIGN KEY(BRAND_NAME) REFERENCES BRAND(BRAND_NAME)" +
            ");";

    private String bodyType;
    private String brandName;

    public Model(long id, String name, String bodyType, String brandName){
        super(id, name);
        this.bodyType = bodyType;
        this.brandName = brandName;
    }

    public String getBodyType(){
        return bodyType;
    }

    public String getBrandName(){
        return brandName;
    }

}

