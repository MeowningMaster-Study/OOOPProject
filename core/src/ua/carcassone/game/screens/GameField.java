package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.TileTextureManager;

public class GameField {
    public Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private GameScreen gameScreen;
    private TileTextureManager textureManager;

    private Vector2 translationSpeed;
    private float zoomSpeed;

    public GameField(GameScreen gameScreen){
        this.textureManager = new TileTextureManager();
        this.gameScreen = gameScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
        this.stage = new Stage(viewport, gameScreen.game.batch);
        this.translationSpeed = new Vector2(0f, 0f);
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
                    image.setPosition((j-from)*tileSize,Utils.fromTop ((i-from)*tileSize));
                    image.setSize(tileSize, tileSize);
                    stage.addActor(image);
                }
            }
        }

    }

    public void handleInput(float delta) {



        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            this.zoomSpeed = -Settings.maxZoomSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            this.zoomSpeed = Settings.maxZoomSpeed;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.translationSpeed.x = -Settings.maxTranslationSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.translationSpeed.x = Settings.maxTranslationSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.translationSpeed.y = -Settings.maxTranslationSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.translationSpeed.y = Settings.maxTranslationSpeed;
        }


        Vector2 translation = new Vector2(translationSpeed);
        translation.scl(delta);
        translation.scl(camera.zoom);
        camera.translate(translation);
        camera.zoom += zoomSpeed * delta;
        if (camera.zoom > Settings.maxCameraZoom) camera.zoom = Settings.maxCameraZoom;
        else if (camera.zoom < Settings.minCameraZoom) camera.zoom = Settings.minCameraZoom;

        if (translationSpeed.x > 0){
            translationSpeed.x = Math.max(0, translationSpeed.x-Settings.translationSpeedDecrease*delta);
        }
        else if (translationSpeed.x < 0){
            translationSpeed.x = Math.min(0, translationSpeed.x+Settings.translationSpeedDecrease*delta);
        }

        if (translationSpeed.y > 0){
            translationSpeed.y = Math.max(0, translationSpeed.y-Settings.translationSpeedDecrease*delta);
        }
        else if (translationSpeed.y < 0){
            translationSpeed.y = Math.min(0, translationSpeed.y+Settings.translationSpeedDecrease*delta);
        }

        if (zoomSpeed > 0){
            zoomSpeed = Math.max(0, zoomSpeed-Settings.zoomSpeedDecrease*delta*Math.abs(zoomSpeed));
        }
        else if (zoomSpeed < 0){
            zoomSpeed = Math.min(0, zoomSpeed+Settings.zoomSpeedDecrease*delta*Math.abs(zoomSpeed));
        }

    }


}
