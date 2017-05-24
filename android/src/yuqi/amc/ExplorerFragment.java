package yuqi.amc;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

            Fragment preConfigListFragment = new PreConfigListFragment();

            Bundle args = new Bundle();

            switch (position){
                case 0:
                    Log.e("Pos", "0");
                    args.putString("ConfigType", "LATEST");
                    preConfigListFragment.setArguments(args);
                    break;
                case 1:
                    Log.e("Pos", "1");
                    args.putString("ConfigType", "HOT");
                    preConfigListFragment.setArguments(args);
                    break;
                case 2:
                    Log.e("Pos", "2");
                    args.putString("ConfigType", "UNIQUE");
                    preConfigListFragment.setArguments(args);
                    break;
            }

            return preConfigListFragment;
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

        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            Bundle args = getArguments();
            currentType = args.getString("ConfigType");
            View view = inflater.inflate(R.layout.fragment_preconfig, container, false);
            configListView = (ListView) view.findViewById(R.id.listPreconfig);

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
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
