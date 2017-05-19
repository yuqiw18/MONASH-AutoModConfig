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
    //private boolean modelAssigned = false;
    private ArrayList<String> defaultModelList = new ArrayList<>();
    private ArrayList<String> modifiedModelList = new ArrayList<>();
    private RendererStateListener rendererStateListener;
    private String currentColor = "#ffffff";
    private static boolean DEBUG_MODE = false;
    private static String MODEL_FORMAT = ".g3dj";

    //
    private int progress = 0;
    private int fileToProcess = 0;
    private boolean isDownloading = false;

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



    @Override
    public void render() {

        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_STENCIL_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
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

        if (DEBUG_MODE){

            defaultModelList.add("mercedes_benz_s_s65_amg_body.g3dj");
            defaultModelList.add("nissan_skyline_r34_lighting.g3dj");
            defaultModelList.add("mercedes_benz_s_s65_amg_bumper.obj");
            defaultModelList.add("mercedes_benz_s_s65_amg_bonnet.obj");
            defaultModelList.add("mercedes_benz_s_s65_amg_spoiler.obj");
            defaultModelList.add("mercedes_benz_s_s65_amg_exhaust.obj");
            defaultModelList.add("mercedes_benz_s_s65_amg_chassis.obj");

        }else {

            // Asset 0 - 3 can be affected by respraying the car
            defaultModelList.add(name + "_body.g3dj");
            defaultModelList.add(name + "_bumper.g3dj");
            defaultModelList.add(name + "_bonnet.g3dj");
            defaultModelList.add("placeholder_spoiler.g3dj");

            // 4-5
            defaultModelList.add(name + "_lighting.g3dj");
            defaultModelList.add(name + "_other.g3dj");

            // Asset 5 - 9
            defaultModelList.add(name + "_exhaust.g3dj");

            defaultModelList.add(name + "_brake.g3dj");
            defaultModelList.add(name + "_rim.g3dj");
            defaultModelList.add(name + "_tyre.g3dj");
        }

        // Asset
        defaultModelList.add("stage.g3dj");
        //defaultModelList.add("nissan_skyline_r34_oem_style_spoiler.g3dj");

        fileToProcess = defaultModelList.size();

        modifiedModelList = (ArrayList<String>)defaultModelList.clone();

        for (int i = 0; i< fileToProcess; i ++){

            final String fileName = defaultModelList.get(i);

            try {

                assetManager.load(fileName, Model.class);
                assetManager.update();

            }catch (Exception e){

                isDownloading = true;

                // File not exist, need to download from server
                Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
                request.setTimeOut(2500);
                request.setUrl("http://amc.yuqi.ninja/resource/"+ fileName);

                Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        // Determine how much we have to download
                        long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                        // We're going to download the file to external storage, create the streams
                        InputStream is = httpResponse.getResultAsStream();
                        OutputStream os = Gdx.files.local(fileName).write(false);

                        byte[] bytes = new byte[1024];
                        int count = -1;
                        long read = 0;


                        try {
                            // Keep reading bytes and storing them until there are no more.
                            while ((count = is.read(bytes, 0, bytes.length)) != -1) {
                                os.write(bytes, 0, count);
                                read += count;

                                // Update the UI with the download progress
                                final int progress = ((int) (((double) read / (double) length) * 100));
                                final String progressString = progress == 100 ? "Download Complete" : progress + "%";

                                Gdx.app.log("Progress", progressString);
                            }

                            os.close();

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    //assetManager.load(Gdx.files.local(fileName));
                                    assetManager.load(fileName, Model.class);
                                    progress ++;
                                    if (progress == fileToProcess){
                                        assetManager.finishLoading();
                                        isDownloading = false;

                                        instances.clear();

                                        for (int i = 0; i < defaultModelList.size(); i++){
                                            Model model = assetManager.get(defaultModelList.get(i), Model.class);
                                            ModelInstance modelInstance = new ModelInstance(model);
                                            instances.add(modelInstance);
                                        }
                                    }
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void failed(Throwable t) {
                        return;
                    }

                    @Override
                    public void cancelled() {
                        return;
                    }
                });

            }
        }

        if (!isDownloading){

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {

                    assetManager.finishLoading();

                    instances.clear();

                    for (int i = 0; i < defaultModelList.size(); i++){
                        Model model = assetManager.get(defaultModelList.get(i), Model.class);
                        ModelInstance modelInstance = new ModelInstance(model);
                        instances.add(modelInstance);
                    }
                    modelBatch.dispose();

                }
            });


        }
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
