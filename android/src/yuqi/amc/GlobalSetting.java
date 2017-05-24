package yuqi.amc;

import android.app.Application;
import android.util.Log;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

// Global setting that overrides the onCreate method
public class GlobalSetting extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        // Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
