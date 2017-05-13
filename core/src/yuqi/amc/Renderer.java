package yuqi.amc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.GLVersion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuqi on 19/04/2017.
 */

public class Renderer implements ApplicationListener {

    private Environment environment;
    private PerspectiveCamera cam;
    private ShaderProgram shader;
    private CameraInputController camController;
    private ModelBatch modelBatch;
    private AssetManager assetManager;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private boolean isLoading = false;
    private boolean modelAssigned = false;
    private ArrayList<String> modelList;
    private RendererStateListener rendererStateListener;

    @Override
    public void create() {

        //rendererStateListener = (RendererStateListener)

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

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/VertexShader.vsh"), Gdx.files.internal("shaders/PhongPixelShader.psh"));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assetManager = new AssetManager();

        modelList = new ArrayList<String>();
    }

    private void doneLoading(){
        for (int i = 0; i < modelList.size(); i++){
            Model model = assetManager.get(modelList.get(i), Model.class);
            ModelInstance modelInstance = new ModelInstance(model);
            instances.add(modelInstance);
        }
        isLoading = false;
        //rendererStateListener.onRendererLoaded();
    }

    @Override
    public void render() {

        if (modelAssigned){

            if (isLoading && assetManager.update()){

                doneLoading();
            }

            camController.update();

            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            shader.begin();
            modelBatch.begin(cam);
            modelBatch.render(instances, environment);
            modelBatch.end();
            shader.end();

        }
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

        switch (type){
            case "Respray":
                for (int i = 0; i < 4; i ++){
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf(value)));
                }
                break;
            default:
                break;

        }
    }

    public void replacePart(){

        assetManager.load("mitsubishi_lancer_evo_bonnet.obj", Model.class);
//        updateLoading = false;
//
        while (!assetManager.update()){

            System.out.println("Loading!!");

        }

//        if (assetManager.update()){
            Model part = assetManager.get("mitsubishi_lancer_evo_bonnet.obj", Model.class);
            instances.removeIndex(2);
            instances.insert(2, new ModelInstance(part));
//}
    }

    public void initModels(String name){

        // Asset 0 - 3 can be affected by respraying the car
        modelList.add(name + "_body.obj");
        modelList.add(name + "_bumper.obj");
        modelList.add(name + "_bonnet.obj");
        modelList.add(name + "_spoiler.obj");

        // Asset 4 - 7
        modelList.add(name + "_exhaust.obj");
        modelList.add(name + "_brake.obj");
        modelList.add(name + "_rim.obj");
        modelList.add(name + "_tyre.obj");

        // Asset
        modelList.add(name + "_lightingA.obj");
        modelList.add(name + "_lightingB.obj");
        modelList.add(name + "_lightingC.obj");
        modelList.add(name + "_chassis.obj");

        // Asset
        modelList.add("stage.obj");

        loadModels();
    }

    private void loadModels(){
        for (int i = 0; i< modelList.size(); i ++){
            assetManager.load(modelList.get(i), Model.class);
        }
        isLoading = true;
        modelAssigned = true;
    }


    public interface RendererStateListener{

        void onRendererLoaded();
        void onRendererReload();

    }
}
