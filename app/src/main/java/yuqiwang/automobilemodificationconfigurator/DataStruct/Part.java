package yuqiwang.automobilemodificationconfigurator.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */



public class Part extends DataStruct {

    enum PART_TYPE {BONNET, BUMPER, SPOILER, RIM, EXHAUST, ENGINE, LIGHTING, TYRE, ACCESSORY, RESPRAY}

    long badgeId;
    PART_TYPE type;
    double price;
    int stock;
    String filePath;


}
