package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.PCLCurrentTile;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTextureManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage stage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin mySkin;

    Button menuButton;
    Button confirmationButton;
    Button cancelButton;

    TileTextureManager textureManager;
    CurrentTileObserver currentTileObserver;
    CurrentPlayerObserver currentPlayerObserver;

    final float CURR_TILE_X = Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5);
    final float CURR_TILE_Y = (float) (ELEMENT_HEIGHT_UNIT * 1.3);

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.textureManager = new TileTextureManager();
        this.gameScreen.map.linkGameHud(this);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        gameScreen.inputMultiplexer.addProcessor(this.stage);
        mySkin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        currentTileObserver = new CurrentTileObserver();
        this.gameScreen.currentTile.addPCLListener(this.currentTileObserver);

        this.currentPlayerObserver = new CurrentPlayerObserver();
        this.gameScreen.players.addPCLListener(this.currentPlayerObserver);

        menuButton = makeMenuButton("Menu");
        stage.addActor(menuButton);

        confirmationButton = makeConfirmationButton("skins/icons/confirm.png");
        stage.addActor(confirmationButton);

        cancelButton = makeCancelButton("skins/icons/cancel.png");
        stage.addActor(confirmationButton);
    }

    private Button makeMenuButton(String name){
        Button menuButton = new TextButton(name, mySkin);
        menuButton.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        menuButton.setPosition(Gdx.graphics.getWidth() - (float)(ELEMENT_WIDTH_UNIT * 1.5), Utils.fromTop((float) (ELEMENT_HEIGHT_UNIT * 1.5)));
        menuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.isPaused = true;
                pause();
                updateStage();

            }
        });
        return menuButton;
    }

    private Button makeConfirmationButton(String path){
        ImageButton confirmationButton = getImageButton(path);
        confirmationButton.setSize(150, 150);
        confirmationButton.setPosition(CURR_TILE_X, CURR_TILE_Y);

        confirmationButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(gameScreen.currentTile.isSet()){
                    gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_PLACE_MEEPLE);
                }
                else{
                    gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_STABILIZED);
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {}
        });

        return confirmationButton;
    }

    private Button makeCancelButton(String path) {
        ImageButton cancelButton = getImageButton(path);
        cancelButton.setSize(150, 150);
        cancelButton.setPosition(CURR_TILE_X - confirmationButton.getWidth() - 10, CURR_TILE_Y);

        cancelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_PUT);
            }
        });

        return cancelButton;
    }

    public void updateStage(){
        stage.clear();

        if(gameScreen.currentTile.isSet()){
            if(gameScreen.currentTile.isHanging()){
                drawCurrentTile();
            }
            else{
                stage.addActor(confirmationButton);

                if(gameScreen.currentTile.isPlaceMeeple()){
                    drawMeeples();
                    stage.addActor(cancelButton);
                }
            }
        }
        if(gameScreen.players.getPlayers() != null){
            drawPlayers();
        }

        stage.addActor(menuButton);

    }

    private ImageButton getImageButton(String path){
        Texture imageTexture = new Texture(path);
        Drawable imageDrawable = new TextureRegionDrawable(new TextureRegion(imageTexture));
        return new ImageButton(imageDrawable);
    }

    private void drawPlayers(){
        float size = gameScreen.players.getPlayers().size();

        float heightCoeff = Gdx.graphics.getHeight() / ((size + 1) * ELEMENT_HEIGHT_UNIT);
        for (int i = 0; i < size; ++i) {
            Player player = gameScreen.players.getPlayers().get(i);

            Image pImage = new Image(textureManager.getTexture(0, 0));
            pImage.setPosition((float)(ELEMENT_WIDTH_UNIT / 2), Utils.fromTop(ELEMENT_HEIGHT_UNIT * (heightCoeff * i + 2)));
            pImage.setSize(100, 100);
            stage.addActor(pImage);

            Table labelsTable = new Table();
            labelsTable.setSize(100, 60);
            labelsTable.setPosition((float)(ELEMENT_WIDTH_UNIT / 2),
                    Utils.fromTop(ELEMENT_HEIGHT_UNIT * (heightCoeff * i + 2)) - pImage.getHeight() / 1.5f);
            labelsTable.setSkin(mySkin);

            Label pName = new Label(
                    (gameScreen.players.isTurnOf(player)?"=> ":"")+
                            player.getName()
                    ,
                    mySkin
            );

            Label pMeeples = new Label(
                    "Meeples: " + player.getMeepleCount(),
                    mySkin
            );

            Label pScore = new Label(
                    "Score: " + player.getScore(),
                    mySkin
            );

            labelsTable.add(pName).expandX().fillX().height(20);
            labelsTable.row();
            labelsTable.add(pMeeples).expandX().fillX().height(20);
            labelsTable.row();
            labelsTable.add(pScore).expandX().fillX().height(20);
            labelsTable.row();

            stage.addActor(labelsTable);
        }
    }

    private void drawCurrentTile(){
        Tile currentTile = gameScreen.currentTile.getCurrentTile();
        Image currentTileImage = new Image(textureManager.getTexture(currentTile));
        currentTileImage.setPosition(CURR_TILE_X, CURR_TILE_Y);
        currentTileImage.setSize(150, 150);
        stage.addActor(currentTileImage);
    }

    private void drawMeeples(){
        Image mImage = new Image(new Texture("skins/meeples/meeple_castle.png"));
        mImage.setPosition(CURR_TILE_X + 50, 3 * CURR_TILE_Y);
        mImage.setSize(50, 50);
        stage.addActor(mImage);
    }

    private class CurrentPlayerObserver implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent evt){
            if (Objects.equals(evt.getPropertyName(), "players")
                    || Objects.equals(evt.getPropertyName(), "currentPlayer"))
                updateStage();
        }
    }

    private class CurrentTileObserver implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent evt){
            if(Objects.equals(evt.getPropertyName(), "currentTile") ||
                    Objects.equals(evt.getPropertyName(), "state"))
                updateStage();
        }
    }

    public void pause(){
        menuButton.setTouchable(Touchable.disabled);
    }

    public void resume(){
        menuButton.setTouchable(Touchable.enabled);
        Gdx.input.setInputProcessor(gameScreen.inputMultiplexer);

    }
}
