package yuqi.amc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import yuqi.amc.JsonData.Part;
import yuqi.amc.Previewer.OnPartSelectListener;

public class RendererFragment extends AndroidFragmentApplication implements OnPartSelectListener, RendererV2.RendererStateListener {

    private RendererV2 renderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new RendererV2();
        renderer.setRendererStateListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initializeForView(renderer);
    }

    @Override
    public void updateScene(Part part) {
        renderer.updateScene(part.getType(),part.getValue());
    }

    @Override
    public void setupScene(String name) {
        renderer.loadModels(name);
    }

    @Override
    public void onRendererLoaded() {

    }

    @Override
    public void onRendererReload() {

    }
    
    public interface RendererFragmentStateListener{

        void onFragmentReady();

    }
}
