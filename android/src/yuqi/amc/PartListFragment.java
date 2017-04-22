package yuqi.amc;

import yuqi.amc.DataStruct.Badge;
import yuqi.amc.DataStruct.Brand;
import yuqi.amc.DataStruct.DataStruct;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartListFragment extends Fragment implements OnClickListener {

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


    public PartListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_part_list, container, false);

        // UI-related variables
        textRespray = (TextView) view.findViewById(R.id.textRespray);
        textBumper = (TextView) view.findViewById(R.id.textBumper);
        textBonnet = (TextView) view.findViewById(R.id.textBonnet);
        textSpoiler = (TextView) view.findViewById(R.id.textSpoiler);
        textExhaust = (TextView) view.findViewById(R.id.textExhaust);
        textSuspension = (TextView) view.findViewById(R.id.textSuspension);
        textBrake = (TextView) view.findViewById(R.id.textBrake);
        textRim = (TextView) view.findViewById(R.id.textRim);
        textTyre = (TextView) view.findViewById(R.id.textTyre);
        textLighting = (TextView) view.findViewById(R.id.textLighting);

        sectionHeader = (TextView) view.findViewById(R.id.sectionHeader);

        btnRespray = (ImageButton) view.findViewById(R.id.btnRespray);
        btnRespray.setOnClickListener(this);
        btnBumper = (ImageButton) view.findViewById(R.id.btnBumper);
        btnBumper.setOnClickListener(this);
        btnBonnet = (ImageButton) view.findViewById(R.id.btnBonnet);
        btnBonnet.setOnClickListener(this);
        btnSpoiler = (ImageButton) view.findViewById(R.id.btnSpoiler);
        btnSpoiler.setOnClickListener(this);
        btnExhaust = (ImageButton) view.findViewById(R.id.btnExhaust);
        btnExhaust.setOnClickListener(this);
        btnSuspension = (ImageButton) view.findViewById(R.id.btnSuspension);
        btnSuspension.setOnClickListener(this);
        btnBrake = (ImageButton) view.findViewById(R.id.btnBrake);
        btnBrake.setOnClickListener(this);
        btnRim = (ImageButton) view.findViewById(R.id.btnRim);
        btnRim.setOnClickListener(this);
        btnTyre = (ImageButton) view.findViewById(R.id.btnTyre);
        btnTyre.setOnClickListener(this);
        btnLighting = (ImageButton) view.findViewById(R.id.btnLighting);
        btnLighting.setOnClickListener(this);

        // Listview and database-related variables
        databaseHelper = new DatabaseHelper(this.getContext());

        partList = new ArrayList<>(databaseHelper.getData("PART", new String[]{"PART_TYPE"}, new String[]{"Respray"}).values());
        partListView = (ListView) view.findViewById(R.id.listParts);

        dataAdapter = new DataAdapter(getContext(), partList);
        partListView.setAdapter(dataAdapter);

        resetText();
        sectionHeader.setText("RESPRAY");
        textRespray.setTypeface(null, Typeface.BOLD);

        Bundle incomingData = getActivity().getIntent().getExtras();

        if (incomingData!=null){
            data = incomingData.getParcelable("BADGE");
            Log.e("Badge:", data.getName());
        }


        partListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            }
        });

        return view;
        // Initialise all the variables
    }


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
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBumper:
                resetText();
                sectionHeader.setText("BUMPER");
                textBumper.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Bumper",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBonnet:
                resetText();
                sectionHeader.setText("BONNET");
                textBonnet.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Bonnet",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnSpoiler:
                resetText();
                sectionHeader.setText("SPOILER");
                textSpoiler.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Spoiler",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnExhaust:
                resetText();
                sectionHeader.setText("EXHAUST");
                textExhaust.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Exhaust",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnSuspension:
                resetText();
                sectionHeader.setText("SUSPENSION");
                textSuspension.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Suspension",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnBrake:
                resetText();
                sectionHeader.setText("BRAKE");
                textBrake.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Brake",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnRim:
                resetText();
                sectionHeader.setText("RIM");
                textRim.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Rim",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnTyre:
                resetText();
                sectionHeader.setText("TYRE");
                textTyre.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Tyre",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
                break;
            case R.id.btnLighting:
                resetText();
                sectionHeader.setText("LIGHTING");
                textLighting.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", COLUMNS, new String[]{"Lighting",model,badge}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
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




}
