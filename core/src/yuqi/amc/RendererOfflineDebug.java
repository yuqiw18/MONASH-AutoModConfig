package yuqi.amc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

/**
 * Created by Yuqi on 19/04/2017.
 */

public class RendererOfflineDebug implements ApplicationListener {

    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private ModelBatch modelBatch;
    private AssetManager assetManager;
    private Array<ModelInstance> instances = new Array<>();
    private ArrayList<String> defaultModelList = new ArrayList<>();

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

    }

    @Override
    public void render() {

        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    public void loadModels(String name){

        defaultModelList.add("mercedes_benz_s_s65_amg_body.g3dj");
        defaultModelList.add("mercedes_benz_s_s65_amg_bumper.obj");
        defaultModelList.add("mercedes_benz_s_s65_amg_bonnet.obj");
        defaultModelList.add("mercedes_benz_s_s65_amg_spoiler.obj");
        defaultModelList.add("mercedes_benz_s_s65_amg_exhaust.obj");
        defaultModelList.add("mercedes_benz_s_s65_amg_chassis.obj");

        defaultModelList.add("stage.g3dj");

        for (int i = 0; i< defaultModelList.size(); i ++){
            assetManager.load(defaultModelList.get(i), Model.class);
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                assetManager.finishLoading();

                for (int i = 0; i < defaultModelList.size(); i++){
                    Model model = assetManager.get(defaultModelList.get(i), Model.class);
                    ModelInstance modelInstance = new ModelInstance(model);
                    instances.add(modelInstance);
                }
            }
        });
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assetManager.dispose();
    }


    public void replacePart(final int pos, final String fileName){}

    public void updateScene(String type, String value){}
}
