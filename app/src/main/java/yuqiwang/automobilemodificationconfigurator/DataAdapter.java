package yuqiwang.automobilemodificationconfigurator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;

/**
 * Created by ClayW on 14/04/2017.
 */

public class DataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Brand> brandList;

    public DataAdapter(Context context, ArrayList<Brand> brandList){
        this.context = context;
        this.brandList = brandList;
    }

    @Override
    public int getCount() {
        return brandList.size();
    }

    @Override
    public Object getItem(int position) {
        return brandList.get(position);
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
            // Create a list item based off layout definition
            convertView = inflater.inflate(R.layout.list_brand_item, null);
        }

        // Assign values to the TextViews using Person Object
        TextView brandName = (TextView) convertView.findViewById(R.id.textBrandName);

        // Following is how we assign values to the cell in the list view
        brandName.setText(brandList.get(position).getName());

        return convertView;
    }
}
