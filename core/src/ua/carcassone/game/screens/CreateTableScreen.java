package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.PCLPlayers;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ua.carcassone.game.Utils.*;

public class CreateTableScreen implements Screen {
    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private final TextField createTableField;

    private Skin mySkin;

    public CreateTableScreen(final CarcassoneGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        mySkin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        addImages();

        Label carcassoneLabel = new Label("Create Table", mySkin, "title");
        carcassoneLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        carcassoneLabel.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(carcassoneLabel);

        createTableField = makeCreateTableField("Table name...");
        stage.addActor(createTableField);

        Button createButton = makeCreateButton("Create");
        stage.addActor(createButton);

        Button backButton = makeBackButton("Back");
        stage.addActor(backButton);
    }

    private void addImages(){
        Image image1 = new Image(new Texture("skins/images/poster1.png"));
        float hwCoefficient = image1.getHeight()/image1.getWidth();
        image1.setWidth(Math.min(Gdx.graphics.getDisplayMode().width*0.6f, image1.getWidth()));
        image1.setHeight(image1.getWidth()*hwCoefficient);
        image1.setPosition((Gdx.graphics.getDisplayMode().width - image1.getWidth())+15, (Gdx.graphics.getDisplayMode().height - image1.getHeight())/2.0f);
        stage.addActor(image1);
    }

    // TODO find new font
    private TextField makeCreateTableField(final String text){

        final TextField createTableField = new TextField("", mySkin);
        createTableField.setMessageText(text);
        createTableField.setSize(ELEMENT_WIDTH_UNIT * 3, ELEMENT_HEIGHT_UNIT);
        createTableField.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 4));

        return createTableField;
    }

    private Button makeCreateButton(String name){
        Button createButton = new TextButton(name, mySkin);
        createButton.setSize(ELEMENT_WIDTH_UNIT * 3, ELEMENT_HEIGHT_UNIT);
        createButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 6));
        createButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // TODO table names concept
                GameWebSocketClient.stateAcceptableObserver changeObserver = new GameWebSocketClient.stateAcceptableObserver(
                        GameWebSocketClient.ClientStateEnum.CONNECTED_TO_TABLE,
                        Arrays.asList(new GameWebSocketClient.ClientStateEnum[]{GameWebSocketClient.ClientStateEnum.CREATING_TABLE}),
                        (stateChange)->{
                            if ( stateChange.newState == GameWebSocketClient.ClientStateEnum.CONNECTED_TO_TABLE) {
                                System.out.println("CHANGING TO A LOBBY SCREEN");
                                String tableId = (String) stateChange.additionalInfo[0];
                                PCLPlayers pclPlayers = (PCLPlayers) stateChange.additionalInfo[1];
                                System.out.println("Got PCL creating: "+pclPlayers);
                                Gdx.app.postRunnable(() -> game.setScreen(
                                        new LobbyScreen(game, tableId, createTableField.getText(), pclPlayers))
                                );
                            }
                        }
                );
                game.socketClient.addStateObserver(changeObserver);

                try {
                    game.socketClient.createTable(createTableField.getText());
                } catch (IncorrectClientActionException e) {
                    e.printStackTrace();
                }

            }
        });
        return createButton;
    }

    private Button makeBackButton(String name){
        Button backButton = new TextButton(name, mySkin);
        backButton.setSize(ELEMENT_WIDTH_UNIT * 3,ELEMENT_HEIGHT_UNIT);
        backButton.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 8));
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        return backButton;
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
