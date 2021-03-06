package yuqi.amc;
import yuqi.amc.SQLiteData.Brand;
import yuqi.amc.SQLiteData.DataStruct;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class ConfiguratorBrand extends AppCompatActivity {

    private ListView brandListView;
    private SQLiteDataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_brand);

        setTitle(getString(R.string.title_brand));

        // Initialise all the variables
        databaseHelper = new DatabaseHelper(getApplicationContext());

        brandList = new ArrayList<>(databaseHelper.getData("BRAND", null, null).values());
        brandListView = (ListView) findViewById(R.id.listBrands);
        dataAdapter = new SQLiteDataAdapter(this, brandList);

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
