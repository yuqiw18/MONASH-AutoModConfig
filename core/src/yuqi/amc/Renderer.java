package yuqi.amc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
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
import com.badlogic.gdx.math.Vector3;
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
    private Array<ModelInstance> instances = new Array<>();
    private boolean isLoading = false;
    private boolean modelAssigned = false;
    private ArrayList<String> modelList = new ArrayList<>();
    private RendererStateListener rendererStateListener;
    private String currentColor = "#ffffff";

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
        //shader = new ShaderProgram(Gdx.files.internal("shaders/VertexShader.vsh"), Gdx.files.internal("shaders/PhongPixelShader.psh"));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assetManager = new AssetManager();
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
//            Gdx.gl.glColorMask(GL30.GL_TRUE,GL20.GL_TRUE,GL20.GL_TRUE,GL20.GL_TRUE);
//            Gdx.gl.glDepthMask();
            Gdx.gl.glClearColor( 0, 0, 0, 1 );
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_STENCIL_BUFFER_BIT);

            //shader.begin();
            modelBatch.begin(cam);
            modelBatch.render(instances, environment);
            //modelBatch.flush();
            modelBatch.end();
            //shader.end();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void replacePart(int pos, String fileName){

        Gdx.graphics.setContinuousRendering(false);
        modelAssigned = false;

        if (!assetManager.isLoaded(fileName)){
            assetManager.load(fileName, Model.class);
            assetManager.finishLoading();
            Gdx.app.log("ReplacePart", "New Model Loaded");
        }

        Model part = assetManager.get(fileName, Model.class);

        ModelInstance modelInstance = new ModelInstance(part);

        if (instances.get(pos).model == modelInstance.model){

            // If it is same model then set back to the default one [DESELECT]
            Model defaultPart = assetManager.get(modelList.get(pos), Model.class);

            //instances.get(pos).model.dispose();

            instances.set(pos, new ModelInstance(defaultPart));

            Gdx.app.log("ReplacePart", "Back to Default");

        }else {

            //instances.get(pos).model.dispose();

            instances.set(pos, modelInstance);

            Gdx.app.log("ReplacePart", "Replace with New");

        }

        if (pos == 0 || pos == 1 || pos ==2 || pos == 3){
            instances.get(pos).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf(currentColor)));
        }

        modelBatch.dispose();

        modelAssigned = true;
        Gdx.graphics.setContinuousRendering(true);

    }

    public void updateScene(String type, String value){


        Gdx.app.log("updateScene", value);

        switch (type){
            case "Respray":
                for (int i = 0; i < 4; i ++){
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf(value)));
                }
                currentColor = value;
                break;
            case "Spoiler":
                replacePart(3, value);
                break;
            case "Bonnet":
                replacePart(2,"mitsubishi_lancer_evo_bonnet.obj");
                break;
            default:
                break;

        }
    }

    public void loadModels(String name){

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

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assetManager.dispose();
    }

}
