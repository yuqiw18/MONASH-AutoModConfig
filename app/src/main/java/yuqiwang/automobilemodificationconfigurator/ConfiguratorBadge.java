package yuqiwang.automobilemodificationconfigurator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;
import yuqiwang.automobilemodificationconfigurator.DataStruct.DataStruct;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Model;

public class ConfiguratorBadge extends AppCompatActivity {

    private ListView badgeListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> badgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_badge);

        setTitle("Models");

        TextView modelName = (TextView) findViewById(R.id.textCBModelName);
        TextView modelYear = (TextView) findViewById(R.id.textCBModelYear);
        TextView badgeCounter = (TextView) findViewById(R.id.textCBBadgeCounter);
        ImageView modelImage = (ImageView) findViewById(R.id.imgCBModel);

        badgeListView = (ListView) findViewById(R.id.listBadges);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){

            Model data = incomingData.getParcelable("MODEL");

            badgeList = new ArrayList<>(databaseHelper.getData("BADGE", "MODEL_NAME", new String[]{data.getName()}).values());

            dataAdapter = new DataAdapter(this, badgeList);

            badgeListView.setAdapter(dataAdapter);

//            brandName.setText(data.getName());
//
//            brandOrigin.setText(data.getOrigin());
//
//            brandLogo.setImageResource(Utility.getResourceID(brandName.getText().toString(),R.drawable.class));
//
//            modelCounter.setText("Available Models: " + modelList.size());


        }

        badgeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String selectedModel = badgeList.get(position).getName();
                Intent intent = new Intent(ConfiguratorBadge.this, Previewer.class);
                intent.putExtra("BADGE",selectedModel);
                startActivity(intent);
            }
        });



    }
}
