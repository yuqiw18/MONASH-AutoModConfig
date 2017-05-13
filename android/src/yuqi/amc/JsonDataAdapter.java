package yuqi.amc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import yuqi.amc.JsonData.Order;
import yuqi.amc.JsonData.Part;
import yuqi.amc.JsonData.Center;

public class JsonDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> dataList;
    private JsonDataType dataType;
    private JsonAdapterMode mode;

    // Customer is one of the JsonData but it will not be used in ListView therefore it is not included here
    public enum JsonDataType {BOOKING, ORDER, PART, CENTER, TRACKING, PAYMENT}
    public enum JsonAdapterMode {CHECKOUT}

    // ViewHolder pattern - Expandable
    private static class ViewHolder{
        ArrayList<TextView> textViewPlaceHolders;
        public ViewHolder(){
            textViewPlaceHolders = new ArrayList<>();
        }
    }

    public JsonDataAdapter(Context context, String json, JsonDataType dataType, JsonAdapterMode mode ){
        this.context = context;
        this.dataType = dataType;
        this.dataList = new ArrayList<>();
        this.mode = mode;
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i ++){
                //String jsonObj = jsonArray.getJSONObject(i).toString();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                switch (this.dataType){
                    case BOOKING:

                        break;
                    case ORDER:
                        dataList.add(Order.jsonToOrder(jsonObject));
                        Log.e("Order","Fetched");
                        break;
                    case PART:
                        dataList.add(Part.jsonToPart(jsonObject));
                        break;
                    case CENTER:
                        dataList.add(Center.jsonToServiceCenter(jsonObject));
                        break;
                    case TRACKING:

                        break;
                    case PAYMENT:

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Check if view already exists. If not inflate it
        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            viewHolder = new ViewHolder();

            switch (dataType) {
                case BOOKING:
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
                    if (mode == JsonAdapterMode.CHECKOUT){
                        convertView = inflater.inflate(R.layout.list_cart_item, null);
                        viewHolder = new ViewHolder();
                        viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textCartName));
                        viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textCartPrice));
                        convertView.setTag(viewHolder);
                    }else {
                        convertView = inflater.inflate(R.layout.list_part_item, null);
                        viewHolder = new ViewHolder();
                        viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartName));
                        viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartDescription));
                        viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartPrice));
                        convertView.setTag(viewHolder);
                    }
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
                case PAYMENT:

                    convertView.setTag(viewHolder);
                    break;
            }

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        switch (dataType){
            case BOOKING:
                break;
            case ORDER:
                viewHolder.textViewPlaceHolders.get(0).setText("ORDER ID:" + ((Order)dataList.get(position)).getId());
                viewHolder.textViewPlaceHolders.get(1).setText(String.valueOf(((Order)dataList.get(position)).getDatetime()));
                viewHolder.textViewPlaceHolders.get(2).setText(String.valueOf(((Order)dataList.get(position)).getPrice()));
                viewHolder.textViewPlaceHolders.get(3).setText("TEST");
                Log.e("View","Added");
                break;
            case PART:
                if (mode == JsonAdapterMode.CHECKOUT){
                    viewHolder.textViewPlaceHolders.get(0).setText(((Part)dataList.get(position)).getName());
                    viewHolder.textViewPlaceHolders.get(1).setText(String.valueOf(((Part)dataList.get(position)).getPrice()));
                }else{
                    viewHolder.textViewPlaceHolders.get(0).setText(((Part)dataList.get(position)).getName());
                    viewHolder.textViewPlaceHolders.get(1).setText(((Part)dataList.get(position)).getDescription());
                    viewHolder.textViewPlaceHolders.get(2).setText("$ " + ((Part)dataList.get(position)).getPrice() +"0");
                }
                break;
            case CENTER:
                viewHolder.textViewPlaceHolders.get(0).setText(((Center)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolders.get(1).setText("Installation Fee from AU$ " + ((Center)dataList.get(position)).getPrice());
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
            case PAYMENT:

                break;
        }
        return convertView;
    }

    public ArrayList<Object> getDataList(){
        return dataList;
    }

}
