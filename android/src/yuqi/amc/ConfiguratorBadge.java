package yuqi.amc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import yuqi.amc.SQLiteData.Badge;
import yuqi.amc.SQLiteData.DataStruct;
import yuqi.amc.SQLiteData.Model;

public class ConfiguratorBadge extends AppCompatActivity {

    private ListView badgeListView;
    private SQLiteDataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> badgeList;
    private String brandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurator_badge);

        setTitle(getString(R.string.title_badge));

        TextView modelName = (TextView) findViewById(R.id.textCBModelName);
        TextView badgeCounter = (TextView) findViewById(R.id.textCBBadgeCounter);
        ImageView modelImage = (ImageView) findViewById(R.id.imgCBModel);

        badgeListView = (ListView) findViewById(R.id.listBadges);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){

            Model data = incomingData.getParcelable("MODEL");

            badgeList = new ArrayList<>(databaseHelper.getData("BADGE", new String[]{"MODEL_NAME"}, new String[]{data.getName()}).values());

            Log.e("Got:",data.getName());

            dataAdapter = new SQLiteDataAdapter(this, badgeList);

            badgeListView.setAdapter(dataAdapter);

            modelName.setText(data.getBrandName() + " " + data.getName());

            brandName = data.getBrandName();

            Picasso.with(this).load(Utility.getImageAddress(data.getBrandName() + "_" + data.getName())).into(modelImage);

            badgeCounter.setText(getString(R.string.ui_badge_available_badge) + badgeList.size());

        }

        badgeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Badge selectedBadge = (Badge) badgeList.get(position);
                Intent intent = new Intent(ConfiguratorBadge.this, Previewer.class);
                intent.putExtra("BADGE",selectedBadge);
                intent.putExtra("BRAND", brandName );
                startActivity(intent);
            }
        });

    }
}
