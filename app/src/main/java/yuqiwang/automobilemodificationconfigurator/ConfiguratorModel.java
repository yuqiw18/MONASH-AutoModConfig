package yuqiwang.automobilemodificationconfigurator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import yuqiwang.automobilemodificationconfigurator.DataStruct.DataStruct;

public class ConfiguratorModel extends AppCompatActivity {

    private ListView modelListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_model);

        // Initialise all the variables
        databaseHelper = new DatabaseHelper(getApplicationContext());

        modelListView = (ListView) findViewById(R.id.listModels);

        Bundle incomingData = getIntent().getExtras();
        if (incomingData!=null){

            DataStruct data = incomingData.getParcelable("BRAND");

            modelList = new ArrayList<>(databaseHelper.getData("MODEL", "BRAND_NAME", new String[]{data.getName()}).values());

            dataAdapter = new DataAdapter(this, modelList);

            modelListView.setAdapter(dataAdapter);

        }

        modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String selectedModel = modelList.get(position).getName();
                Intent intent = new Intent(ConfiguratorModel.this, ConfiguratorBadge.class);
                intent.putExtra("MODEL",selectedModel);
                startActivity(intent);
            }
        });

    }
}
