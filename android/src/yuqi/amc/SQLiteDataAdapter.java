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
import java.util.ArrayList;

import yuqi.amc.SQLiteData.Badge;
import yuqi.amc.SQLiteData.Brand;
import yuqi.amc.SQLiteData.DataStruct;
import yuqi.amc.SQLiteData.Model;

// DataAdapter for SQLite Persistent Data
public class SQLiteDataAdapter extends BaseAdapter implements Callback {

    private Context context;
    private ArrayList<DataStruct> dataList;
    private String identifier;
    private DatabaseHelper databaseHelper;

    public SQLiteDataAdapter(Context context, ArrayList<DataStruct> dataList){
        this.context = context;
        this.dataList = dataList;
        this.identifier = this.context.getClass().getSimpleName();
        databaseHelper = new DatabaseHelper(context);
    }

    // Expandable View Holder (TextView)
    private static class ViewHolder{
        ArrayList<TextView> textViewPlaceHolders;
        ImageView imageViewPlaceHolder;
        public ViewHolder(){
            textViewPlaceHolders = new ArrayList<>();
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

            switch (identifier){

                case "ConfiguratorBrand":
                    convertView = inflater.inflate(R.layout.list_brand_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textBrandName));
                    viewHolder.imageViewPlaceHolder = (ImageView) convertView.findViewById(R.id.imgCMBrand);
                    convertView.setTag(viewHolder);
                    break;
                case "ConfiguratorModel":
                    convertView = inflater.inflate(R.layout.list_model_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textModelName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textBodyType));
                    viewHolder.imageViewPlaceHolder = (ImageView) convertView.findViewById(R.id.imgModel);
                    convertView.setTag(viewHolder);
                    break;
                case "ConfiguratorBadge":
                    convertView = inflater.inflate(R.layout.list_badge_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textBadgeName));
                    viewHolder.textViewPlaceHolders.add((TextView) convertView.findViewById(R.id.textPartNum));
                    convertView.setTag(viewHolder);
                    break;
                default:
                    return convertView;
            }

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (identifier){

            case "ConfiguratorBrand":
                final Brand brand = (Brand) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(brand.getName());
                Picasso.with(context)
                        .load(Utility.getImageAddress(brand.getName()))
                        .placeholder(R.drawable.img_placeholder)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.imageViewPlaceHolder, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.e("Picasso", "Loaded Locally");
                            }
                            @Override
                            public void onError() {
                                Picasso.with(context)
                                    .load(Utility.getImageAddress(brand.getName()))
                                    .placeholder(R.drawable.img_placeholder)
                                    .into(viewHolder.imageViewPlaceHolder, this);
                            }
                        });
                break;
            case "ConfiguratorModel":
                final Model model = (Model) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(model.getName());
                viewHolder.textViewPlaceHolders.get(1).setText(model.getBodyType() + " " +model.getDriveType());
                Picasso.with(context)
                        .load(Utility.getImageAddress(model.getBrandName()+"_"+model.getName()))
                        .placeholder(R.drawable.img_placeholder_wide)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.imageViewPlaceHolder, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.e("Picasso", "Loaded Locally");
                            }
                            @Override
                            public void onError() {
                                Picasso.with(context)
                                    .load(Utility.getImageAddress(model.getBrandName()+"_"+model.getName()))
                                    .placeholder(R.drawable.img_placeholder_wide)
                                    .into(viewHolder.imageViewPlaceHolder, this);
                            }
                        });
                break;
            case "ConfiguratorBadge":
                final Badge badge = (Badge) dataList.get(position);
                viewHolder.textViewPlaceHolders.get(0).setText(badge.getName());
                //viewHolder.textViewPlaceHolder2.setText("Available Parts: " + databaseHelper.countParts(new String[]{((Badge)dataList.get(position)).getModelName(),((Badge)dataList.get(position)).getName()}));
                break;
            default:
                return convertView;
        }
        return convertView;
    }

    // Picasso Callbacks
    @Override
    public void onSuccess() {
        Log.i("Picasso","Downloaded");
    }

    @Override
    public void onError() {
        Log.i("Picasso", "Could not load image.");
    }
}
