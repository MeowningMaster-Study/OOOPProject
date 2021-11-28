package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage hudStage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin mySkin;

    Button menuButton;
    ImageButton leftRotateButton;
    ImageButton rightRotateButton;

    TileTextureManager textureManager;
    CurrentTileObserver currentTileObserver;
    PlayersObserver playersObserver;

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.textureManager = new TileTextureManager();
        this.gameScreen.map.linkGameHud(this);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        hudStage = new Stage(viewport, gameScreen.game.batch);
        Gdx.input.setInputProcessor(hudStage);
        mySkin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        currentTileObserver = new CurrentTileObserver();
        playersObserver = new PlayersObserver();

        menuButton = makeMenuButton("Menu");
        hudStage.addActor(menuButton);

        leftRotateButton = makeRotateButton("left", "skins/icons/left.png");
        hudStage.addActor(leftRotateButton);

        rightRotateButton = makeRotateButton("right", "skins/icons/right.png");
        hudStage.addActor(rightRotateButton);
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

    private ImageButton makeRotateButton(String type, String path){
        Texture leftRotateTexture = new Texture(Gdx.files.internal(path));
        Drawable leftDrawable = new TextureRegionDrawable(new TextureRegion(leftRotateTexture));
        ImageButton leftRotateButton = new ImageButton(leftDrawable);
        leftRotateButton.setSize(50, 50);
        int shift = type.equals("left") ? 0 : 170 - 50;
        leftRotateButton.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5) + shift,
                (float) (ELEMENT_HEIGHT_UNIT * 1.3));

        leftRotateButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(type.equals("left")){
                    currentTileObserver.leftRotate();
                }
                else{
                    currentTileObserver.rightRotate();
                }
            }
        });

        return leftRotateButton;
    }

    public void updateStage(){
        hudStage.clear();

        if(currentTileObserver.tile != null){
            drawCurrentTile();
        }
        if(gameScreen.players != null){
            drawPlayers();
        }

        hudStage.addActor(menuButton);
        hudStage.addActor(rightRotateButton);
        hudStage.addActor(leftRotateButton);

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
            hudStage.addActor(pImage);

            System.out.println("Drawing for "+
                    (gameScreen.players.isTurnOf(player)?"=> ":"")+
                    player.getName()+
                    player.getColor()
            );
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
            hudStage.addActor(pName);
        }
    }

    private void drawCurrentTile(){
        Image tileImage = currentTileObserver.tileImage;
        tileImage.setSize(170, 170);
        tileImage.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5), (float) (ELEMENT_HEIGHT_UNIT * 1.3));
        hudStage.addActor(tileImage);

        hudStage.addActor(leftRotateButton);
        hudStage.addActor(rightRotateButton);
    }

    private class PlayersObserver implements PropertyChangeListener{
        public void propertyChange(PropertyChangeEvent evt){
            updateStage();
        }
    }

    private class CurrentTileObserver implements PropertyChangeListener{
        private Tile tile;
        private Image tileImage;

        public void propertyChange(PropertyChangeEvent evt){
            this.tile = (Tile) evt.getNewValue();
            tileImage = new Image(textureManager.getTexture(this.tile));
            updateStage();
        }

        public void leftRotate(){
            tile.rotation = tile.rotation != 0 ? tile.rotation - 1 : 3;
            tileImage = new Image(textureManager.getTexture(this.tile));
            updateStage();
        }

        public void rightRotate(){
            tile.rotation = (tile.rotation + 1) % 4;
            tileImage = new Image(textureManager.getTexture(this.tile));
            updateStage();
        }
    }

    public void pause(){

        menuButton.setTouchable(Touchable.disabled);
        rightRotateButton.setTouchable(Touchable.disabled);
        leftRotateButton.setTouchable(Touchable.disabled);

    }

    public void resume(){

        menuButton.setTouchable(Touchable.enabled);
        rightRotateButton.setTouchable(Touchable.enabled);
        leftRotateButton.setTouchable(Touchable.enabled);

        Gdx.input.setInputProcessor(hudStage);

    }
}
