package yuqi.amc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import yuqi.amc.JsonData.Part;
import yuqi.amc.Previewer.OnPartSelectListener;

public class RendererFragment extends AndroidFragmentApplication implements OnPartSelectListener, Renderer.RendererStateListener {

    private Renderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new Renderer();
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
}
