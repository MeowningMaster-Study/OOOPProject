package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;

import java.util.ArrayList;
import java.util.Observable;

import static ua.carcassone.game.Utils.*;

public class GameScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private GameHud hud;

    private final Tile[][] map;
    public CurrentTileObservable currentTile;
    public PlayersObservable players;

    public GameScreen(final CarcassoneGame game) {
        this.game = game;
        hud = new GameHud(this);
        map = new Tile[143][143];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Label carcassoneLabel = new Label("Game", mySkin, "big");
        carcassoneLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        carcassoneLabel.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(carcassoneLabel);

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(250f/255, 224f/255, 145f/255, 1);

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

        hud.stage.act(Gdx.graphics.getDeltaTime());
        hud.stage.draw();
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

    class PlayersObservable extends Observable {
        private ArrayList<Player> players;

        public PlayersObservable() {
            players = new ArrayList<>();
        }

        public void set(ArrayList<Player> players){
            this.players = players;
            setChanged();
            notifyObservers(this.players);
        }


        public ArrayList<Player> getPlayers() {
            return players;
        }

    }

    class CurrentTileObservable extends Observable {
        private Tile currentTile;

        public CurrentTileObservable() {
        }

        public void set(Tile currentTile){
            this.currentTile = currentTile;
            setChanged();
            notifyObservers(this.currentTile);
        }

        public Tile getCurrentTile() {
            return currentTile;
        }
    }
}