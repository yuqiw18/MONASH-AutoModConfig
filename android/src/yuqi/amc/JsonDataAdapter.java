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
import org.w3c.dom.Text;

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

    // ViewHolder pattern
    private static class ViewHolder{
        ArrayList<TextView> textViewPlaceHolders;
        TextView textViewPlaceHolder1;
        TextView textViewPlaceHolder2;
        TextView textViewPlaceHolder3;
        TextView textViewPlaceHolder4;

        protected ViewHolder(){
            textViewPlaceHolders = new ArrayList<>();
        }

        protected void addTextPlaceHolder(TextView textView){
            textViewPlaceHolders.add(textView);
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
                    viewHolder.textViewPlaceHolder1 = (TextView) convertView.findViewById(R.id.labelOrderName);
                    viewHolder.textViewPlaceHolder2 = (TextView) convertView.findViewById(R.id.labelOrderDatetime);
                    viewHolder.textViewPlaceHolder3 = (TextView) convertView.findViewById(R.id.labelOrderPrice);
                    viewHolder.textViewPlaceHolder4 = (TextView) convertView.findViewById(R.id.labelOrderStatus);
                    convertView.setTag(viewHolder);
                    break;
                case PART:
                    convertView = inflater.inflate(R.layout.list_part_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolder1 = (TextView) convertView.findViewById(R.id.textPartName);
                    viewHolder.textViewPlaceHolder2 = (TextView) convertView.findViewById(R.id.textPartPrice);
                    convertView.setTag(viewHolder);
                    break;
                case CENTER:
                    convertView = inflater.inflate(R.layout.list_service_center_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolder1 = (TextView) convertView.findViewById(R.id.labelCenterName);
                    viewHolder.textViewPlaceHolder2 = (TextView) convertView.findViewById(R.id.labelInstallationFee);
                    viewHolder.textViewPlaceHolder3 = (TextView) convertView.findViewById(R.id.labelCenterRanking);
                    viewHolder.textViewPlaceHolder4 = (TextView) convertView.findViewById(R.id.labelCenterDistance);
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
                viewHolder.textViewPlaceHolder1.setText("ORDER ID:" + ((Order)dataList.get(position)).getId());
                viewHolder.textViewPlaceHolder2.setText(String.valueOf(((Order)dataList.get(position)).getDatetime()));
                viewHolder.textViewPlaceHolder4.setText(String.valueOf(((Order)dataList.get(position)).getPrice()));
                viewHolder.textViewPlaceHolder3.setText("TEST");
                break;
            case PART:
                viewHolder.textViewPlaceHolder1.setText(((Part)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolder2.setText(String.valueOf(((Part)dataList.get(position)).getPrice()));
                break;
            case CENTER:
                viewHolder.textViewPlaceHolder1.setText(((Center)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolder2.setText("Installation Fee from AU$ " + ((Center)dataList.get(position)).getPrice());
                double avgScore = ((Center) dataList.get(position)).getAvgScore();
                if (avgScore!=-1d){
                    viewHolder.textViewPlaceHolder3.setText("Rating: " + avgScore);
                }else {
                    viewHolder.textViewPlaceHolder3.setText("No Rating");
                }
                viewHolder.textViewPlaceHolder4.setText(((Center)dataList.get(position)).getDistance() + "Km");
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
