package yuqiwang.automobilemodificationconfigurator.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */

public class Brand extends DataStruct {

    String origin;

    public Brand(long id, String name, String origin){
        this.id = id;
        this.name = name;
        this.origin = origin;
    }

    public String getOrigin(){
        return origin;
    }

}



