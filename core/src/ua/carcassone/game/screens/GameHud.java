package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import java.util.ArrayList;
import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage stage;
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

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));


        currentTileObserver = new CurrentTileObserver();
        playersObserver = new PlayersObserver();


        menuButton = new TextButton("Menu", mySkin);
        menuButton.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        menuButton.setPosition(Gdx.graphics.getWidth() - (float)(ELEMENT_WIDTH_UNIT * 1.5), Utils.fromTop((float) (ELEMENT_HEIGHT_UNIT * 1.5)));
        menuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                currentTileObserver.rightRotate();
            }
        });
        stage.addActor(menuButton);

        Texture leftRotateTexture = new Texture(Gdx.files.internal("skin/icons/left.png"));
        Drawable leftDrawable = new TextureRegionDrawable(new TextureRegion(leftRotateTexture));
        leftRotateButton = new ImageButton(leftDrawable);
        leftRotateButton.setSize(50, 50);
        leftRotateButton.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5),
                                        (float) (ELEMENT_HEIGHT_UNIT * 1.3));

        leftRotateButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                currentTileObserver.leftRotate();
            }
        });

        stage.addActor(leftRotateButton);

        Texture rightRotateTexture = new Texture(Gdx.files.internal("skin/icons/right.png"));
        Drawable rightDrawable = new TextureRegionDrawable(new TextureRegion(rightRotateTexture));
        rightRotateButton = new ImageButton(rightDrawable);
        rightRotateButton.setSize(50, 50);
        rightRotateButton.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5) + 170 - 50,
                                        (float) (ELEMENT_HEIGHT_UNIT * 1.3));

        rightRotateButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBB");
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                currentTileObserver.rightRotate();
            }
        });

        stage.addActor(rightRotateButton);
    }

    private void updateStage(){
        stage.clear();

        stage.addActor(menuButton);

        if(currentTileObserver.tile != null){
            Image tileImage = currentTileObserver.tileImage;
            tileImage.setSize(170, 170);
            tileImage.setPosition(Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5), (float) (ELEMENT_HEIGHT_UNIT * 1.3));
            stage.addActor(tileImage);

            stage.addActor(leftRotateButton);
            stage.addActor(rightRotateButton);
        }


        if(playersObserver.players != null){
            float size = playersObserver.players.size();
            float heightCoeff = Gdx.graphics.getHeight() / ((size + 1) * ELEMENT_HEIGHT_UNIT);
            for (int i = 0; i < size; ++i) {
                Player player = playersObserver.players.get(i);


                Image pImage = new Image(new Texture(Gdx.files.internal("skin/classic-tiles/Null-0.png")));
                System.out.println(ELEMENT_HEIGHT_UNIT);
                System.out.println(heightCoeff);
                System.out.println(ELEMENT_HEIGHT_UNIT * (heightCoeff * i + 2));
                pImage.setPosition((float)(ELEMENT_WIDTH_UNIT / 2), Utils.fromTop(ELEMENT_HEIGHT_UNIT * (heightCoeff * i + 2)));
                pImage.setSize(100, 100);
                stage.addActor(pImage);

                Label pName = new Label(player.getName(), new Label.LabelStyle(new BitmapFont(), player.getColor()));
                pName.setSize(100, 20);
                pName.setPosition(pImage.getX(), pImage.getY());
                stage.addActor(pName);
            }
        }
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

}
