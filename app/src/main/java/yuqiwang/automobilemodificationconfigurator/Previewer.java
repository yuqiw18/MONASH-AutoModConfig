package yuqiwang.automobilemodificationconfigurator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

public class Previewer extends FragmentActivity {


    private FragmentManager fragmentManager;
    private Fragment rendererFragment;
    private Fragment partListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
