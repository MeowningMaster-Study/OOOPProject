package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;

import ua.carcassone.game.Utils;
import ua.carcassone.game.game.*;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameScreen implements Screen {

    public final CarcassoneGame game;
    private final OrthographicCamera camera;
    public Viewport viewport;

    public final Stage stage;
    public final GameHud hud;
    private final GameField field;
    private final String tableId;
    private int tilesLeft;
    public PauseGameScreen pauseScreen;

    public final Map map;
    public PCLPlayers players;
    public PCLCurrentTile currentTile;
    public InputMultiplexer inputMultiplexer;
  
    private final Label debugLabel;
    public boolean isPaused;

    public GameScreen(final CarcassoneGame game, String tableId, int tilesLeft, PCLPlayers players) {
        this.game = game;
        this.tableId = tableId;
        this.tilesLeft = tilesLeft;
        this.map = new Map();
        this.players = players;
        this.currentTile = new PCLCurrentTile();

        this.inputMultiplexer = new InputMultiplexer();


        pauseScreen = null;
        isPaused = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        debugLabel = new Label("Debug", mySkin, "default");
        debugLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        debugLabel.setPosition(ELEMENT_WIDTH_UNIT * 5, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        stage.addActor(debugLabel);

        Label tableIdLabel = new Label("Table ID: "+this.tableId, mySkin, "default");
        tableIdLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        tableIdLabel.setPosition(ELEMENT_WIDTH_UNIT * 2, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        stage.addActor(tableIdLabel);

        // TODO move to hud and link to game logic
        Label tilesLeftLabel = new Label("Tiles left: "+this.tilesLeft, mySkin, "default");
        tilesLeftLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        tilesLeftLabel.setPosition(ELEMENT_WIDTH_UNIT * 6, (ELEMENT_HEIGHT_UNIT));
        stage.addActor(tilesLeftLabel);


        hud = new GameHud(this);
        field = new GameField(this);


        this.players.addPCLListener(hud.currentPlayerObserver);
        this.map.setRelatedPlayers(this.players);
        currentTile.addPCLListener(hud.currentTileObserver);
        game.socketClient.setPCLCurrentTile(currentTile);
        game.socketClient.setMap(this.map);

        if (currentTile.getCurrentTile() == null){
            currentTile.setTile(new Tile(TileTypes.get(0), 0));
        }
        Gdx.input.setInputProcessor(this.inputMultiplexer);
    }

    @Override
    public void render(float delta) {

        if(isPaused && pauseScreen == null){
            pauseScreen = new PauseGameScreen(this);
        }
        else if(!isPaused && pauseScreen != null){
            pauseScreen = null;
        }

        if(!isPaused){
            field.handleInput(delta);
        }

        ScreenUtils.clear(1, 1, 1, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        if(!isPaused){
            field.stage.act(Gdx.graphics.getDeltaTime());
        }
        field.stage.draw();

        if(!isPaused){
            hud.stage.act(Gdx.graphics.getDeltaTime());
        }
        hud.stage.draw();

        if(isPaused){
            pauseScreen.stage.act(Gdx.graphics.getDeltaTime());
            pauseScreen.stage.draw();
        }

        if(!isPaused){
            stage.act(Gdx.graphics.getDeltaTime());
        }
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

    public void setDebugLabel(String val){
        debugLabel.setText(val);
    }


}