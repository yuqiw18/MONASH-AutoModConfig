package yuqi.amc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import yuqi.amc.DataStruct.Brand;
import yuqi.amc.DataStruct.DataStruct;
import yuqi.amc.DataStruct.Model;

public class ConfiguratorModel extends AppCompatActivity {

    private ListView modelListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_model);

        setTitle("Models");

        TextView brandName = (TextView) findViewById(R.id.textCMBrandName);
        TextView brandOrigin = (TextView) findViewById(R.id.textCMBrandOrigin);
        TextView modelCounter = (TextView) findViewById(R.id.textCMModelCounter);
        ImageView brandLogo = (ImageView) findViewById(R.id.imgCMBrand);

        modelListView = (ListView) findViewById(R.id.listModels);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){

            Brand data = incomingData.getParcelable("BRAND");

            modelList = new ArrayList<>(databaseHelper.getData("MODEL", new String[]{"BRAND_NAME"}, new String[]{data.getName()}).values());

            dataAdapter = new DataAdapter(this, modelList);

            modelListView.setAdapter(dataAdapter);

            brandName.setText(data.getName());

            brandOrigin.setText(data.getOrigin());

            Picasso.with(this).load(Utility.getImageAddress(brandName.getText().toString())).into(brandLogo);

            //brandLogo.setImageResource(Utility.getResourceID(brandName.getText().toString(),R.drawable.class));

            modelCounter.setText("Available Models: " + modelList.size());

        }

        modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Model selectedModel = (Model) modelList.get(position);
                Intent intent = new Intent(ConfiguratorModel.this, ConfiguratorBadge.class);
                intent.putExtra("MODEL",selectedModel);
                startActivity(intent);
            }
        });

    }
}
