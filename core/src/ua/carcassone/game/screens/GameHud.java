package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTextureManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage stage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin mySkin;

    TileTextureManager textureManager;
    CurrentTileObserver currentTileObserver;
    PlayersObserver players;

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.textureManager = new TileTextureManager();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));


        currentTileObserver = new CurrentTileObserver();
        players = new PlayersObserver();


        Button menuButton = new TextButton("Menu", mySkin);
        menuButton.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        menuButton.setPosition(Gdx.graphics.getWidth() - (float)(ELEMENT_WIDTH_UNIT * 1.5), Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(menuButton);

/*        Image tileImage = new Image(textureManager.getTexture(new Tile(TileTypes.tiles.get(1), 1)));
        tileImage.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        tileImage.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        stage.addActor(tileImage);*/
    }

    private void updateStage(){
        stage.clear();

        Image tileImage = currentTileObserver.tileImage;
        tileImage.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT));
        tileImage.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        stage.addActor(tileImage);
    }

    class PlayersObserver implements PropertyChangeListener{
        private ArrayList<Player> players;

        public void propertyChange(PropertyChangeEvent evt){
            this.players = (ArrayList<Player>) evt.getNewValue();
            updateStage();
        }
    }

    class CurrentTileObserver implements PropertyChangeListener{
        private Tile tile;
        private Image tileImage;

        public void propertyChange(PropertyChangeEvent evt){
            this.tile = (Tile) evt.getNewValue();
            tileImage = new Image(textureManager.getTexture(this.tile));
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
            updateStage();
        }
    }

}
