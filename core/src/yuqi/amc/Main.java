package yuqi.amc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Yuqi on 19/04/2017.
 */

public class Main extends ApplicationAdapter {

    private SpriteBatch spriteBatch;
    private Texture splash;

    @Override
    public void create () {
        spriteBatch = new SpriteBatch();
        splash = new Texture("splash.png");
    }

    @Override
    public void render () {
        // Get screen width and height
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Get splash width and height
        float sw = splash.getWidth();
        float sh = splash.getHeight();

        // Set background color to white
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Start drawing the splash at the center of screen
        spriteBatch.begin();
        spriteBatch.draw(splash, w/2 - sw/2, h/2 - sh/2);
        spriteBatch.end();
    }

    @Override
    public void dispose () {
        spriteBatch.dispose();
        splash.dispose();
    }
}
