package yuqi.amc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import yuqi.amc.DataStruct.DataStruct;

public class PartListFragment extends Fragment {

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
    private ImageButton btnRLighting;

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

        View view = inflater.inflate(R.layout.fragment_part_list, container, false);

        // Inflate the layout for this fragment
        databaseHelper = new DatabaseHelper(this.getContext());

        partList = new ArrayList<>(databaseHelper.getData("PART", null, null).values());
        partListView = (ListView) view.findViewById(R.id.listParts);
        dataAdapter = new DataAdapter(getContext(), partList);
        partListView.setAdapter(dataAdapter);

        sectionHeader = (TextView) view.findViewById(R.id.sectionHeader);

        btnRespray = (ImageButton) view.findViewById(R.id.btnRespray);
        btnRespray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionHeader.setText("RESPRAY");
                partList = new ArrayList<>(databaseHelper.getData("PART", "PART_TYPE", new String[]{"Respray"}).values());
                dataAdapter = new DataAdapter(getContext(), partList);
                partListView.setAdapter(dataAdapter);
            }
        });


        return view;
        // Initialise all the variables
    }




}
