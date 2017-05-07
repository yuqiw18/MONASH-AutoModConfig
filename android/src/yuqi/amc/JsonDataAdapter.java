package yuqi.amc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;

import yuqi.amc.JsonData.Part;

public class JsonDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> dataList;
    private JsonDataType dataType;

    // Customer is one of the JsonData but it will not be used in ListView therefore it is not included here
    public enum JsonDataType {BOOKING, ORDER, PART, SERVICING, TRACKING, PAYMENT}

    // Viewholder pattern
    private static class ViewHolder{
        TextView textViewPlaceHolder1;
        TextView textViewPlaceHolder2;
        TextView textViewPlaceHolder3;
        TextView textViewPlaceHolder4;
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
                    case BOOKING:

                        break;
                    case ORDER:

                        break;
                    case PART:
                        dataList.add(Part.jsonToPart(jsonObject));
                        break;
                    case SERVICING:

                        break;
                    case TRACKING:

                        break;
                    case PAYMENT:

                        break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
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

                    break;
                case ORDER:

                    break;
                case PART:
                    convertView = inflater.inflate(R.layout.list_part_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolder1 = (TextView) convertView.findViewById(R.id.textPartName);
                    viewHolder.textViewPlaceHolder2 = (TextView) convertView.findViewById(R.id.textPartPrice);
                    convertView.setTag(viewHolder);
                    break;
                case SERVICING:

                    break;
                case TRACKING:

                    break;
                case PAYMENT:

                    break;
            }

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        switch (dataType){
            case BOOKING:
                break;
            case ORDER:

                break;
            case PART:
                viewHolder.textViewPlaceHolder1.setText(((Part)dataList.get(position)).getName());
                viewHolder.textViewPlaceHolder2.setText(String.valueOf(((Part)dataList.get(position)).getPrice()));
                break;
            case SERVICING:
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
