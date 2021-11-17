package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;

import java.util.Observable;
import java.util.Observer;

public class JoinGameScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private String str = "Bottom text";

    public JoinGameScreen(final CarcassoneGame game) {
        this.game = game;



        int scalingCoefficient = 12;
        int row_height = Gdx.graphics.getWidth() / scalingCoefficient;
        int col_width = Gdx.graphics.getWidth() / scalingCoefficient;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Label carcassoneLabel = new Label("Join game", mySkin, "big");
        carcassoneLabel.setSize(col_width, row_height);
        carcassoneLabel.setPosition(col_width*2, Utils.fromTop(row_height*2));
        stage.addActor(carcassoneLabel);

        final TextField joinCodeField = new TextField("", mySkin);
        joinCodeField.setSize(col_width*2,row_height);
        joinCodeField.setPosition(col_width*2, Utils.fromTop(row_height*4));
        stage.addActor(joinCodeField);

        Button joinButton = new TextButton("Join", mySkin);
        joinButton.setSize(col_width*3,row_height);
        joinButton.setPosition(col_width*2, Utils.fromTop(row_height*6));
        joinButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                try {
                    game.socketClient.connectToTable(joinCodeField.getText());
                } catch (IncorrectClientActionException e) {
                    e.printStackTrace();
                }

                game.gameScreen = new GameScreen(game);
                GameWebSocketClient.onStateChangedObserver changeObserver = new GameWebSocketClient.onStateChangedObserver(()->{
                    System.out.println("SETTING THE SCREEN!!");
                    game.setScreen(game.gameScreen);
                });
                game.socketClient.addStateObserver(changeObserver);
            }
        });
        stage.addActor(joinButton);

        Button backButton = new TextButton("Back", mySkin);
        backButton.setSize(col_width*5,row_height);
        backButton.setPosition(col_width*2, Utils.fromTop(row_height*8));
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(backButton);
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