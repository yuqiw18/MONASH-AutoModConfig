package yuqi.amc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import javax.jws.WebParam;

/**
 * Created by Yuqi on 19/04/2017.
 */

public class Renderer implements ApplicationListener {

    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private ModelBatch modelBatch;
    private AssetManager assetManager;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private boolean isLoading;

    @Override
    public void create() {

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(5f, 5f, 5f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assetManager = new AssetManager();
        assetManager.load("gtr_body.obj",Model.class);
        assetManager.load("gtr_body_window.obj",Model.class);
        assetManager.load("gtr_rim.obj",Model.class);
        assetManager.load("gtr_tyre.obj",Model.class);
        assetManager.load("gtr_bodyA.obj", Model.class);
        assetManager.load("gtr_lightingA.obj", Model.class);
        assetManager.load("stage.obj", Model.class);
        isLoading = true;

    }

    private void doneLoading(){
        Model body = assetManager.get("gtr_body.obj", Model.class);
        ModelInstance bodyIns = new ModelInstance(body);

        //bodyIns.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
        instances.add(bodyIns);

        Model carWind = assetManager.get("gtr_body_window.obj", Model.class);
        ModelInstance carWindInstance = new ModelInstance(carWind);
        instances.add(carWindInstance);

        Model rim = assetManager.get("gtr_rim.obj", Model.class);
        ModelInstance rimIns = new ModelInstance(rim);
        instances.add(rimIns);

        Model tyre = assetManager.get("gtr_tyre.obj", Model.class);
        ModelInstance tyreIns = new ModelInstance(tyre);
        instances.add(tyreIns);

//        Model body;
//        Model bodyA;
        Model bodyA = assetManager.get("gtr_bodyA.obj", Model.class);
        ModelInstance bodyAIns = new ModelInstance(bodyA);
        instances.add(bodyAIns);
//        Model bodyB;
//        Model bumper;
//        Model bonnet;
//        Model spoiler;
//        Model exhaust;
//        Model brake;
//        Model rim;
//        Model tyre;
//        Model lightingA;
        Model lightingA = assetManager.get("gtr_lightingA.obj", Model.class);
        ModelInstance lightingAIns = new ModelInstance(lightingA);
        instances.add(lightingAIns);

        Model stage = assetManager.get("stage.obj", Model.class);
        ModelInstance stageIns = new ModelInstance(stage);
        stageIns.transform.setToTranslation(0,-0.3f,0);
        instances.add(stageIns);



//        Model lightingB;

        isLoading = false;
    }

    @Override
    public void render() {

        if (isLoading && assetManager.update()){

            doneLoading();
        }

        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();

//        if (cam.position.y < 0){
//            cam.position.set(cam.position.x, 0, cam.position.z);
//        }
//
//        System.out.println(cam.position);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assetManager.dispose();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void updateScene(String type, String value){

        switch (value){
            case "Red":
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#ff3333")));
                break;
            case "Blue":
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#3366ff")));
                break;
            case "Green":
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#2eb82e")));
                break;
            case "Yellow":
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#ffcc00")));
                break;
            case "Black":
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#1a1a1a")));
                break;
            default:
                instances.get(0).materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));
                break;
        }
    }
}
