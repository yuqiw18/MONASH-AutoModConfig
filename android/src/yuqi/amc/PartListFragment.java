package yuqi.amc;

import yuqi.amc.DataStruct.DataStruct;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;

public class PartListFragment extends Fragment implements OnClickListener {

    private ListView partListView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataStruct> partList;

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

    public PartListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_part_list, container, false);

        // Listview and database-related variables
        databaseHelper = new DatabaseHelper(this.getContext());
        partList = new ArrayList<>(databaseHelper.getData("PART", null, null).values());
        partListView = (ListView) view.findViewById(R.id.listParts);
        dataAdapter = new DataAdapter(getContext(), partList);
        partListView.setAdapter(dataAdapter);

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

        return view;
        // Initialise all the variables
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRespray:
                resetText();
                sectionHeader.setText("RESPRAY");
                textRespray.setTypeface(null, Typeface.BOLD);
                partList = new ArrayList<>(databaseHelper.getData("PART", "PART_TYPE", new String[]{"Respray"}).values());
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
