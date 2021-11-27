package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameScreen implements Screen {

    public final CarcassoneGame game;
    private final OrthographicCamera camera;
    public Viewport viewport;
    private final Stage stage;
    private final GameHud hud;
    private final GameField field;
    private final String tableId;

    public final Map map;
    public PCLPlayers players;
    public PCLCurrentTile currentTile;

    private final Label debugLabel;

    public GameScreen(final CarcassoneGame game, String tableId) {
        this.game = game;
        this.tableId = tableId;
        System.out.println(tableId+" - 4");
        this.map = new Map(new Tile(TileTypes.tiles.get(1), 0));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        debugLabel = new Label("Debug", mySkin, "default");
        debugLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        debugLabel.setPosition(ELEMENT_WIDTH_UNIT * 5, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        stage.addActor(debugLabel);

        Label tableIdLabel = new Label("Table ID: "+this.tableId, mySkin, "default");
        tableIdLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        tableIdLabel.setPosition(ELEMENT_WIDTH_UNIT * 2, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        stage.addActor(tableIdLabel);


        hud = new GameHud(this);
        field = new GameField(this);

        players = new PCLPlayers();
        players.addPCLListener(hud.playersObserver);
        currentTile = new PCLCurrentTile();
        currentTile.addPCLListener(hud.currentTileObserver);

        // ------------
        Player[] testPlayers = {
                new Player("firstPlayer", "111", Color.BLUE),
                new Player("secondPlayer", "222", Color.RED),
                new Player("thirdPlayer", "333", Color.YELLOW),
                new Player("fourthPlayer", "444", Color.GREEN),
                new Player("fifthPlayer", "555", Color.PINK)
        };
        currentTile.setTile(new Tile(TileTypes.tiles.get(1), 0));
        players.setPlayers(new ArrayList<>(Arrays.asList(testPlayers)));
        // ------------
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        field.handleInput(delta);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        field.stage.act(Gdx.graphics.getDeltaTime());
        field.stage.draw();

        hud.stage.act(Gdx.graphics.getDeltaTime());
        hud.stage.draw();

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

    class PCLPlayers{
        private ArrayList<Player> players;
        private PropertyChangeSupport support;

        public PCLPlayers(){
            support = new PropertyChangeSupport(this);
        }

        public void addPCLListener(PropertyChangeListener pcl){
            support.addPropertyChangeListener(pcl);
        }

        public void removePCLListener(PropertyChangeListener pcl){
            support.removePropertyChangeListener(pcl);
        }

        public void setPlayers(ArrayList<Player> newPlayers){
            support.firePropertyChange("players", this.players, newPlayers);
            this.players = newPlayers;
        }

    }

    class PCLCurrentTile{
        private Tile currentTile;
        private PropertyChangeSupport support;

        public PCLCurrentTile(){
            support = new PropertyChangeSupport(this);
        }

        public void addPCLListener(PropertyChangeListener pcl){
            support.addPropertyChangeListener(pcl);
        }

        public void removePCLListener(PropertyChangeListener pcl){
            support.removePropertyChangeListener(pcl);
        }

        public void setTile(Tile newTile){
            support.firePropertyChange("currentTile", this.currentTile, newTile);
            this.currentTile = newTile;
        }

    }

    public void setDebugLabel(String val){
        debugLabel.setText(val);
    }


}