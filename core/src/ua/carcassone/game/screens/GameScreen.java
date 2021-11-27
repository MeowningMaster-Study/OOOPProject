package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTypes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameScreen implements Screen {
    // s
    public final CarcassoneGame game;
    private OrthographicCamera camera;
    public Viewport viewport;
    private Stage stage;
    private GameHud hud;
    private GameField field;

    public final Tile[][] map;
    public PCLPlayers players;
    public PCLCurrentTile currentTile;

    public GameScreen(final CarcassoneGame game) {
        this.game = game;
        map = new Tile[143][143];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        // --- Test ---
        Random random = new Random();
        for (int i = 1; i < 142; i++) {
            for (int j = 1; j < 142; j++) {
                int tries = 0;
                while (tries < 25){
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
        field.handleInput(delta);

        ScreenUtils.clear(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Draw using game.batch
        game.batch.end();

/*        hud.batch.begin();
        // Draw using hudBatch
        hud.batch.end();*/

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        field.stage.act(Gdx.graphics.getDeltaTime());
        field.stage.draw();

        hud.hudStage.act(Gdx.graphics.getDeltaTime());
        hud.hudStage.draw();


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
}