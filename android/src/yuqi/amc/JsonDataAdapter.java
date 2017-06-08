package yuqi.amc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import yuqi.amc.JsonData.Config;
import yuqi.amc.JsonData.Order;
import yuqi.amc.JsonData.Part;
import yuqi.amc.JsonData.Center;

// DataAdapter for process json data
public class JsonDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> dataList;
    private JsonDataType dataType;

    // Customer and Tracking are JsonData but they will not be used in ListView therefore it is not included here
    public enum JsonDataType {CONFIG, ORDER, PART, CENTER}

    // ViewHolder pattern - Expandable
    private static class ViewHolder{
        ArrayList<TextView> textViewPlaceHolders;
        ImageView imagePlaceHolder;
        public ViewHolder(){
            textViewPlaceHolders = new ArrayList<>();
        }
    }

    public JsonDataAdapter(Context context, String json, JsonDataType dataType){
        this.context = context;
        this.dataType = dataType;
        this.dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i ++){
                //String jsonObj = jsonArray.getJSONObject(i).toString();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                switch (this.dataType){
                    case CONFIG:
                        dataList.add(Config.jsonToConfig(jsonObject));
                        break;
                    case ORDER:
                        dataList.add(Order.jsonToOrder(jsonObject));
                        break;
                    case PART:
                        dataList.add(Part.jsonToPart(jsonObject));
                        break;
                    case CENTER:
                        dataList.add(Center.jsonToServiceCenter(jsonObject));
                        break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("JsonDataAdapter","No Data");
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        // Check if view already exists. If not inflate it
        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Set up the view holder based on the required content
            switch (dataType) {
                case CONFIG:
                    convertView = inflater.inflate(R.layout.list_config_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelConfigName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelConfigHighlight));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelConfigBudget));
                    viewHolder.imagePlaceHolder = (ImageView) convertView.findViewById(R.id.imgConfigImage);
                    convertView.setTag(viewHolder);
                    break;
                case ORDER:
                    convertView = inflater.inflate(R.layout.list_order_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelOrderName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelOrderDatetime));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelOrderPrice));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelOrderStatus));
                    convertView.setTag(viewHolder);
                    break;
                case PART:
                    convertView = inflater.inflate(R.layout.list_part_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartDescription));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartPrice));
                    convertView.setTag(viewHolder);
                    break;
                case CENTER:
                    convertView = inflater.inflate(R.layout.list_service_center_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelCenterName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelInstallationFee));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelCenterRanking));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelCenterDistance));
                    convertView.setTag(viewHolder);
                    break;
                default:
                    return convertView;
            }

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fill in the actual contents
        switch (dataType){
            case CONFIG:
                final Config config = (Config) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(config.getName());
                viewHolder.textViewPlaceHolders.get(1).setText(config.getHighlight());
                viewHolder.textViewPlaceHolders.get(2).setText(context.getString(R.string.dialog_preconfig_budget) + config.getBudget());
                Picasso.with(context)
                        .load(Utility.getImageAddress("config_" + config.getId()))
                        .placeholder(R.drawable.img_placeholder_block)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.imagePlaceHolder, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("Picasso", "Loaded Locally");
                            }
                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(Utility.getImageAddress("config_" + config.getId()))
                                        .placeholder(R.drawable.img_placeholder_block)
                                        .into(viewHolder.imagePlaceHolder, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.i("Picasso","Downloaded");
                                            }
                                            @Override
                                            public void onError() {
                                                Log.i("Picasso", "Could not load image.");
                                            }
                                        });
                            }
                        });
                break;
            case ORDER:
                final Order order = (Order) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(context.getString(R.string.ui_order_id)+ order.getId());
                viewHolder.textViewPlaceHolders.get(1).setText(order.getLocalTime());
                viewHolder.textViewPlaceHolders.get(2).setText(Utility.getFormattedPrice(order.getPrice()));
                viewHolder.textViewPlaceHolders.get(3).setText(order.getStatus());
                break;
            case PART:
                final Part part = (Part) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(part.getName());
                viewHolder.textViewPlaceHolders.get(1).setText(part.getDescription());
                viewHolder.textViewPlaceHolders.get(2).setText(Utility.getFormattedPrice(part.getPrice()));
                break;
            case CENTER:
                final Center center = (Center) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(center.getName());
                viewHolder.textViewPlaceHolders.get(1).setText(context.getString(R.string.dialog_map_install_from) + Utility.getFormattedPrice(center.getPrice()));
                double avgScore = center.getAvgScore();
                if (avgScore!=-1d){
                    viewHolder.textViewPlaceHolders.get(2).setText(context.getString(R.string.dialog_map_rating) + avgScore);
                }else {
                    viewHolder.textViewPlaceHolders.get(2).setText(context.getString(R.string.dialog_map_no_rating));
                }
                viewHolder.textViewPlaceHolders.get(3).setText(center.getDistance() + context.getString(R.string.unit_km));
                break;
        }
        return convertView;
    }

    public ArrayList<Object> getDataList(){
        return dataList;
    }

}
