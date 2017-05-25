package yuqi.amc;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import yuqi.amc.JsonData.Config;

public class ExplorerFragment extends android.app.Fragment {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);

        sectionsPagerAdapter = new SectionsPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) view.findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return PreConfigListFragment.newInstance("LATEST");
                case 1:
                    return PreConfigListFragment.newInstance("HOT");
                case 2:
                    return PreConfigListFragment.newInstance("UNIQUE");
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LATEST";
                case 1:
                    return "HOT";
                case 2:
                    return "UNIQUE";
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_title_explorer));
    }

    public static class PreConfigListFragment extends Fragment {

        private ListView configListView;
        private ArrayList<Config> configList;
        private String currentType;


        public static PreConfigListFragment newInstance(String type) {
            PreConfigListFragment preConfigListFragment = new PreConfigListFragment();
            Bundle args = new Bundle();
            args.putString("ConfigType", type);
            preConfigListFragment.setArguments(args);
            return preConfigListFragment;
        }


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            Bundle args = getArguments();
            currentType = args.getString("ConfigType");
            Log.e("Get", currentType);

            View view = inflater.inflate(R.layout.fragment_preconfig, container, false);
            configListView = (ListView) view.findViewById(R.id.listPreconfig);

            configListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_preconfig_detail, null);
                    ImageView imgDetailImage = (ImageView) dialogView.findViewById(R.id.imgPreconfigDetailImage);
                    TextView labelDetailName = (TextView) dialogView.findViewById(R.id.labelPreconfigDetailName);
                    TextView labelDetailHighlight = (TextView) dialogView.findViewById(R.id.labelPreconfigDetailHighlight);
                    TextView labelDetailContent = (TextView) dialogView.findViewById(R.id.labelPreconfigDetailContent);

                    Config selectedConfig = configList.get(position);

                    labelDetailName.setText(selectedConfig.getName());
                    labelDetailHighlight.setText(selectedConfig.getHighlight());
                    labelDetailContent.setText(selectedConfig.toString());

                    Picasso.with(getContext())
                            .load(Utility.getImageAddress("config_" + (selectedConfig.getId())))
                            .placeholder(R.drawable.img_placeholder_block)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(imgDetailImage);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.e("Have", currentType);
            new FetchPreConfig().execute(currentType);
        }

        class FetchPreConfig extends AsyncTask<String,Void,String>{
            @Override
            protected String doInBackground(String... params) {
                return RestClient.requestData("config",params);
            }

            @Override
            protected void onPostExecute(String result) {
                JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getContext(), result, JsonDataAdapter.JsonDataType.CONFIG);
                configList = (ArrayList<Config>)((ArrayList<?>)jsonDataAdapter.getDataList());
                configListView.setAdapter(jsonDataAdapter);
            }
        }
    }
}
