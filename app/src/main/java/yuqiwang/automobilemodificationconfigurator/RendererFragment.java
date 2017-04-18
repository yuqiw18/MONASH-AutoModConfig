package yuqiwang.automobilemodificationconfigurator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.ScaleAnimation3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class RendererFragment extends Fragment {

    private Renderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_renderer, container, false);
        final RajawaliSurfaceView surface = (RajawaliSurfaceView) view.findViewById(R.id.rajawali_surface);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_CONTINUOUSLY);
        renderer = new Renderer(this.getContext());
        surface.setSurfaceRenderer(renderer);

        view.setOnClickListener(new View.OnClickListener() {
            boolean zoomedOut = false;

            @Override
            public void onClick(View v) {
                if(zoomedOut) {
                    renderer.zoomOut();
                    zoomedOut = false;
                }
                else {
                    renderer.zoomIn();
                    zoomedOut = true;
                }
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_renderer, container, false);
    }

    @Override
    public void onDestroy() {
        renderer.stopRendering();
        super.onDestroy();
    }
}
