package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;
import ua.carcassone.game.networking.GameWebSocketClient;

import static ua.carcassone.game.Utils.*;

public class MainMenuScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private Skin skin;

    public MainMenuScreen(final CarcassoneGame game) {
        this.game = game;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        Label carcassoneLabel = new Label("Carcassone Game", skin, "title");
        carcassoneLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        carcassoneLabel.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(carcassoneLabel);

        Label connectionLabel = new Label("NOT Connected", skin, "default");
        connectionLabel.setPosition(10, 20);
        stage.addActor(connectionLabel);

        if(!game.socketClient.isConnected()){
            GameWebSocketClient.stateMultipleObserver observer = new GameWebSocketClient.stateMultipleObserver((stateChange)->{
                if (stateChange.newState == GameWebSocketClient.ClientStateEnum.CONNECTED_TO_SERVER)
                    connectionLabel.setText("Connected to server!");
                else
                    connectionLabel.setText("Not connected to server! " + stateChange.newState);
            });
            game.socketClient.addStateObserver(observer);
        } else {
            connectionLabel.setText("Connected to server!");
        }

        Button settingsButton = makeSettingsButton("Settings");
        stage.addActor(settingsButton);
      
        Button createTableButton = makeCreateTableButton("Create table");
        stage.addActor(createTableButton);

        Button joinButton = makeJoinButton("Join game");
        stage.addActor(joinButton);

        Button exitButton = makeExitButton("Exit");
        stage.addActor(exitButton);

    }

    private Button makeCreateTableButton(String name){
        Button createTableButton = new TextButton(name, skin);
        createTableButton.setSize(ELEMENT_WIDTH_UNIT * 3, ELEMENT_HEIGHT_UNIT);
        createTableButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 4));
        createTableButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new CreateTableScreen(game));
            }
        });

        return createTableButton;
    }

    private Button makeJoinButton(String name){
        Button joinButton = new TextButton(name, skin);
        joinButton.setSize(ELEMENT_WIDTH_UNIT * 3, ELEMENT_HEIGHT_UNIT);
        joinButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 6));
        joinButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new JoinGameScreen(game));
                dispose();
            }
        });

        return joinButton;
    }

    private Button makeSettingsButton(String name){
        Button settingsButton = new TextButton(name, skin);
        settingsButton.setSize(ELEMENT_WIDTH_UNIT * 3,ELEMENT_HEIGHT_UNIT);
        settingsButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 8));
        settingsButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });
        return settingsButton;
    }

    private Button makeExitButton(String name){
        Button exitButton = new TextButton(name, skin);
        exitButton.setSize(ELEMENT_WIDTH_UNIT * 3,ELEMENT_HEIGHT_UNIT);
        exitButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 10));
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.socketClient.close();
                dispose();
                Gdx.app.exit();
            }
        });
        return exitButton;
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
        stage.dispose();
    }
}