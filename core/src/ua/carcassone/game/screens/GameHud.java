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

    Image currentTileImage;

    Button menuButton;
    Button confirmationButton;
    Button cancelButton;

    TileTextureManager textureManager;
    CurrentTileObserver currentTileObserver;
    CurrentPlayerObserver currentPlayerObserver;

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

        currentTileImage.setSize(170, 170);
        currentTileImage.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5), (float) (ELEMENT_HEIGHT_UNIT * 1.3));

        menuButton = makeMenuButton("Menu");
        stage.addActor(menuButton);

        confirmationButton = makeConfirmationButton("skins/icons/confirm.png");
        stage.addActor(confirmationButton);

        cancelButton = makeCancelButton("skins/icons/confirm.png");
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
        confirmationButton.setSize(50, 50);
        confirmationButton.setPosition(currentTileImage.getX() - confirmationButton.getHeight(), currentTileImage.getY());

        confirmationButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.postRunnable(gameScreen.map::rotateSelectedTile);
            }
        })
    }

    public void updateStage(){
        stage.clear();

        if(gameScreen.currentTile.getCurrentTile() != null){
            drawCurrentTile();
        }
        if(gameScreen.players.getPlayers() != null){
            drawPlayers();
        }

        if(gameScreen.currentTile.isPut()){
            drawMeeples();
            stage.addActor(confirmationButton);
            stage.addActor(cancelButton);
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
        System.out.println("drawing "+size+" players");

        float heightCoeff = Gdx.graphics.getHeight() / ((size + 1) * ELEMENT_HEIGHT_UNIT);
        for (int i = 0; i < size; ++i) {
            Player player = gameScreen.players.getPlayers().get(i);

            Image pImage = new Image(textureManager.getTexture(0, 0));
            pImage.setPosition((float)(ELEMENT_WIDTH_UNIT / 2), Utils.fromTop(ELEMENT_HEIGHT_UNIT * (heightCoeff * i + 2)));
            pImage.setSize(100, 100);
            stage.addActor(pImage);

            Label pName = new Label(
                    (gameScreen.players.isTurnOf(player)?"=> ":"")+
                            player.getName()
                    ,
//                    new Label.LabelStyle(new BitmapFont(), player.getColor())
                    mySkin,
                    "alt"
            );
            pName.setSize(100, 20);
            pName.setPosition(pImage.getX(), pImage.getY());
            stage.addActor(pName);
        }
    }

    private void drawCurrentTile(){
        Tile currentTile = gameScreen.currentTile.getCurrentTile();
        currentTileImage = new Image(textureManager.getTexture(currentTile));
        stage.addActor(currentTileImage);
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
                    Objects.equals(evt.getPropertyName(), "isSpotted"))
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
