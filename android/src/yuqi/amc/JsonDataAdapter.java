package yuqi.amc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by ClayW on 7/05/2017.
 */

public class JsonDataAdapter extends BaseAdapter {

    private Context context;

    public JsonDataAdapter(Context context, String jsonArray){
        this.context = context;
    }
    
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
