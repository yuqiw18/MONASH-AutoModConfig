package yuqi.amc.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */

public class Badge extends DataStruct {


    public static final String CREATE_STATEMENT = "CREATE TABLE BADGE " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "BADGE_NAME TEXT NOT NULL," +
            "BADGE_YEAR TEXT NOT NULL," +
            "MODEL_NAME TEXT NOT NULL," +
            "FOREIGN KEY(MODEL_NAME) REFERENCES MODEL(MODEL_NAME)" +
            ");";

    private String year;
    private String modelName;

    public Badge(long id, String name, String year, String modelName){
        super(id, name);
        this.year = year;
        this.modelName = modelName;
    }

    public String getYear(){
        return year;
    }

    public String getModelName(){
        return modelName;
    }
}
