package yuqi.amc;

import android.app.Application;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

// Global setting that overrides the onCreate method
// This method provides local caching function for Picasso
// Reference: https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
public class GlobalSetting extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
