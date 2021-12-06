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
    Table ccButtons;
    boolean isOverThePlayers;
    private Label tilesLeftLabel;

    TileTextureManager textureManager;
    CurrentTileObserver currentTileObserver;
    CurrentPlayerObserver currentPlayerObserver;

    final float CURR_TILE_X = Gdx.graphics.getWidth() - (float) (ELEMENT_WIDTH_UNIT * 1.5);
    final float CURR_TILE_Y = (float) (ELEMENT_HEIGHT_UNIT * 0.5);

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

        ccButtons = new Table(skin);
        ccButtons.row().fillX().expandX().height(cancelButton.getHeight());
        ccButtons.add(cancelButton).width(cancelButton.getWidth()).padRight(20);
        ccButtons.add(confirmationButton).width(confirmationButton.getWidth());

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

        confirmationButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(gameScreen.currentTile.isPut()){
                    gameScreen.gameLogic.confirmSelectedTilePosition();

                    cancelButton.setDisabled(false);
                    cancelButton.setVisible(true);
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

        cancelButton.setDisabled(true);
        cancelButton.setVisible(false);

        cancelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.gameLogic.disproveSelectedTileMeeples();

                cancelButton.setDisabled(true);
                cancelButton.setVisible(false);
            }
        });

        return cancelButton;
    }

    private Label makeTilesLeftLabel(){
        Label tilesLeftLabel = new Label("Tiles left: "+this.gameScreen.tilesTotal, skin, "big");
        //tilesLeftLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        //tilesLeftLabel.setPosition(ELEMENT_WIDTH_UNIT * 0.3f, (ELEMENT_HEIGHT_UNIT) * 0.5f);
        return tilesLeftLabel;
    }

    private void updateTilesLeftLabel(int left){
        tilesLeftLabel.setText("Tiles left: "+left);
    }

    public void updateStage(){
        stage.clear();

        if(gameScreen.currentTile.isSet()){
            drawCurrentTile();

            if(!gameScreen.currentTile.isHanging()){
                drawButtons();
            }

            if(gameScreen.currentTile.isPlaceMeeple()){
                drawMeeples();
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
        Table infoTable = new Table(skin);

        int iconWidth = 100;
        int iconHeight = 100;
        int topPadding = 10;

        for (int i = 0; i < size; ++i) {

            playersTable.row().fillX();
            if(i != 0){
                playersTable.padTop(topPadding);
            }

            Player player = gameScreen.players.getPlayers().get(i);

            Image pImage;
            if(gameScreen.players.isTurnOf(player)){
                pImage = new Image(textureManager.getProfileGolden(player.getColor()));
            } else {
                pImage = new Image(textureManager.getProfileSilver(player.getColor()));
            }

            pImage.setSize(iconWidth, iconHeight);
            playersTable.add(pImage).width(pImage.getWidth()).height(pImage.getHeight());


            Label pName = new Label(
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


            infoTable.row().fillX();
            if(i != 0){
                infoTable.padTop(topPadding + iconHeight % 3);
            }

            infoTable.add(pName).expandX().fillX().height(iconHeight / 3).width(iconWidth);
            infoTable.row().height(iconHeight / 3).width(iconWidth);
            infoTable.add(pMeeples).expandX().fillX();
            infoTable.row().height(iconHeight / 3).width(iconWidth);
            infoTable.add(pScore).expandX().fillX();
        }

        Container<Table> playersTableContainer = new Container<>();
        playersTableContainer.setActor(playersTable);
        playersTableContainer.setSize(iconWidth, size * (iconWidth + topPadding));
        playersTableContainer.setPosition(ELEMENT_WIDTH_UNIT * 0.3f, Utils.fromTop(playersTableContainer.getHeight() + 50));

        playersTableContainer.addListener(new ClickListener(){

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if(!infoTable.isVisible() && isOver()){
                    infoTable.setVisible(true);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.exit(event, x, y, pointer, fromActor);
                if(infoTable.isVisible() && !isOver()){
                    infoTable.setVisible(false);
                }
            }
        });

        stage.addActor(playersTableContainer);

        infoTable.setVisible(false);

        Container<Table> infoTableContainer = new Container<>();
        infoTableContainer.setActor(infoTable);
        infoTableContainer.setSize(playersTableContainer.getWidth(),playersTableContainer.getHeight());
        infoTableContainer.setPosition(
                playersTableContainer.getX() + playersTableContainer.getWidth() + 5,
                playersTableContainer.getY()
        );

        stage.addActor(infoTableContainer);
    }

    private void drawCurrentTile(){
        final float tileSize = 130;

        Image rectanglePlank = new Image(new Texture("skins/icons/rectangle-plank.png"));
        rectanglePlank.setSize(tileSize * 2 + 170, tileSize + 35);
        rectanglePlank.setPosition(ELEMENT_WIDTH_UNIT * 0.3f, (ELEMENT_HEIGHT_UNIT) * 0.5f);

        stage.addActor(rectanglePlank);

        Tile currentTile = gameScreen.currentTile.getCurrentTile();
        Group toDraw = new Group();
        toDraw.setSize(tileSize, tileSize);

        Image currentTileImage = new Image(textureManager.getTexture(currentTile));
        currentTileImage.setSize(tileSize, tileSize);

        Image border = new Image(textureManager.getBorderSilverTexture());
        border.setSize(tileSize, tileSize);

        Stack stack = new Stack();
        stack.setSize(tileSize, tileSize);
        stack.add(currentTileImage);
        stack.add(border);


        toDraw.addActor(stack);

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

        Table tileTable = new Table(skin);
        tileTable.row().fillX().expandX().height(currentTileImage.getHeight());
        tileTable.add(toDraw).padRight(20);
        tileTable.add(tilesLeftLabel);

        Container<Table> angleContainer = new Container<>();
        angleContainer.setActor(tileTable);
        angleContainer.setPosition(rectanglePlank.getX(), rectanglePlank.getY());
        angleContainer.setSize(rectanglePlank.getWidth(), rectanglePlank.getHeight());

        stage.addActor(angleContainer);
    }

    private void drawMeeples(){
        Image rectanglePlank = new Image(new Texture("skins/icons/rectangle-plank.png"));
        rectanglePlank.setSize(cancelButton.getWidth() * 2 + 170, 100);
        rectanglePlank.setPosition(Utils.fromRight(rectanglePlank.getWidth() + 40), CURR_TILE_Y + 200);

        stage.addActor(rectanglePlank);

        Image mImage = new Image(
                new TextureRegionDrawable(new TextureRegion(
                        textureManager.getMeepleTexture(gameScreen.players.getCurrentPlayer().getColor(), 0)
                ))
        );
        mImage.setSize(70, 70);

        Label mCount = new Label(
                "x " + gameScreen.players.getCurrentPlayer().getMeepleCount(),
                skin,
                "big"
        );

        Table mTable = new Table(skin);
        mTable.row().fillX().expandX().height(mImage.getHeight());
        mTable.add(mImage).width(mImage.getWidth()).padRight(20);
        mTable.add(mCount).width(mCount.getWidth());

        Container<Table> angleContainer = new Container<>();
        angleContainer.setActor(mTable);
        angleContainer.setPosition(rectanglePlank.getX(), rectanglePlank.getY());
        angleContainer.setSize(rectanglePlank.getWidth(), rectanglePlank.getHeight());

        stage.addActor(angleContainer);
    }

    private void drawButtons(){
        Image anglePlank = new Image(new Texture("skins/icons/angle-plank.png"));
        anglePlank.setSize(cancelButton.getWidth() * 2 + 170, cancelButton.getWidth() + 30);
        anglePlank.setPosition(Utils.fromRight(anglePlank.getWidth() + 40), CURR_TILE_Y);

        stage.addActor(anglePlank);

        Container<Table> angleContainer = new Container<>();
        angleContainer.setActor(ccButtons);
        angleContainer.setPosition(anglePlank.getX(), anglePlank.getY());
        angleContainer.setSize(anglePlank.getWidth(), anglePlank.getHeight());

        if(!gameScreen.currentTile.isPlaceMeeple()){
            cancelButton.setDisabled(true);
            cancelButton.setVisible(false);
        }

        stage.addActor(angleContainer);
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
