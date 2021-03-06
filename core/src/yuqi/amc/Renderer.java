package yuqi.amc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Renderer implements ApplicationListener {

    private RendererStateListener rendererStateListener;

    // Renderer-related variables
    private Environment environment;
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private ModelBatch modelBatch;
    private AssetManager assetManager;
    private Array<ModelInstance> instances = new Array<>();

    // Logic-related variable
    private ArrayList<String> defaultModelList = new ArrayList<>();
    private ArrayList<String> modifiedModelList = new ArrayList<>();
    private float currentSuspension = 0;
    private String currentColor = "#ffffff";

    // Download-related variables
    private static String RESOURCE_URL = "http://amc.yuqi.ninja/resource/";
    private int progress = 0;
    private int fileToProcess = 0;

    @Override
    public void create() {

        modelBatch = new ModelBatch();

        // Create a simple environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // Instantiate a camera and set up the parameters
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(8f, 8f, 8f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        // Assign controller to camera
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Define the asset manager, which reads files from local path
        assetManager = new AssetManager(new LocalFileHandleResolver());
    }

    // Render is called for every frame
    @Override
    public void render() {

        // Check the user input (rotate camera)
        camController.update();

        // Clean the frame
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        // Render the scene
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    // Method for replacing a part on the vehicle
    public void replacePart(final int pos, final String fileName){

        // If the part is not loaded
        if (!assetManager.isLoaded(fileName)) {

            // Queue the request for loading the part
            assetManager.load(fileName, Model.class);
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

        // Try loading the file since the file may not exist
            try {
                assetManager.finishLoading();

                Model part = assetManager.get(fileName, Model.class);
                ModelInstance modelInstance = new ModelInstance(part);

                if (instances.get(pos).model == modelInstance.model){
                    modifiedModelList.set(pos, defaultModelList.get(pos));
                    Gdx.app.log("Default Part", defaultModelList.get(pos));

                }else {
                    modifiedModelList.set(pos, fileName);
                    Gdx.app.log("New Part", fileName);
                }

                instances.clear();
                modelBatch.dispose();

                reloadAssets();

            }catch (GdxRuntimeException e){

                // File not exist, need to download from server
                Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
                request.setTimeOut(2500);
                request.setUrl(RESOURCE_URL+ fileName);

                Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {

                        // Determine how much we have to download
                        long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

                        // Download the file to local storage
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

                                // [DEBUG] Display the progress
                                final int progress = ((int) (((double) read / (double) length) * 100));
                                final String progressString = progress == 100 ? "Download Complete" : progress + "%";
                                Gdx.app.log("Progress", progressString);
                            }

                            os.close();

                            // Load the downloaded file
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {

                                assetManager.load(fileName, Model.class);
                                assetManager.finishLoading();

                                Model part = assetManager.get(fileName, Model.class);
                                ModelInstance modelInstance = new ModelInstance(part);

                                // If it is not currently selected, update the part list
                                if (instances.get(pos).model == modelInstance.model){
                                    modifiedModelList.set(pos, defaultModelList.get(pos));
                                    Gdx.app.log("Default Part", defaultModelList.get(pos));

                                }else {
                                    // If it is currently selected, restore to default part (deselect)
                                    modifiedModelList.set(pos, fileName);
                                    Gdx.app.log("New Part", fileName);
                                }

                                instances.clear();
                                modelBatch.dispose();

                                reloadAssets();

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
        });
    }

    // Method for determining which parts need to be replaced
    public void updateScene(final String type, final String value){

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

            Gdx.app.log("updateScene", value);

            switch (type){
                case "Respray":
                    for (int i = 0; i < 4; i ++){
                        instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf(value)));
                    }
                    currentColor = value;
                    break;
                case "Bumper":
                    replacePart(1, value);
                    break;
                case "Bonnet":
                    replacePart(2, value);
                    break;
                case "Spoiler":
                    replacePart(3, value);
                    break;
                case "Lighting":
                    replacePart(4, value);
                    break;
                case "Exhaust":
                    replacePart(6, value);
                    break;
                case "Brake":
                    replacePart(7, value);
                    break;
                case "Rim":
                    replacePart(8, value);
                    break;
                case "Tyre":
                    replacePart(9, value);
                    break;
                case "Suspension":
                    float adjustment = Float.valueOf(value);

                    if (adjustment!=currentSuspension){
                        for (int i = 0; i < 7; i ++){
                            instances.get(i).transform.translate(0, currentSuspension*1, 0);
                            instances.get(i).transform.translate(0, adjustment*-1, 0);
                        }
                        currentSuspension = adjustment;
                    }else {
                        for (int i = 0; i < 7; i ++){
                            instances.get(i).transform.translate(0, adjustment*1, 0);
                        }
                        currentSuspension = 0;
                    }
                    break;
                default:
                    break;
            }
            }
        });
    }

    public void loadModels(String name){

        // Asset 0 - 3 can be affected by respraying the car
        defaultModelList.add(name + "_body.g3dj");
        defaultModelList.add(name + "_bumper.g3dj");
        defaultModelList.add(name + "_bonnet.g3dj");
        defaultModelList.add("placeholder_spoiler.g3dj");

        // Asset 4 - 6
        // Asset 0 - 6 can be affected by adjusting the suspension
        defaultModelList.add(name + "_lighting.g3dj");
        defaultModelList.add(name + "_other.g3dj");
        defaultModelList.add(name + "_exhaust.g3dj");

        // Asset 7 - 9
        defaultModelList.add(name + "_brake.g3dj");
        defaultModelList.add(name + "_rim.g3dj");
        defaultModelList.add(name + "_tyre.g3dj");

        // Asset
        defaultModelList.add("stage.g3dj");

        fileToProcess = defaultModelList.size();

        modifiedModelList = (ArrayList<String>)defaultModelList.clone();

        for (int i = 0; i< fileToProcess; i ++) {

            assetManager.load(defaultModelList.get(i), Model.class);

        }

        // Try loading the given files, if not exist then download them from the server and cache them locally
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
            try{
                assetManager.finishLoading();

                instances.clear();

                for (int i = 0; i < defaultModelList.size(); i++){
                    Model model = assetManager.get(defaultModelList.get(i), Model.class);
                    ModelInstance modelInstance = new ModelInstance(model);
                    instances.add(modelInstance);
                }
                modelBatch.dispose();

                rendererStateListener.onRendererLoad();

            }catch (GdxRuntimeException e){

                for (final String fileName: defaultModelList) {

                    // File not exist, need to download from server
                    Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
                    request.setTimeOut(2500);
                    request.setUrl(RESOURCE_URL+ fileName);

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

                                            instances.clear();
                                            modelBatch.dispose();

                                            for (int i = 0; i < defaultModelList.size(); i++){
                                                Model model = assetManager.get(defaultModelList.get(i), Model.class);
                                                ModelInstance modelInstance = new ModelInstance(model);
                                                instances.add(modelInstance);
                                            }

                                            rendererStateListener.onRendererLoad();
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
            }
        });
    }

    // Release all the resources upon shutting down
    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assetManager.dispose();
    }

    public void setRendererStateListener(RendererStateListener rendererStateListener){
        this.rendererStateListener = rendererStateListener;
    }

    private void reloadAssets(){
        for (int i = 0; i < modifiedModelList.size(); i++){
            Model model = assetManager.get(modifiedModelList.get(i), Model.class);
            instances.add(new ModelInstance(model));
        }

        for (int i = 0; i < 4; i ++){
            instances.get(i).materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf(currentColor)));
        }

        for (int i = 0; i < 7; i ++){
            instances.get(i).transform.translate(0, currentSuspension*-1, 0);
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    // An interface to communicate with its parent fragment
    public interface RendererStateListener{
        void onRendererLoad();
    }
}
