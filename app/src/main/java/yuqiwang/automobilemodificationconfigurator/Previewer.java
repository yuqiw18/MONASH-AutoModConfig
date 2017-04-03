package yuqiwang.automobilemodificationconfigurator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class Previewer extends RendererActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_previewer);
//    }

    private Object3dContainer faceObject3D;
    /** Called when the activity is first created. */
    @Override public void initScene()
    {
        scene.lights().add(new Light());
        scene.lights().add(new Light());
        Light myLight = new Light();
        myLight.position.setZ(150);
        scene.lights().add(myLight);
        IParser myParser = Parser.createParser(Parser.Type.OBJ, getResources(), String.valueOf(R.raw.avent_obj), true);
        myParser.parse();
        faceObject3D = myParser.getParsedObject();
        faceObject3D.position().x = faceObject3D.position().y = faceObject3D.position().z = 0;
        faceObject3D.scale().x = faceObject3D.scale().y = faceObject3D.scale().z = 1.0f;
        // Depending on the model you will need to change the scale faceObject3D.scale().x = faceObject3D.scale().y = faceObject3D.scale().z = 0.009f;        scene.addChild(faceObject3D);
    }
    @Override    public void updateScene() {
        faceObject3D.rotation().x += 0.5;
        faceObject3D.rotation().z += 1;
    }
}
