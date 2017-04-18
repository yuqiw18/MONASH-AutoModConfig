package yuqiwang.automobilemodificationconfigurator;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.animation.ScaleAnimation3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;


public class Renderer extends RajawaliRenderer {

    //public Context context;

    private DirectionalLight directionalLight;
    private Object3D object3D;

    private Animation3D zoomIn;
    private Animation3D zoomOut;

    private float x = 0;
    private float y = 0;
    private float z = 0;

    public Renderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    public void initScene(){

        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);

        getCurrentCamera().setX(10.0f);
        getCurrentCamera().setY(10.0f);
        getCurrentCamera().setZ(10.0f);
        getCurrentCamera().setLookAt(0,0,0);

        getCurrentScene().addLight(directionalLight);

        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.nissan_skyline_gtr_obj);

        try {
            objParser.parse();
            object3D = objParser.getParsedObject();
            getCurrentScene().addChild(object3D);

            zoomIn = new ScaleAnimation3D(new Vector3(2.0f,2.0f,2.0f));
            zoomIn.setDurationMilliseconds(2000);
            zoomOut = new ScaleAnimation3D(new Vector3(1.0f,1.0f,1.0f));
            zoomOut.setDurationMilliseconds(2000);

        }catch (ParsingException ex){

            ex.printStackTrace();

        }

        zoomOut.setTransformable3D(object3D);
        getCurrentScene().registerAnimation(zoomOut);
        zoomIn.setTransformable3D(object3D);
        getCurrentScene().registerAnimation(zoomIn);
    }

//    @Override
//    public void onRender(final long elapsedTime, final double deltaTime) {
//        super.onRender(elapsedTime, deltaTime);
//    }

    public void onTouchEvent(MotionEvent event){

    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }

    public void zoomIn(){
        zoomIn.play();
    }

    public void zoomOut(){
        zoomOut.play();
    }
}

