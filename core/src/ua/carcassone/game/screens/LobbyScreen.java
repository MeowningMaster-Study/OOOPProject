package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.PCLPlayers;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;

import static ua.carcassone.game.Utils.*;

public class LobbyScreen implements Screen {
    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private String tableId;

    private Skin mySkin;
    private PCLPlayers players;
    private PlayersObserver playersObserver;


    public LobbyScreen(final CarcassoneGame game, String tableId, PCLPlayers pclPlayers) {
        this.game = game;
        this.tableId = tableId;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        mySkin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        this.playersObserver = new PlayersObserver();
        this.players = pclPlayers;
        this.players.addPCLListener(playersObserver);
        game.socketClient.setPCLPlayers(this.players);

        GameWebSocketClient.stateSingleObserver changeObserver = new GameWebSocketClient.stateSingleObserver(
                (stateChange)->{
                    if ( stateChange.newState == GameWebSocketClient.ClientStateEnum.IN_GAME) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("CHANGING TO A GAME SCREEN "+this.getClass().getSimpleName());
                            players.removePCLListener(playersObserver);
                            int tiles = (int) stateChange.additionalInfo[0];
                            game.setScreen(
                                    new GameScreen(game, this.tableId, tiles, players)
                            );
                        });
                    }
                }
        );
        game.socketClient.addStateObserver(changeObserver);


        updateStage();
    }

    private void addImages(){
        Image image1 = new Image(new Texture("skins/images/cowboy.png"));
        float hwCoefficient = image1.getHeight()/image1.getWidth();
        image1.setHeight(Math.min(Gdx.graphics.getDisplayMode().height*0.65f, image1.getHeight()));
        image1.setWidth(image1.getHeight()/hwCoefficient);
        image1.setPosition((Gdx.graphics.getDisplayMode().width*0.55f - image1.getWidth()*0.5f), (Gdx.graphics.getDisplayMode().height * 0.05f));
        stage.addActor(image1);
    }

    private ImageButton makeCopyButton(String path, String pathClicked){
        Texture copyTexture = new Texture(Gdx.files.internal(path));
        Drawable copyDrawable = new TextureRegionDrawable(new TextureRegion(copyTexture));
        Texture copyTextureCl = new Texture(Gdx.files.internal(pathClicked));
        Drawable copyDrawableCl = new TextureRegionDrawable(new TextureRegion(copyTextureCl));
        ImageButton copyButton = new ImageButton(copyDrawable, copyDrawableCl);

        copyButton.setPosition(3* ELEMENT_WIDTH_UNIT + 40, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        copyButton.setSize(50, 50);


        copyButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                copyStringInBuffer(tableId);
            }
        });
        return copyButton;
    }

    private void copyStringInBuffer(String message){
        final StringSelection stringSelection = new StringSelection(message);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private Button makeStartGameButton(String name){
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
                try {
                    game.socketClient.startGame();
                } catch (IncorrectClientActionException e) {
                    e.printStackTrace();
                }

            }
        });

        return createButton;
    }

    private Button makeLeaveButton(String name){
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
                try {
                    game.socketClient.leaveTable();
                } catch (IncorrectClientActionException e) {
                    e.printStackTrace();
                }
                dispose();
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

    private void updateStage(){
        stage.clear();

        addImages();

        Label carcassoneLabel = new Label("Lobby", mySkin, "title");
        carcassoneLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        carcassoneLabel.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(carcassoneLabel);

        Label codeLabel = new Label("Join Code:", mySkin, "big");
        codeLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        codeLabel.setPosition(carcassoneLabel.getX(), carcassoneLabel.getY()-ELEMENT_HEIGHT_UNIT);
        stage.addActor(codeLabel);

        Label code = new Label(this.tableId, mySkin, "half-tone");
        code.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        code.setPosition(codeLabel.getX()+codeLabel.getWidth()+20, codeLabel.getY());
        stage.addActor(code);

        ImageButton copyButton = makeCopyButton("skins/icons/copy.png", "skins/icons/copyClicked.png");
        stage.addActor(copyButton);

        Button startGameButton = makeStartGameButton("Start game");
        stage.addActor(startGameButton);

        Button leaveButton = makeLeaveButton("Leave table");
        stage.addActor(leaveButton);

        drawPlayers();
    }

    private void drawPlayers(){
        System.out.println("Drawing "+players.getPlayers());
        float size = players.getPlayers().size();
        float heightSpacing = 20;
        float top = Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2);
        float left = fromRight(ELEMENT_WIDTH_UNIT * 2);

        Label playersLabel = new Label("Players:", mySkin, "big");
        playersLabel.setSize(100, 20);
        playersLabel.setPosition(left, top);
        stage.addActor(playersLabel);

        for (int i = 0; i < size; ++i) {
            Player player = players.getPlayers().get(i);

            Label pName = new Label(player.getName(), mySkin, "narration");
            pName.setSize(150, 40);
            pName.setPosition(left, top-(40+heightSpacing)*(i+1));
            stage.addActor(pName);
        }
    }

    private class PlayersObserver implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt){
            System.out.println("AAAAAAAAAAAAAAAAAH PROPERTY CHANGED SENPAIII");
            updateStage();
        }
    }

}
