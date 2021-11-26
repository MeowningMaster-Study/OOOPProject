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
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTypes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameScreen implements Screen {

    public final CarcassoneGame game;
    private OrthographicCamera camera;
    public Viewport viewport;
    private Stage stage;
    private GameHud hud;
    private GameField field;

    public final Tile[][] map;
    public PCLPlayers players;
    public PCLCurrentTile currentTile;

    private Label debugLabel;

    public GameScreen(final CarcassoneGame game) {
        this.game = game;
        map = new Tile[(int) Settings.fieldTileCount.y][(int) Settings.fieldTileCount.x];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        debugLabel = new Label("Debug", mySkin, "default");
        debugLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        debugLabel.setPosition(ELEMENT_WIDTH_UNIT * 10, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(debugLabel);

        // --- Test ---
        Random random = new Random();
        for (int i = 1; i < Settings.fieldTileCount.y-1; i++) {
            for (int j = 1; j < Settings.fieldTileCount.x-1; j++) {
                int tries = 0;
                while (tries < 50){
                    Tile tile = new Tile(TileTypes.tiles.get(1+random.nextInt(24)), random.nextInt(4));
                    if (tile.canBePutBetween(map[i-1][j], map[i][j+1], map[i+1][j], map[i][j-1])) {
                        map[i][j] = tile;
                        break;
                    }
                    tries++;
                }
            }
        }

        // ------------
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