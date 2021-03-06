package yuqi.amc.JsonData;

import org.json.JSONObject;

public class Config {

    private long id;
    private String name;
    private String type;
    private String highlight;
    private String content;
    private String budget;

    public Config(){}

    // Convert json to object
    public static Config jsonToConfig(JSONObject jsonObject){
        Config config = new Config();
        try {
            config.setId(jsonObject.getLong("ID"));
            config.setName(jsonObject.getString("CONFIG_NAME"));
            config.setType(jsonObject.getString("CONFIG_TYPE"));
            config.setHighlight(jsonObject.getString("CONFIG_HIGHLIGHT"));
            config.setContent(jsonObject.getString("CONFIG_CONTENT"));
            config.setBudget(jsonObject.getString("CONFIG_BUDGET"));
        }catch (Exception e){
            e.printStackTrace();
            config = null;
        }
        return config;
    }

    @Override
    public String toString() {

        String item[] = content.split(";");
        String desc = "";

        for (int i = 0 ; i < item.length; i ++){

            desc += item[i];

            if (i + 1 != item.length){
                desc += "\n";
            }
        }

        return desc;
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

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
