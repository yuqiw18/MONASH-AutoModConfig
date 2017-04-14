package yuqiwang.automobilemodificationconfigurator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import yuqiwang.automobilemodificationconfigurator.DataStruct.DataStruct;

/**
 * Created by ClayW on 14/04/2017.
 */

public class DataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataStruct> dataList;
    private String identifier;

    public DataAdapter(Context context, ArrayList<DataStruct> dataList){
        this.context = context;
        this.dataList = dataList;
        this.identifier = this.context.getClass().getSimpleName();
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
        // Check if view already exists. If not inflate it
        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (identifier){

                case "ConfiguratorBrand":

                    convertView = inflater.inflate(R.layout.list_brand_item, null);
                    TextView brandName = (TextView) convertView.findViewById(R.id.textBrandName);
                    ImageView brandLogo = (ImageView) convertView.findViewById(R.id.imgCMBrand);
                    brandName.setText(dataList.get(position).getName());
                    brandLogo.setImageResource(Utility.getResourceID(brandName.getText().toString(),R.drawable.class));
                    return convertView;
                case "ConfiguratorModel":
                    convertView = inflater.inflate(R.layout.list_model_item, null);
                    TextView modelName = (TextView) convertView.findViewById(R.id.textModelName);
                    modelName.setText(dataList.get(position).getName());
                    return convertView;

                case "ConfiguratorBadge":



                    return convertView;

                case "Previewer":

                    return convertView;

                default:

                    return convertView;

            }

        }

        return null;
    }
}
