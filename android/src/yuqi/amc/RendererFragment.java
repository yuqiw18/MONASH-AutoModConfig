package yuqi.amc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;


public class RendererFragment extends AndroidFragmentApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_renderer, container, false);
//
//        return view;
        //return inflater.inflate(R.layout.fragment_renderer, container, false);


        return initializeForView(new Renderer());

    }

}
