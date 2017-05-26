package yuqi.amc;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yuqi.amc.JsonData.Order;
import yuqi.amc.JsonData.Tracking;

/**
 * Created by yuqi on 25/5/17.
 */

public class DetailTrackingDialogFragment extends DialogFragment {

    private Order selectedOrder;
    private TextView trackingRecords;
    private TextView trackingId;
    private TextView trackingCourier;
    private TextView orderDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();

        String id = String.valueOf(args.getLong("OrderId"));

        String detail = args.getString("Detail");

        View view = inflater.inflate(R.layout.dialog_tracking, null);

        trackingRecords = (TextView) view.findViewById(R.id.labelTrackingDetail);
        trackingId = (TextView) view.findViewById(R.id.labelTrackingId);
        trackingCourier = (TextView) view.findViewById(R.id.labelTrackingCourier);
        orderDetail = (TextView) view.findViewById(R.id.labelTrackingOrderDetail);

        orderDetail.setText(getFormattedDetail(detail));

        new FetchTrackingRecord().execute(id);

        return view;
    }

    private class FetchTrackingRecord extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.requestData("tracking", params);
        }

        @Override
        protected void onPostExecute(String result) {
            Tracking tracking = Tracking.strJsonToTracking(result);
            trackingRecords.setText(tracking.getFormattedDetail());
            trackingId.setText(getString(R.string.dialog_tracking_id) + tracking.getId());
            trackingCourier.setText(getString(R.string.dialog_tracking_courier) + tracking.getCourier());
        }
    }

    private String getFormattedDetail(String detail){

        String formatted = "";

        String items[] = detail.split(";");

        for (int i = 0 ; i < items.length -1 ; i ++ ){

            String item[] = items[i].split(",");

            formatted += item[1] + ":" + Utility.getFormattedPrice(item[3]) + "\n";
        }

        return formatted;
    }
    
}
