package ua.carcassone.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Observable;
import java.util.Observer;

public class GameHud {
    public Stage stage;
    public SpriteBatch spriteBatch;
    private GameScreen gameScreen;

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        CurrentTileObserver currentTile = new CurrentTileObserver();
        gameScreen.currentTile.addObserver(currentTile);
    }

    class CurrentTileObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            // изменять stage
        }
    }
}
