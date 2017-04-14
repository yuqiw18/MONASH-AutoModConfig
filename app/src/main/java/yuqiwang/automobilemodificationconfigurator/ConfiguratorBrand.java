package yuqiwang.automobilemodificationconfigurator;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class ConfiguratorBrand extends AppCompatActivity {

    private ListView brandListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Brand> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_brand);

        setTitle("Search");

        // Initialise all the variables
        databaseHelper = new DatabaseHelper(getApplicationContext());

        // Add default records if the list is empty
        //if (databaseHelper.getData("").size() == 0) databaseHelper.createSampleMonster();

        brandList = new ArrayList<>(databaseHelper.getData().values());

        brandListView = (ListView) findViewById(R.id.listBrands);

        dataAdapter = new DataAdapter(this, brandList);

        brandListView.setAdapter(dataAdapter);

    }
}
