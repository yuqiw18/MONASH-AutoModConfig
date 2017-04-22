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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

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

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/VertexShader.vsh"), Gdx.files.internal("shaders/PhongPixelShader.psh"));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assetManager = new AssetManager();
        // Asset 0 - 3 can be affected by respraying the car
        assetManager.load("nissan_skyline_r34_body.obj",Model.class);
        assetManager.load("nissan_skyline_r34_bumper.obj",Model.class);
        assetManager.load("nissan_skyline_r34_bonnet.obj", Model.class);
        assetManager.load("nissan_skyline_r34_spoiler.obj", Model.class);

        // Asset 4 - 7
        assetManager.load("nissan_skyline_r34_exhaust.obj", Model.class);
        assetManager.load("nissan_skyline_r34_brake.obj",Model.class);
        assetManager.load("nissan_skyline_r34_rim.obj",Model.class);
        assetManager.load("nissan_skyline_r34_tyre.obj",Model.class);

        // Asset
        assetManager.load("nissan_skyline_r34_lightingA.obj", Model.class);
        assetManager.load("nissan_skyline_r34_lightingB.obj", Model.class);
        assetManager.load("nissan_skyline_r34_lightingC.obj", Model.class);

        // Asset
        assetManager.load("nissan_skyline_r34_chassis.obj",Model.class);

        assetManager.load("stage.obj", Model.class);
        isLoading = true;

    }

    private void doneLoading(){
        Model body = assetManager.get("nissan_skyline_r34_body.obj", Model.class);
        ModelInstance bodyIns = new ModelInstance(body);
        instances.add(bodyIns);

        Model bumper = assetManager.get("nissan_skyline_r34_bumper.obj", Model.class);
        ModelInstance bumperIns = new ModelInstance(bumper);
        instances.add(bumperIns);

        Model bonnet = assetManager.get("nissan_skyline_r34_bonnet.obj", Model.class);
        ModelInstance bonnetIns = new ModelInstance(bonnet);
        instances.add(bonnetIns);

        Model spoiler = assetManager.get("nissan_skyline_r34_spoiler.obj", Model.class);
        ModelInstance spoilerIns = new ModelInstance(spoiler);
        instances.add(spoilerIns);

        Model exhaust = assetManager.get("nissan_skyline_r34_exhaust.obj", Model.class);
        ModelInstance exhaustIns = new ModelInstance(exhaust);
        instances.add(exhaustIns);

        Model brake = assetManager.get("nissan_skyline_r34_brake.obj", Model.class);
        ModelInstance brakeIns = new ModelInstance(brake);
        instances.add(brakeIns);

        Model rim = assetManager.get("nissan_skyline_r34_rim.obj", Model.class);
        ModelInstance rimIns = new ModelInstance(rim);
        instances.add(rimIns);

        Model tyre = assetManager.get("nissan_skyline_r34_tyre.obj", Model.class);
        ModelInstance tyreIns = new ModelInstance(tyre);
        instances.add(tyreIns);

        Model lightingA = assetManager.get("nissan_skyline_r34_lightingA.obj", Model.class);
        ModelInstance lightingAIns = new ModelInstance(lightingA);
        instances.add(lightingAIns);

        Model lightingB = assetManager.get("nissan_skyline_r34_lightingB.obj", Model.class);
        ModelInstance lightingBIns = new ModelInstance(lightingB);
        instances.add(lightingBIns);

        Model lightingC = assetManager.get("nissan_skyline_r34_lightingC.obj", Model.class);
        ModelInstance lightingCIns = new ModelInstance(lightingC);
        instances.add(lightingCIns);

        Model chassis = assetManager.get("nissan_skyline_r34_chassis.obj", Model.class);
        ModelInstance chassisIns = new ModelInstance(chassis);
        instances.add(chassisIns);

        Model stage = assetManager.get("stage.obj", Model.class);
        ModelInstance stageIns = new ModelInstance(stage);
        instances.add(stageIns);

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

        shader.begin();
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
        shader.end();

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
                for (int i = 0; i < 4; i ++){
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#ff3333")));
                }
                break;
            case "Blue":
                for (int i = 0; i < 4; i ++) {
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#3366ff")));
                }
                break;
            case "Green":
                for (int i = 0; i < 4; i ++) {
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#2eb82e")));
                }
                break;
            case "Yellow":
                for (int i = 0; i < 4; i ++) {
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#ffcc00")));
                }
                break;
            case "Black":
                for (int i = 0; i < 4; i ++) {
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#1a1a1a")));
                }
                break;
            default:
                for (int i = 0; i < 4; i ++) {
                    instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));
                }
                break;
        }
    }
}
