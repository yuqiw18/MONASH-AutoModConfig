package yuqi.amc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import yuqi.amc.JsonData.Part;
import yuqi.amc.Previewer.OnPartSelectListener;

public class RendererFragment extends AndroidFragmentApplication implements OnPartSelectListener, RendererV2.RendererStateListener {

    private RendererV2 renderer;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialise the renderer
        renderer = new RendererV2();
        renderer.setRendererStateListener(this);

        // Display a loading dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_previewer_loading, null);
        builder.setView(dialogView);
        alertDialog = builder.create();

        // No operation can be taken during this process (Locked)
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialise the renderer for the view
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
    public void onRendererLoad() {
        // Dismiss the loading screen upon completion
        alertDialog.dismiss();
    }
}
