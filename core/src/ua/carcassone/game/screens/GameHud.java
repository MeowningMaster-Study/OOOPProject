package ua.carcassone.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameHud {
    public Stage stage;
    public SpriteBatch spriteBatch;
    private GameScreen gameScreen;

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }
}
