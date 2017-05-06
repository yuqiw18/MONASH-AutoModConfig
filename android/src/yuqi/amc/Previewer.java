package yuqi.amc;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;


import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import java.util.ArrayList;

import yuqi.amc.SqliteData.Badge;
import yuqi.amc.SqliteData.DataStruct;
import yuqi.amc.SqliteData.Part;

public class Previewer extends FragmentActivity implements AndroidFragmentApplication.Callbacks, OnClickListener {

    private ListView partListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> partList;
    private static String[] COLUMNS = {"PART_TYPE","MODEL_NAME","BADGE_NAME"};

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

    private TextView textRespray;
    private TextView textBumper;
    private TextView textBonnet;
    private TextView textSpoiler;
    private TextView textExhaust;
    private TextView textSuspension;
    private TextView textBrake;
    private TextView textRim;
    private TextView textTyre;
    private TextView textLighting;

    private TextView sectionHeader;

    private Badge data;
    private String brandName = null;

    private OnPartSelectListener onPartSelectListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_previewer);

        // UI-related variables
        textRespray = (TextView) findViewById(R.id.textRespray);
        textBumper = (TextView) findViewById(R.id.textBumper);
        textBonnet = (TextView) findViewById(R.id.textBonnet);
        textSpoiler = (TextView) findViewById(R.id.textSpoiler);
        textExhaust = (TextView) findViewById(R.id.textExhaust);
        textSuspension = (TextView) findViewById(R.id.textSuspension);
        textBrake = (TextView) findViewById(R.id.textBrake);
        textRim = (TextView) findViewById(R.id.textRim);
        textTyre = (TextView) findViewById(R.id.textTyre);
        textLighting = (TextView) findViewById(R.id.textLighting);

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

        onPartSelectListener = (OnPartSelectListener) getSupportFragmentManager().findFragmentById(R.id.fragmentRenderer);

        // Listview and database-related variables
        databaseHelper = new DatabaseHelper(getApplicationContext());

        partList = new ArrayList<>(databaseHelper.getData("PART", new String[]{"PART_TYPE"}, new String[]{"Respray"}).values());
        partListView = (ListView) findViewById(R.id.listParts);

        dataAdapter = new DataAdapter(this, partList);
        partListView.setAdapter(dataAdapter);

        resetText();
        sectionHeader.setText("RESPRAY");
        textRespray.setTypeface(null, Typeface.BOLD);

        Bundle incomingData = getIntent().getExtras();

        if (incomingData!=null){
            data = incomingData.getParcelable("BADGE");
            brandName = incomingData.getString("BRAND");
            Log.e("Badge:", data.getName());
        }

        partListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Part selectedPart = (Part) partList.get(position);
                onPartSelectListener.updateScene(selectedPart);
            }
        });

        // Renderer-related functions should run on another thread
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onPartSelectListener.setupScene(Utility.stringConvert(brandName + "_" + data.getModelName() + "_" + data.getName()));
            }
        }, 500);
    }

//    @Override
//    public void onBackPressed() {
//        getSupportFragmentManager().beginTransaction().remove(rendererFragment).commit();
//        super.onBackPressed();
//    }

    @Override
    public void exit() {}


    @Override
    public void onClick(View v) {

        String badge = data.getName();
        String model = data.getModelName();

        switch (v.getId()){
            case R.id.btnRespray:
                resetText();
                sectionHeader.setText("RESPRAY");
                textRespray.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", new String[]{"PART_TYPE"}, new String[]{"Respray"}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBumper:
                resetText();
                sectionHeader.setText("BUMPER");
                textBumper.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Bumper",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBonnet:
                resetText();
                sectionHeader.setText("BONNET");
                textBonnet.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Bonnet",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnSpoiler:
                resetText();
                sectionHeader.setText("SPOILER");
                textSpoiler.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Spoiler",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnExhaust:
                resetText();
                sectionHeader.setText("EXHAUST");
                textExhaust.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Exhaust",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnSuspension:
                resetText();
                sectionHeader.setText("SUSPENSION");
                textSuspension.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Suspension",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBrake:
                resetText();
                sectionHeader.setText("BRAKE");
                textBrake.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Brake",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnRim:
                resetText();
                sectionHeader.setText("RIM");
                textRim.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Rim",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnTyre:
                resetText();
                sectionHeader.setText("TYRE");
                textTyre.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Tyre",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnLighting:
                resetText();
                sectionHeader.setText("LIGHTING");
                textLighting.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Lighting",model,badge}).values());
                dataAdapter = new DataAdapter(this, partList);
                partListView.setAdapter(dataAdapter);
                break;
            default:
                break;
        }
    }

    public void resetText(){
        textRespray.setTypeface(null, Typeface.NORMAL);
        textBumper.setTypeface(null, Typeface.NORMAL);
        textBonnet.setTypeface(null, Typeface.NORMAL);
        textSpoiler.setTypeface(null, Typeface.NORMAL);
        textExhaust.setTypeface(null, Typeface.NORMAL);
        textSuspension.setTypeface(null, Typeface.NORMAL);
        textBrake.setTypeface(null, Typeface.NORMAL);
        textRim.setTypeface(null, Typeface.NORMAL);
        textTyre.setTypeface(null, Typeface.NORMAL);
        textLighting.setTypeface(null, Typeface.NORMAL);
    }

    public interface OnPartSelectListener{

        void updateScene(Part part);

        void setupScene(String name);

    }

}
