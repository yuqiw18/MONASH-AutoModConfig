package yuqi.amc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    // Customer is one of the JsonData but it will not be used in ListView therefore it is not included here
    public enum JsonDataType {CONFIG, ORDER, PART, CENTER, TRACKING}

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
                    case TRACKING:

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
        ViewHolder viewHolder;

        // Check if view already exists. If not inflate it
        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            viewHolder = new ViewHolder();

            switch (dataType) {
                case CONFIG:
                    convertView = inflater.inflate(R.layout.list_config_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelConfigName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.labelConfigHighlight));
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
                case TRACKING:

                    convertView.setTag(viewHolder);
                    break;
                default:
                    return convertView;
            }

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (dataType){
            case CONFIG:
                viewHolder.textViewPlaceHolders.get(0).setText(((Config) dataList.get(position)).getName());
                viewHolder.textViewPlaceHolders.get(1).setText(((Config) dataList.get(position)).getHighlight());
//                Picasso.with(context)
//                        .load(Utility.getImageAddress("config_" + ((Config) dataList.get(position)).getId()))
//                        .placeholder(R.drawable.img_placeholder)
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .into(viewHolder.imagePlaceHolder, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                Log.e("Picasso", "Loaded Locally");
//                            }
//                            @Override
//                            public void onError() {
//                                Picasso.with(context)
//                                        .load(Utility.getImageAddress("config_" + ((Config) dataList.get(position)).getId()))
//                                        .placeholder(R.drawable.img_placeholder)
//                                        .into(viewHolder.imagePlaceHolder, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//                                                Log.e("Picasso","Downloaded");
//                                            }
//                                            @Override
//                                            public void onError() {
//                                                Log.e("Picasso", "Could not load image.");
//                                            }
//                                        });
//                            }
//                        });
                break;
            case ORDER:
                viewHolder.textViewPlaceHolders.get(0).setText("ORDER ID:" + ((Order)dataList.get(position)).getId());

                viewHolder.textViewPlaceHolders.get(1).setText(((Order)dataList.get(position)).getLocalTime());

                //viewHolder.textViewPlaceHolders.get(1).setText(String.valueOf(((Order)dataList.get(position)).getDatetime()));
                viewHolder.textViewPlaceHolders.get(2).setText(Utility.getFormattedPrice(((Order)dataList.get(position)).getPrice()));
                viewHolder.textViewPlaceHolders.get(3).setText(((Order) dataList.get(position)).getStatus());
                Log.e("View","Added");
                break;
            case PART:
                viewHolder.textViewPlaceHolders.get(0).setText(((Part)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolders.get(1).setText(((Part)dataList.get(position)).getDescription());
                viewHolder.textViewPlaceHolders.get(2).setText(Utility.getFormattedPrice(((Part)dataList.get(position)).getPrice()));
                break;
            case CENTER:
                viewHolder.textViewPlaceHolders.get(0).setText(((Center)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolders.get(1).setText("Installation Fee from AU" + Utility.getFormattedPrice(((Center)dataList.get(position)).getPrice()));
                double avgScore = ((Center) dataList.get(position)).getAvgScore();
                if (avgScore!=-1d){
                    viewHolder.textViewPlaceHolders.get(2).setText("Rating: " + avgScore);
                }else {
                    viewHolder.textViewPlaceHolders.get(2).setText("No Rating");
                }
                viewHolder.textViewPlaceHolders.get(3).setText(((Center)dataList.get(position)).getDistance() + "Km");
                break;
            case TRACKING:

                break;
        }
        return convertView;
    }

    public ArrayList<Object> getDataList(){
        return dataList;
    }

}
