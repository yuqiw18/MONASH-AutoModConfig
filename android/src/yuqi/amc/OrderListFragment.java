package yuqi.amc;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import yuqi.amc.JsonDataAdapter.JsonDataType;
import yuqi.amc.JsonData.Order;

// Class for displaying order history
public class OrderListFragment extends Fragment {

    private ListView orderListView;
    private ArrayList<Order> orderList;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        orderListView = (ListView) view.findViewById(R.id.listOrders);

        // Display the tracking information on item click
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Order selectedOrder = orderList.get(position);

                // Pass selected value to the dialog
                Bundle args = new Bundle();
                DetailTrackingDialogFragment detailTrackingDialogFragment = new DetailTrackingDialogFragment();

                args.putLong("OrderId", selectedOrder.getId());
                args.putString("Detail", selectedOrder.getDetail());
                detailTrackingDialogFragment.setArguments(args);

                // Show the tracking fragment with provided data
                detailTrackingDialogFragment.show(getFragmentManager(), "Tracking");

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new FetchOrderList().execute(String.valueOf(sharedPreferences.getLong("id", 0)));
    }

    // Method for retrieving order history
    private class FetchOrderList extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.requestData("transaction", params);
        }

        @Override
        protected void onPostExecute(String result) {
            JsonDataAdapter jsonDataAdapter = new JsonDataAdapter(getContext(), result, JsonDataType.ORDER);
            orderList = (ArrayList<Order>)((ArrayList<?>)jsonDataAdapter.getDataList());
            orderListView.setAdapter(jsonDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_order));
    }

}
