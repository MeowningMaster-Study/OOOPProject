package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
import ua.carcassone.game.game.Score;
import ua.carcassone.game.game.TileTextureManager;
import ua.carcassone.game.networking.IncorrectClientActionException;

import java.util.*;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class EndGameScreen {
    public Stage stage;
    private Viewport viewport;
    private final GameScreen gameScreen;
    private final Skin skin;

    TileTextureManager textureManager;

    private final Image background;
    private final Image scorePaper;
    private final Image wantedPaper;

    private final Button leftButton;
    private final Button rightButton;

    private final Table buttonTable;
    Container<Table> scoreContainer;
    Container<Table> wantedContainer;

    private final ArrayList<Page> pages;
    private int pageIndex;


    public EndGameScreen(GameScreen gameScreen, Map<Player, Score> pToS){
        this.gameScreen = gameScreen;

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        textureManager = new TileTextureManager();
        pageIndex = 0;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.getColor().a = 0.6f;

        pages = paginate(pToS);

        wantedPaper = new Image(new Texture("skins/icons/wantedPaper.png"));
        wantedPaper.setSize(600, 800);
        wantedPaper.setPosition(ELEMENT_WIDTH_UNIT, (Gdx.graphics.getHeight() - wantedPaper.getHeight())/2 );

        scorePaper = new Image(new Texture("skins/icons/simplePaper.png"));
        scorePaper.setSize(600, 800);
        scorePaper.setPosition(Utils.fromRight(ELEMENT_WIDTH_UNIT + scorePaper.getWidth()), (Gdx.graphics.getHeight() - scorePaper.getHeight())/2 );

        buttonTable = new Table(skin);

        Button exitButton = makeExitButton("Exit game");
        leftButton = makeLeftButton("skins/icons/leftArrow.png");
        rightButton = makeRightButton("skins/icons/rightArrow.png");

        buttonTable.row().colspan(3).expandX().fillX();
        buttonTable.add(leftButton).padRight(20).height(leftButton.getHeight()).width(leftButton.getWidth());
        buttonTable.add(exitButton).height(exitButton.getHeight()).width(exitButton.getWidth());
        buttonTable.add(rightButton).padLeft(20).height(rightButton.getHeight()).width(rightButton.getWidth());

        wantedContainer = new Container<>();
        wantedContainer.fillX();

        wantedContainer.setSize(wantedPaper.getWidth(), wantedPaper.getHeight());
        wantedContainer.setPosition(wantedPaper.getX(), wantedPaper.getY());

        scoreContainer = new Container<>();
        scoreContainer.fillX();

        scoreContainer.setSize(scorePaper.getWidth(), scorePaper.getHeight());
        scoreContainer.setPosition(scorePaper.getX(), wantedPaper.getY());

        updateStage();
    }

    private ArrayList<Page> paginate(Map<Player, Score> ps){
        ArrayList<Page> pages = new ArrayList<>();

        for(Map.Entry<Player, Score> p : ps.entrySet()){
            Page page = new Page(p.getKey(), p.getValue());
            pages.add(page);
        }

        Collections.sort(pages, new Comparator<Page>() {
            @Override
            public int compare(Page lhs, Page rhs) {
                return rhs.score.summary.compareTo(lhs.score.summary);
            }
        });

        return pages;
    }

    private ImageButton getImageButton(String path){
        Texture imageTexture = new Texture(path);
        Drawable imageDrawable = new TextureRegionDrawable(new TextureRegion(imageTexture));
        return new ImageButton(imageDrawable);
    }

    private Button makeRightButton(String path){
        ImageButton rightButton = getImageButton(path);
        rightButton.setSize(100, 70);

        if(pageIndex == pages.size() - 1){
            rightButton.setVisible(false);
            rightButton.setDisabled(true);
        }

        rightButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(pageIndex < pages.size()){
                    pageIndex++;
                    updateStage();
                }

                if(pageIndex == pages.size() - 1 && !rightButton.isDisabled()){
                    rightButton.setVisible(false);
                    rightButton.setDisabled(true);
                }
                else if(pageIndex != 0 && leftButton.isDisabled()){
                    leftButton.setVisible(true);
                    leftButton.setDisabled(false);
                }
            }
        });

        return rightButton;
    }

    private Button makeLeftButton(String path){
        ImageButton leftButton = getImageButton(path);
        leftButton.setSize(100, 70);

        if(pageIndex == 0){
            leftButton.setVisible(false);
            leftButton.setDisabled(true);
        }

        leftButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(pageIndex > 0){
                    pageIndex--;
                    updateStage();
                }

                if(pageIndex == 0 && !leftButton.isDisabled()){
                    leftButton.setVisible(false);
                    leftButton.setDisabled(true);
                }
                else if(pageIndex != pages.size() - 1 && rightButton.isDisabled()){
                    rightButton.setVisible(true);
                    rightButton.setDisabled(false);
                }
            }
        });

        return leftButton;
    }

    private Button makeExitButton(String name){
        Button exitButton = new TextButton(name, skin);
        exitButton.setSize(200, 70);

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    gameScreen.game.socketClient.leaveTable();
                } catch (IncorrectClientActionException e) {
                    e.printStackTrace();
                }
                gameScreen.game.setScreen(new MainMenuScreen(gameScreen.game));
                gameScreen.dispose();
            }
        });

        return exitButton;
    }

    private void updateStage(){
        stage.clear();

        stage.addActor(background);

        stage.addActor(scorePaper);
        stage.addActor(wantedPaper);


        Page currPage = pages.get(pageIndex);

        Table wantedTable = currPage.getPlayerTable(skin);
        wantedTable.add(buttonTable);

        wantedContainer.setActor(wantedTable);

        stage.addActor(wantedContainer);


        Table scoreTable = currPage.getScoreTable(skin);
        scoreContainer.setActor(scoreTable);

        stage.addActor(scoreContainer);
    }

    class Page{
        public Player player;
        public Score score;

        public Page(Player player, Score score){
            this.player = player;
            this.score = score;
        }

        public Table getPlayerTable(Skin skin){
            Table table = new Table(skin);

            Image pImage = new Image(textureManager.getProfileTransparent(player.getColor()));
            pImage.setSize(250, 250);
            Label pName = new Label(player.getName(), skin, "big");

            table.row().expandX().padTop(150).fillX();
            table.add(pImage).height(pImage.getHeight()).width(pImage.getWidth());
            table.row().expandX().padTop(50).fillX();
            table.add(pName).height(pName.getHeight()).width(pName.getWidth());
            table.row().expandX().fillX().padTop(120).padLeft(150).padRight(150).height(70).width(100);

            table.setSize(220, 240);

            return table;
        }

        public Table getScoreTable(Skin skin){
            Table table = new Table(skin);

            int p = pages.indexOf(this);
            Label place = new Label(p == 0 ?
                    "Winner" : "#" + (p + 1) + " place", skin, "big");


            Label roads = new Label("Roads", skin, "big");
            Label castles = new Label("Castles", skin, "big");
            Label monasteries = new Label("Monasteries", skin, "big");
            Label fields = new Label("Fields", skin, "big");
            Label summary = new Label("Summary", skin, "big");

            Label rScore = new Label(score.roads.toString(), skin, "big");
            Label cScore = new Label(score.castles.toString(), skin, "big");
            Label mScore = new Label(score.monasteries.toString(), skin, "big");
            Label fScore = new Label(score.fields.toString(), skin, "big");
            Label summaryScore = new Label(score.summary.toString(), skin, "big");

            table.row().colspan(3).expandX().fillX().padTop(50).padLeft(40).padRight(40).height(30);
            table.add(place).fillX().width(place.getWidth());

            table.row().colspan(2).expandX().fillX().padTop(70).padLeft(40).padRight(40).height(30);
            table.add(roads).fillX().width(roads.getWidth());
            table.add(rScore).fillX().width(rScore.getWidth());

            table.row().colspan(2).expandX().fillX().padTop(70).padLeft(40).padRight(40).height(30);
            table.add(castles).fillX().width(castles.getWidth());
            table.add(cScore).fillX().width(cScore.getWidth());

            table.row().colspan(2).expandX().fillX().padTop(70).padLeft(40).padRight(40).height(30);
            table.add(monasteries).fillX().width(monasteries.getWidth());
            table.add(mScore).fillX().width(mScore.getWidth());

            table.row().colspan(2).expandX().fillX().padTop(70).padLeft(40).padRight(40).height(30);
            table.add(fields).fillX().width(fields.getWidth());
            table.add(fScore).fillX().width(fScore.getWidth());

            table.row().colspan(2).expandX().fillX().padTop(70).padLeft(40).padRight(40).padBottom(70).height(30);
            table.add(summary).fillX().width(summary.getWidth());
            table.add(summaryScore).fillX().width(summaryScore.getWidth());

            return table;
        }
    }

}
