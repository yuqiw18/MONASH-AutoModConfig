package yuqi.amc;
import yuqi.amc.SqliteData.Brand;
import yuqi.amc.SqliteData.DataStruct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;


public class ConfiguratorBrand extends AppCompatActivity {

    private ListView brandListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_brand);

        setTitle("Brands");

        // Initialise all the variables
        databaseHelper = new DatabaseHelper(getApplicationContext());

        // Add default records if the list is empty
        //if (databaseHelper.getData("BRAND", null, null).size() == 0) databaseHelper.createDefault();

        brandList = new ArrayList<>(databaseHelper.getData("BRAND", null, null).values());

        brandListView = (ListView) findViewById(R.id.listBrands);

        dataAdapter = new DataAdapter(this, brandList);

        brandListView.setAdapter(dataAdapter);

        brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Brand selectedBrand = (Brand) brandList.get(position);
                Intent intent = new Intent(ConfiguratorBrand.this, ConfiguratorModel.class);
                intent.putExtra("BRAND",selectedBrand);
                startActivity(intent);
            }
        });
    }
}
