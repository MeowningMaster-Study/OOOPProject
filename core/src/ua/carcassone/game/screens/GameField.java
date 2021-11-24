package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.TileTextureManager;

public class GameField {
    public Stage stage;
    private GameScreen gameScreen;
    private TileTextureManager textureManager;

    public GameField(GameScreen gameScreen){
        this.textureManager = new TileTextureManager();
        this.gameScreen = gameScreen;
        stage = new Stage(gameScreen.viewport, gameScreen.game.batch);

        updateStage();
    }

    private void updateStage(){
        int from = 60, to = 80;
        stage.clear();
        int fieldHeight = Utils.fromTop(0);
        int tileSize = fieldHeight/(to-from);
        for (int i = 0; i < 143; i++){
            for (int j = 0; j < 143; j++){
                if (gameScreen.map[i][j] != null){
                    Image image = new Image(textureManager.getTexture(gameScreen.map[i][j].type, gameScreen.map[i][j].rotation));
                    image.setPosition((i-from)*tileSize,(j-from)*tileSize);
                    image.setSize(tileSize, tileSize);
                    stage.addActor(image);
                }
            }
        }

    }




}
