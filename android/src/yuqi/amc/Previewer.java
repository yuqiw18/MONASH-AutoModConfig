package yuqi.amc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class Previewer extends FragmentActivity implements AndroidFragmentApplication.Callbacks {

    private FragmentManager fragmentManager;
    private Fragment rendererFragment;
    private Fragment partListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_previewer);

        fragmentManager = getSupportFragmentManager();
        rendererFragment = new RendererFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentRenderer, rendererFragment).commit();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().remove(rendererFragment).commit();
        super.onBackPressed();
    }

    @Override
    public void exit() {}


}
