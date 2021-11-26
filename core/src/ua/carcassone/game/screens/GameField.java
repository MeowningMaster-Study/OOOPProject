package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Settings;
import ua.carcassone.game.game.TileTextureManager;

public class GameField {
    public Stage stage;
    private Viewport viewport;
    private final OrthographicCamera camera;
    private final GameScreen gameScreen;
    private final TileTextureManager textureManager;

    private final int tileSize;
    private final Vector2 fieldSize;
    private final Vector2 translationSpeed;
    private float zoomSpeed;
    private final Vector2 minOccupiedCoordinate;
    private final Vector2 maxOccupiedCoordinate;

    public GameField(GameScreen gameScreen){
        this.textureManager = new TileTextureManager();
        this.tileSize = textureManager.getMinTileSize();
        this.fieldSize = Settings.fieldTileCount.cpy().scl(tileSize);

        this.maxOccupiedCoordinate = new Vector2(0, 0);
        this.minOccupiedCoordinate = new Vector2(Settings.fieldTileCount.x, Settings.fieldTileCount.y);

        this.gameScreen = gameScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
        this.stage = new Stage(viewport, gameScreen.game.batch);
        this.translationSpeed = new Vector2(0f, 0f);
        updateStage();
        centerCameraOnTile(Settings.fieldTileCount.cpy().scl(0.5f));
    }

    /**
     * Updates the stage, using map
     */
    private void updateStage(){
        // can be updated by saving prev. field and only setting changed tiles
        // but is not needed as stage updates rarely
        stage.clear();

        System.out.println(Settings.fieldTileCount);
        for (int i = 0; i < Settings.fieldTileCount.y; i++){ // для каждой строки
            for (int j = 0; j < Settings.fieldTileCount.x; j++){ // для каждого столбца
                if (gameScreen.map[i][j] != null){
                    if (i < minOccupiedCoordinate.y) minOccupiedCoordinate.y = i;
                    if (i > maxOccupiedCoordinate.y) maxOccupiedCoordinate.y = i;
                    if (j < minOccupiedCoordinate.x) minOccupiedCoordinate.x = j;
                    if (j > maxOccupiedCoordinate.x) maxOccupiedCoordinate.x = j;

                    Image image = new Image(textureManager.getTexture(gameScreen.map[i][j].type, gameScreen.map[i][j].rotation));
                    image.setPosition(j*tileSize, ((Settings.fieldTileCount.y-i-1)*tileSize));
                    image.setSize(tileSize, tileSize);
                    stage.addActor(image);
                }
            }
        }

    }

    public void handleInput(float delta) {

        // new input
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

        // calculating camera based on input
        Vector2 translation = new Vector2(translationSpeed).cpy().scl(delta).scl(camera.zoom);
        camera.translate(translation);

        if (camera.position.x < minOccupiedCoordinate.x * tileSize  - camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.x = minOccupiedCoordinate.x * tileSize - camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.x > maxOccupiedCoordinate.x * tileSize  + camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.x = maxOccupiedCoordinate.x * tileSize + camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.y < minOccupiedCoordinate.y * tileSize  - camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.y = minOccupiedCoordinate.y * tileSize - camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.y > maxOccupiedCoordinate.y * tileSize  + camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.y = maxOccupiedCoordinate.y * tileSize + camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent;


        camera.zoom += zoomSpeed * delta;
        if (camera.zoom > Settings.maxCameraZoom) camera.zoom = Settings.maxCameraZoom;
        else if (camera.zoom < Settings.minCameraZoom) camera.zoom = Settings.minCameraZoom;


        // descend the speeds
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

    /**
     * Centers the camera on tile on map[x][y]
     * @param x x coordinate of a tile in map
     * @param y y coordinate of a tile in map
     */
    public void centerCameraOnTile(int x, int y){
        camera.position.x = tileSize*(x+0.5f);
        camera.position.y = fieldSize.y - tileSize*(y+0.5f);
        translationSpeed.setZero();
    }

    /**
     * Centers the camera on tile on map[pos.x][pos.y]
     * Vector will be cast to int.
     * @param pos position of a tile in map
     */
    public void centerCameraOnTile(Vector2 pos){
        centerCameraOnTile((int) pos.x, (int) pos.y);
    }

    public void setZoomToSeeTiles(int tilesQuantity){

    }

    public Vector2 getMinOccupiedCoordinate() {
        return minOccupiedCoordinate;
    }

    public Vector2 getMaxOccupiedCoordinate() {
        return maxOccupiedCoordinate;
    }
}
