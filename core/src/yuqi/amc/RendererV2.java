package yuqi.amc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Yuqi on 19/04/2017.
 */

public class RendererV2 implements ApplicationListener {

    private Environment environment;
    private PerspectiveCamera cam;
    private ShaderProgram shader;
    private CameraInputController camController;
    private ModelBatch modelBatch;
    private AssetManager assetManager;
    private Array<ModelInstance> instances = new Array<>();
    private boolean isLoading = false;
    private boolean modelAssigned = false;
    private ArrayList<String> defaultModelList = new ArrayList<>();
    private ArrayList<String> modifiedModelList = new ArrayList<>();
    private RendererStateListener rendererStateListener;
    private String currentColor = "#ffffff";
    private static boolean DEBUG_MODE = false;
    private static String MODEL_FORMAT = ".g3dj";

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

        assetManager = new AssetManager(new LocalFileHandleResolver());

    }

    private void doneLoading(){
        for (int i = 0; i < defaultModelList.size(); i++){
            Model model = assetManager.get(defaultModelList.get(i), Model.class);
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

    public void replacePart(final int pos, final String fileName){}

    public void updateScene(String type, String value){}

    public void loadModels(String name){

        // Asset
        defaultModelList.add("nissan_skyline_r34_oem_style_spoiler.g3dj");

        modifiedModelList = (ArrayList<String>)defaultModelList.clone();

        for (int i = 0; i< defaultModelList.size(); i ++){
            assetManager.load(defaultModelList.get(i), Model.class);
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

    public void setRendererStateListener(RendererStateListener rendererStateListener){
        this.rendererStateListener = rendererStateListener;
    }

}
