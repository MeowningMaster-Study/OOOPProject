package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.Player;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTextureManager;
import ua.carcassone.game.game.sprites.PointTypeSprite;
import ua.carcassone.game.game.sprites.TileTypeSpritesGenerator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage stage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin skin;

    Button menuButton;
    Button confirmationButton;
    Button cancelButton;
    boolean isOverThePlayers;
    private Label tilesLeftLabel;

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
        skin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        currentTileObserver = new CurrentTileObserver();
        this.gameScreen.currentTile.addPCLListener(this.currentTileObserver);

        this.currentPlayerObserver = new CurrentPlayerObserver();
        this.gameScreen.players.addPCLListener(this.currentPlayerObserver);

        isOverThePlayers = false;

        menuButton = makeMenuButton("Menu");
        confirmationButton = makeConfirmationButton("skins/icons/confirm.png");
        cancelButton = makeCancelButton("skins/icons/cancel.png");
        tilesLeftLabel = makeTilesLeftLabel();
        updateStage();
    }

    private Button makeMenuButton(String name){
        Button menuButton = new TextButton(name, skin);
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
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(gameScreen.currentTile.isPut()){
                    gameScreen.gameLogic.confirmSelectedTilePosition();
                } else if(gameScreen.currentTile.isPlaceMeeple()) {
                    gameScreen.gameLogic.confirmSelectedTileMeeples();
                }
            }
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
                gameScreen.gameLogic.disproveSelectedTileMeeples();
            }
        });

        return cancelButton;
    }

    private Label makeTilesLeftLabel(){
        Label tilesLeftLabel = new Label("Tiles left: "+this.gameScreen.tilesTotal, skin, "big");
        tilesLeftLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        tilesLeftLabel.setPosition(ELEMENT_WIDTH_UNIT * 0.3f, (ELEMENT_HEIGHT_UNIT) * 0.5f);
        return tilesLeftLabel;
    }

    private void updateTilesLeftLabel(int left){
        tilesLeftLabel.setText("Tiles left: "+left);
    }

    public void updateStage(){
        stage.clear();

        stage.addActor(tilesLeftLabel);

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
        Table playersTable = new Table(skin);
        int iconWidth = 100;
        int iconHeight = 100;
        int infoHeight = 60;
        int padding = 10;

        playersTable.setWidth(iconWidth);
        playersTable.setHeight((iconHeight + iconWidth + infoHeight + padding)* size);

        for (int i = 0; i < size; ++i) {

            if(i != 0){
                playersTable.row();
                playersTable.pad(padding);
            }

            Player player = gameScreen.players.getPlayers().get(i);

            Image pImage = new Image(textureManager.getTexture(0, 0));
            pImage.setSize(iconWidth, iconHeight);

            // длбавляем рамку для игрока, чей ход сейчас.
            if(gameScreen.players.isTurnOf(player)){
                Image borderImage = new Image(textureManager.getBorderSilverTexture());
                borderImage.setSize(pImage.getWidth(), pImage.getHeight());

                Stack stack = new Stack();
                stack.add(pImage);
                stack.add(borderImage);

                playersTable.add(stack).fillX().width(pImage.getWidth()).height(pImage.getHeight());
            }
            else {
                playersTable.add(pImage).fillX();
            }

            Label pName = new Label(
                    (gameScreen.players.isTurnOf(player) ? "=> " : "") +
                            player.getName()
                    ,
                    skin
            );

            Label pMeeples = new Label(
                    "Meeples: " + player.getMeepleCount(),
                    skin
            );

            Label pScore = new Label(
                    "Score: " + player.getScore(),
                    skin
            );

            Table labelsTable = new Table(skin);
            labelsTable.setWidth(iconWidth);
            labelsTable.setHeight(infoHeight);

            labelsTable.add(pName).expandX().fillX().height(20);
            labelsTable.row();
            labelsTable.add(pMeeples).expandX().fillX().height(20);
            labelsTable.row();
            labelsTable.add(pScore).expandX().fillX().height(20);

            playersTable.row();
            playersTable.add(labelsTable).fillX();
        }

        playersTable.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(playersTable.getHeight() + 40));
        stage.addActor(playersTable);

    }

    private void drawCurrentTile(){
        final float tileSize = 150;

        Tile currentTile = gameScreen.currentTile.getCurrentTile();
        Group toDraw = new Group();
        toDraw.setPosition(CURR_TILE_X, CURR_TILE_Y);
        toDraw.setSize(tileSize, tileSize);

        Image currentTileImage = new Image(textureManager.getTexture(currentTile));
        currentTileImage.setPosition(0,0);
        currentTileImage.setSize(tileSize, tileSize);
        toDraw.addActor(currentTileImage);

        List<PointTypeSprite> generatedSprites =
                TileTypeSpritesGenerator.generatePointTypeSprites(currentTile.type, currentTile.getSeed(), tileSize);

        generatedSprites.addAll(TileTypeSpritesGenerator.generateMandatorySprites(
                currentTile.type, currentTile.rotation, currentTile.getSeed())
        );

        generatedSprites.sort((o1, o2)->{
            if(o1.getY() == o2.getY()) return 0;
            return (o1.getY() < o2.getY() ? 1 : -1);
        });

        for (PointTypeSprite sprite : generatedSprites) {
            Image spriteImage = sprite.getImage(currentTile.rotation, tileSize, tileSize/textureManager.getMinTileSize());
            toDraw.addActor(spriteImage);
        }

        stage.addActor(toDraw);
    }

    private void drawMeeples(){
        Image mImage = new Image(
                new TextureRegionDrawable(new TextureRegion(
                        textureManager.getMeepleTexture(gameScreen.players.getCurrentPlayer().getColor(), 0)
                ))
        );
        mImage.setSize(50, 50);

        Label mCount = new Label(
                "x " + gameScreen.players.getCurrentPlayer().getMeepleCount(),
                skin,
                "big"
                );

        Stack stack = new Stack();
        stack.add(mCount);

        Table mTable = new Table(skin);
        mTable.setSize(100, 50);
        mTable.add(mImage).colspan(3).expand().width(mImage.getWidth()).height(mImage.getHeight());
        mTable.add(stack).colspan(5).expand();

        mTable.setPosition(CURR_TILE_X, CURR_TILE_Y + 200);

        stage.addActor(mTable);
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
                    Objects.equals(evt.getPropertyName(), "state")) {
                updateStage();
            } else if(Objects.equals(evt.getPropertyName(), "tilesSet")) {
                updateTilesLeftLabel(gameScreen.tilesTotal - (Integer) evt.getNewValue());
            }
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
