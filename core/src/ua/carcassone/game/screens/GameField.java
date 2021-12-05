package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.*;
import ua.carcassone.game.game.sprites.PointTypeSprite;
import ua.carcassone.game.game.sprites.TileTypeSpritesGenerator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ua.carcassone.game.Settings.shiftTranslationCoefficient;
import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

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
    private CurrentPlayerObserver currentPlayerObserver;
    private CurrentTileObserver currentTileObserver;

    public GameField(GameScreen gameScreen){
        this.textureManager = new TileTextureManager();
        this.tileSize = textureManager.getMinTileSize();
        this.fieldSize = Settings.fieldTileCount.cpy().scl(tileSize);

        this.gameScreen = gameScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
        this.stage = new Stage(viewport, gameScreen.game.batch);
        this.translationSpeed = new Vector2(0f, 0f);
        gameScreen.inputMultiplexer.addProcessor(this.stage);

        this.currentTileObserver = new CurrentTileObserver();
        this.gameScreen.currentTile.addPCLListener(this.currentTileObserver);
        this.currentPlayerObserver = new CurrentPlayerObserver();
        this.gameScreen.players.addPCLListener(this.currentPlayerObserver);
        gameScreen.map.linkGameField(this);
        centerCameraOnTile(0,0);
    }

    /**
     * Updates the stage, using map
     */
    public void updateStage(){
        // can be updated by saving prev. field and only setting changed tiles
        // but is not needed as stage updates rarely

        stage.clear();

        addBaseTiles();

        if(gameScreen.players.isCurrentPlayerClient() &&
                TileTypes.isGamingTile(gameScreen.currentTile.getCurrentTile()) &&
                (gameScreen.players.getCurrentPlayer().getMeepleCount() > 0))
            addAvailableTileSpots();
        addSprites();
        addControls();
        addMeeples();

    }

    private void addBaseTiles(){
        float halfTile = tileSize/2.0f;
        for (int i = gameScreen.map.minX(); i <= gameScreen.map.maxX(); i++){ // для каждого столбца
            for (int j = gameScreen.map.maxY(); j >= gameScreen.map.minY(); j--){ // для каждой строки
                if (gameScreen.map.get(i, j) != null) {
                    Tile tile = gameScreen.map.get(i, j);
                    Image image = new Image(textureManager.getTexture(tile.type, tile.rotation));
                    image.setPosition(i * tileSize - halfTile, j * tileSize - halfTile);
                    image.setSize(tileSize, tileSize);
                    stage.addActor(image);
                }
            }
        }
    }

    private void addAvailableTileSpots(){
        float halfTile = tileSize/2.0f;

        ArrayList<Vector2> availableTileSpots = gameScreen.map.getAvailableSpots(gameScreen.currentTile.getCurrentTile().type);
        for (Vector2 coordinate : availableTileSpots) {
            Texture imageTexture = textureManager.getInnerBorderTexture();
            Drawable imageDrawable = new TextureRegionDrawable(new TextureRegion(imageTexture));
            ImageButton imageButton = new ImageButton(imageDrawable);
            imageButton.setPosition(coordinate.x*tileSize-halfTile, coordinate.y*tileSize-halfTile);
            imageButton.setSize(tileSize, tileSize);
            imageButton.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.postRunnable(() -> gameScreen.gameLogic.setSelection(coordinate));
                }
            });
            stage.addActor(imageButton);

        }
    }

    private void addSprites(){
        float halfTile = tileSize/2.0f;

        for (int i = gameScreen.map.minX(); i <= gameScreen.map.maxX(); i++) { // для каждого столбца
            for (int j = gameScreen.map.maxY(); j >= gameScreen.map.minY(); j--) { // для каждой строки
                if (gameScreen.map.get(i, j) != null) {
                    Tile tile = gameScreen.map.get(i, j);
                    Group group = new Group();
                    group.setPosition(i * tileSize - halfTile, j * tileSize - halfTile);
                    group.setSize(tileSize, tileSize);

                    List<PointTypeSprite> generatedSprites =
                            TileTypeSpritesGenerator.generatePointTypeSprites(tile.type, tile.getSeed(), tileSize);

                    generatedSprites.addAll(TileTypeSpritesGenerator.generateMandatorySprites(gameScreen.map, i, j, tile.getSeed()));

                    generatedSprites.sort((o1, o2)->{
                        if(o1.getY() == o2.getY()) return 0;
                        return (o1.getY() > o1.getY() ? 1 : -1);
                    });

                    for (PointTypeSprite sprite : generatedSprites) {
                        Image spriteImage = sprite.getImage(tile.rotation, tileSize);
                        group.addActor(spriteImage);
                    }
                    stage.addActor(group);
                }
            }
        }
    }




    private void addControls(){
        float halfTile = tileSize/2.0f;

        for (int i = gameScreen.map.minX(); i <= gameScreen.map.maxX(); i++) { // для каждого столбца
            for (int j = gameScreen.map.maxY(); j >= gameScreen.map.minY(); j--) { // для каждой строки
                if (gameScreen.map.get(i, j) != null) {
                    Tile tile = gameScreen.map.get(i, j);

                    if (tile.purpose != Tile.TilePurpose.LEGIT) {

                        // border
                        Texture borderTexture;
                        if (tile.purpose == Tile.TilePurpose.IMAGINARY_SELECTED) {
                            borderTexture = textureManager.getBorderTexture();
                        } else if (tile.purpose == Tile.TilePurpose.IMAGINARY_FOCUS) {
                            borderTexture = textureManager.getBorderWhiteTexture();
                        } else {
                            borderTexture = textureManager.getTransparentTexture();
                        }

                        Drawable imageDrawable = new TextureRegionDrawable(new TextureRegion(borderTexture));
                        ImageButton imageButton = new ImageButton(imageDrawable);
                        imageButton.setPosition(i * tileSize - halfTile, j * tileSize - halfTile);
                        imageButton.setSize(tileSize, tileSize);

                        // points
                        if (gameScreen.currentTile.isPlaceMeeple()) {
                            for (MeeplePosition meeplePosition : MeeplePosition.get(tile)) {
                                ImageButton pointButton = new ImageButton(
                                        new TextureRegionDrawable(new TextureRegion(textureManager.getPointDarkerTexture())),
                                        new TextureRegionDrawable(new TextureRegion(textureManager.getPointDarkTexture()))
                                );

                                pointButton.setSize(tileSize * Settings.pointRadius * 2, tileSize * Settings.pointRadius * 2);
                                pointButton.setPosition(
                                        tileSize * meeplePosition.position.x,
                                        tileSize * meeplePosition.position.y,
                                        Align.center
                                );

                                pointButton.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        return true;
                                    }

                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        Gdx.app.postRunnable(() -> gameScreen.gameLogic.setMeepleOnSelectedTile(meeplePosition.entityId));
                                    }
                                });
                                imageButton.addActor(pointButton);
                            }
                        }

                        // rotate
                        if (gameScreen.currentTile.isPut()) {
                            if (gameScreen.map.getAvailableRotations(i, j, tile.type).size() > 1) {
                                Image rotateImage = new Image(textureManager.getRotateClockwiseTexture(tile.rotation));
                                rotateImage.setPosition(i * tileSize - halfTile / 2.0f, j * tileSize - halfTile / 2.0f);
                                rotateImage.setSize(tileSize / 2.0f, tileSize / 2.0f);
                                stage.addActor(rotateImage);

                                imageButton.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        return true;
                                    }

                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        Gdx.app.postRunnable(gameScreen.map::rotateSelectedTile);
                                    }
                                });
                            }

                        }
                        stage.addActor(imageButton);
                    }
                }
            }
        }
    }

    private void addMeeples(){
        float halfTile = tileSize/2.0f;

        for (int i = gameScreen.map.minX(); i <= gameScreen.map.maxX(); i++) { // для каждого столбца
            for (int j = gameScreen.map.maxY(); j >= gameScreen.map.minY(); j--) { // для каждой строки
                if (gameScreen.map.get(i, j) != null) {
                    Tile tile = gameScreen.map.get(i, j);

                    if (tile.hasMeeple()){
                        ImageButton meepleActor = new ImageButton(
                                new TextureRegionDrawable(new TextureRegion(
                                        textureManager.getMeepleTexture(tile.getMeeple().getPlayer().getColor())
                                ))
                        );
                        Vector2 position = MeeplePosition.getPosition(tile.getMeeple(), tile);
                        assert position != null;
                        System.out.println(position);
                        float hwCoefficient = meepleActor.getHeight()/meepleActor.getWidth();
                        meepleActor.setSize(tileSize * Settings.meepleHeight/hwCoefficient, tileSize * Settings.meepleHeight);
                        meepleActor.setPosition(
                                (i+position.x)*tileSize-halfTile,
                                (j+position.y)*tileSize-halfTile,
                                Align.center
                        );


                        if(gameScreen.currentTile.isPlaceMeeple()){
                            meepleActor.addListener(new InputListener(){
                                @Override
                                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {return true;}

                                @Override
                                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                                    Gdx.app.postRunnable(()-> gameScreen.gameLogic.unsetMeepleOnSelectedTile());
                                }
                            });
                        }
                        stage.addActor(meepleActor);
                    }

                }
            }
        }
    }


    public void handleInput(float delta) {
        handleNewInput();
        calculateCamera(delta);
        descendSpeed(delta);
    }

    private void handleNewInput(){
        float shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? shiftTranslationCoefficient : 0f;
        // new input
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            if(this.zoomSpeed == 0) {

                this.zoomSpeed = -Settings.maxZoomSpeed / 3;
            }
            else
                this.zoomSpeed = -Settings.maxZoomSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if(this.zoomSpeed == 0) {
                this.zoomSpeed = Settings.maxZoomSpeed / 3;
            }
            else
                this.zoomSpeed = Settings.maxZoomSpeed;
        }

        float maxTranslationSpeed = (float) (Settings.maxTranslationSpeed
                + Math.pow(Utils.min(gameScreen.map.getOccupiedSize()), Settings.maxTranslationSpeedTilesPower));
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.translationSpeed.x = -maxTranslationSpeed * (1 + shift);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.translationSpeed.x = maxTranslationSpeed * (1 + shift);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.translationSpeed.y = -maxTranslationSpeed * (1 + shift);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.translationSpeed.y = maxTranslationSpeed * (1 + shift);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            this.setZoomToSeeTiles(2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            this.setZoomToSeeTiles(3);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)) {
            this.setZoomToSeeTiles(4);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.V)) {
            this.setZoomToSeeTiles(5);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameScreen.gameLogic.confirmSelectedTilePosition();
        }
    }

    private void calculateCamera(float delta){
        Vector2 translation = new Vector2(translationSpeed).cpy().scl(delta).scl(camera.zoom);
        camera.translate(translation);

        if (camera.position.x < gameScreen.map.getMinOccupiedCoordinate().x * tileSize  - camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.x = gameScreen.map.getMinOccupiedCoordinate().x * tileSize - camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.x > gameScreen.map.getMaxOccupiedCoordinate().x * tileSize  + camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.x = gameScreen.map.getMaxOccupiedCoordinate().x * tileSize + camera.viewportWidth*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.y < gameScreen.map.getMinOccupiedCoordinate().y * tileSize  - camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.y = gameScreen.map.getMinOccupiedCoordinate().y * tileSize - camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent;
        if (camera.position.y > gameScreen.map.getMaxOccupiedCoordinate().y * tileSize  + camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent)
            camera.position.y = gameScreen.map.getMaxOccupiedCoordinate().y * tileSize + camera.viewportHeight*camera.zoom/2 * Settings.possibleEmptyCameraPercent;

        camera.zoom += zoomSpeed * delta;

        if (camera.zoom > Settings.maxCameraZoom * Math.pow(Utils.min(gameScreen.map.getOccupiedSize()), Settings.maxCameraZoomTilesPower)) {
            camera.zoom = (float) (Settings.maxCameraZoom * Math.pow(Utils.min(gameScreen.map.getOccupiedSize()), Settings.maxCameraZoomTilesPower));
        }
        if (camera.zoom < Settings.minCameraZoom) {
            camera.zoom = Settings.minCameraZoom;
        }

    }

    private void descendSpeed(float delta){

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
            zoomSpeed = Math.max(0, zoomSpeed-Settings.zoomSpeedDecrease*delta);
            if (zoomSpeed < 0.001)
                zoomSpeed = 0;
        }
        else if (zoomSpeed < 0){
            zoomSpeed = Math.min(0, zoomSpeed+Settings.zoomSpeedDecrease*delta);
            if (zoomSpeed > -0.001)
                zoomSpeed = 0;
        }
    }


    /**
     * Centers the camera on tile on map[x][y]
     * @param x x coordinate of a tile in map
     * @param y y coordinate of a tile in map
     */
    public void centerCameraOnTile(int x, int y){
        camera.position.x = tileSize*x;
        camera.position.y = tileSize*y;
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
        float minScreenDimension = Math.min(camera.viewportWidth, camera.viewportHeight);
        float neededTileSize = minScreenDimension/tilesQuantity;
        camera.zoom = tileSize/neededTileSize;
    }

    private class CurrentPlayerObserver implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt){
            if (Objects.equals(evt.getPropertyName(), "currentPlayer"))
                updateStage();
        }
    }

    private class CurrentTileObserver implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent evt){
            if(Objects.equals(evt.getPropertyName(), "currentTile") ||
                    Objects.equals(evt.getPropertyName(), "state") )
                updateStage();
        }
    }


}
