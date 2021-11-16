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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;

public class MainMenuScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private String str = "Bottom text";

    public MainMenuScreen(final CarcassoneGame game) {
        this.game = game;

        int scalingCoefficient = 12;
        int row_height = Gdx.graphics.getWidth() / scalingCoefficient;
        int col_width = Gdx.graphics.getWidth() / scalingCoefficient;


                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Label carcassoneLabel = new Label("Carcassone Game", mySkin, "big");
        carcassoneLabel.setSize(col_width, row_height);
        carcassoneLabel.setPosition(col_width*2, Utils.fromTop(row_height*2));
        stage.addActor(carcassoneLabel);

        Button joinButton = new TextButton("Join game", mySkin);
        joinButton.setSize(col_width*5,row_height);
        joinButton.setPosition(col_width*2, Utils.fromTop(row_height*4));
        joinButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                str = x+":"+y;
            }
        });
        stage.addActor(joinButton);

        Button exitButton = new TextButton("Exit", mySkin);
        exitButton.setSize(col_width*5,row_height);
        exitButton.setPosition(col_width*2, Utils.fromTop(row_height*6));
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(250f/255, 224f/255, 145f/255, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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