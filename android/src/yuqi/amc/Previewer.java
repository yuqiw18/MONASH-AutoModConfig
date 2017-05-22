package yuqi.amc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import yuqi.amc.JsonDataAdapter.JsonDataType;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import java.util.ArrayList;

import yuqi.amc.SQLiteData.Badge;
import yuqi.amc.JsonData.Part;

public class Previewer extends AppCompatActivity implements AndroidFragmentApplication.Callbacks, OnClickListener {

    private ListView partListView;
    private ArrayList<Part> partList;

    private Cart cart;

    private ImageButton btnRespray;
    private ImageButton btnBumper;
    private ImageButton btnBonnet;
    private ImageButton btnSpoiler;
    private ImageButton btnExhaust;
    private ImageButton btnSuspension;
    private ImageButton btnBrake;
    private ImageButton btnRim;
    private ImageButton btnTyre;
    private ImageButton btnLighting;

    private TextView sectionHeader;
    private Badge data;
    private String brandName = null;

    private OnPartSelectListener onPartSelectListener;
    private SharedPreferences sharedPreferences;
    private String currentPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewer);

        setTitle(getString(R.string.title_previewer));

        sectionHeader = (TextView) findViewById(R.id.sectionHeader);

        btnRespray = (ImageButton) findViewById(R.id.btnRespray);
        btnRespray.setOnClickListener(this);
        btnBumper = (ImageButton) findViewById(R.id.btnBumper);
        btnBumper.setOnClickListener(this);
        btnBonnet = (ImageButton) findViewById(R.id.btnBonnet);
        btnBonnet.setOnClickListener(this);
        btnSpoiler = (ImageButton) findViewById(R.id.btnSpoiler);
        btnSpoiler.setOnClickListener(this);
        btnExhaust = (ImageButton) findViewById(R.id.btnExhaust);
        btnExhaust.setOnClickListener(this);
        btnSuspension = (ImageButton) findViewById(R.id.btnSuspension);
        btnSuspension.setOnClickListener(this);
        btnBrake = (ImageButton) findViewById(R.id.btnBrake);
        btnBrake.setOnClickListener(this);
        btnRim = (ImageButton) findViewById(R.id.btnRim);
        btnRim.setOnClickListener(this);
        btnTyre = (ImageButton) findViewById(R.id.btnTyre);
        btnTyre.setOnClickListener(this);
        btnLighting = (ImageButton) findViewById(R.id.btnLighting);
        btnLighting.setOnClickListener(this);

        cart = new Cart();

        onPartSelectListener = (OnPartSelectListener) getSupportFragmentManager().findFragmentById(R.id.fragmentRenderer);

        partListView = (ListView) findViewById(R.id.listParts);

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){
            data = incomingData.getParcelable("BADGE");
            brandName = incomingData.getString("BRAND");
            Log.e("Badge:", data.getName());
        }

        partListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                Part selectedPart = (Part) partListOLD.get(position);
                Part selectedPart = partList.get(position);
                cart.addToCart(selectedPart);
                onPartSelectListener.updateScene(selectedPart);
                Log.e("Item", String.valueOf(view.getId()));
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btnRespray.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));


        sectionHeader.setText(getString(R.string.ui_previewer_respray));

        currentPart = "Respray";

        new fetchPartList().execute("Respray",data.getModelName(),data.getName());

        // Renderer-related functions should run on another thread
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onPartSelectListener.setupScene(Utility.stringConvert(brandName + "_" + data.getModelName() + "_" + data.getName()));
            }
        }, 500);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_previewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // View matched students on map (if has related location records)
        if (id == R.id.menu_previewer_checkout) {
            if (cart.isCartEmpty()){
                Toast.makeText(getBaseContext(), getString(R.string.msg_checkout_no_item), Toast.LENGTH_LONG).show();
            }else {
                if (sharedPreferences.getBoolean("isSignedIn", false)){
                    Intent intent = new Intent(this, Checkout.class);
                    intent.putExtra("Cart", cart.getCart());
                    startActivity(intent);
                }else {
                    Toast.makeText(getBaseContext(), getString(R.string.msg_checkout_no_login), Toast.LENGTH_LONG).show();
                }
            }
        }else if (id == R.id.menu_previewer_save){


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void exit() {}

    @Override
    public void onClick(View v) {

        String badge = data.getName();
        String model = data.getModelName();

        partList = null;
        partListView.setAdapter(null);

        resetColor();

        switch (v.getId()){
            case R.id.btnRespray:
                sectionHeader.setText(getString(R.string.ui_previewer_respray));
                currentPart = "Respray";
                break;
            case R.id.btnBumper:
                sectionHeader.setText(getString(R.string.ui_previewer_bumper));
                currentPart = "Bumper";
                break;
            case R.id.btnBonnet:
                sectionHeader.setText(getString(R.string.ui_previewer_bonnet));
                currentPart = "Bonnet";
                break;
            case R.id.btnSpoiler:
                sectionHeader.setText(getString(R.string.ui_previewer_spoiler));
                currentPart = "Spoiler";
                break;
            case R.id.btnExhaust:
                sectionHeader.setText(getString(R.string.ui_previewer_exhaust));
                currentPart = "Exhaust";
                break;
            case R.id.btnSuspension:
                sectionHeader.setText(getString(R.string.ui_previewer_suspension));
                currentPart = "Suspension";
                break;
            case R.id.btnBrake:
                sectionHeader.setText(getString(R.string.ui_previewer_brake));
                currentPart = "Brake";
                break;
            case R.id.btnRim:
                sectionHeader.setText(getString(R.string.ui_previewer_rim));
                currentPart = "Rim";
                break;
            case R.id.btnTyre:
                sectionHeader.setText(getString(R.string.ui_previewer_tyre));
                currentPart = "Tyre";
                break;
            case R.id.btnLighting:
                sectionHeader.setText(getString(R.string.ui_previewer_lighting));
                currentPart = "Lighting";
                break;
            default:
                break;
        }
        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
        new fetchPartList().execute(currentPart,model,badge);
    }

    public interface OnPartSelectListener{
        void updateScene(Part part);
        void setupScene(String name);
    }

    private class fetchPartList extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return RestClient.requestData("part",params);
        }

        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getBaseContext(), result, JsonDataType.PART);
            partList = (ArrayList<Part>)((ArrayList<?>)jsonDataAdapter.getDataList());
            partListView.setAdapter(jsonDataAdapter);

            Long selectedItem = cart.getValue(currentPart);

            Log.e("Selected",String.valueOf(selectedItem));

            for (int i = 0; i < partList.size(); i ++){

                if (selectedItem != null && partList.get(i).getId() == selectedItem){

                    Log.e("Got Item", i + "_");

                    Log.e("FirstVisiblePosition",partListView.getFirstVisiblePosition() + "_");
                    Log.e("HeaderViewsCount",partListView.getHeaderViewsCount() + "_");

                    int firstPos = partListView.getFirstVisiblePosition() - partListView.getHeaderViewsCount();

                    Log.e("firstPos",firstPos + "_");

                    int childPos = i - firstPos;

                    Log.e("ChildCount",partListView.getChildCount() + "_");

//                    if (childPos < 0 || childPos >= partListView.getChildCount()){
//
//                        Log.e("??","??");
//
//                        return;
//                    }
//
//                    partListView.getChildAt(childPos).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
                }
            }
        }
    }

    private void resetColor(){

        btnRespray.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnBumper.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnBonnet.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnSpoiler.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnExhaust.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnSuspension.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnBrake.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnRim.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnTyre.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));
        btnLighting.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_out, null)));

    }

}
