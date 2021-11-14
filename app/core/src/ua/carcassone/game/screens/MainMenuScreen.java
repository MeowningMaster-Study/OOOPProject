package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import ua.carcassone.game.CarcassoneGame;

public class MainMenuScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private String str = "Bottom text";

    public MainMenuScreen(final CarcassoneGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Button button = new TextButton("Text Button", mySkin);
        button.setSize(100,50);
        button.setPosition(400,400);

        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//                dispose();
//                Gdx.app.exit();
                str = x+":"+y;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(123214);


                return true;
            }
        });
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


        game.batch.begin();
        game.font.draw(game.batch, Gdx.input.getX()+", "+Gdx.input.getY(), 100, 150);
        game.font.draw(game.batch, this.str, 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
//            Gdx.graphics.setWindowedMode(100,100);
            //game.setScreen(new GameScreen(game));
//            dispose();
//            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}