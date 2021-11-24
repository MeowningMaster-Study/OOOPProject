package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ua.carcassone.game.Utils;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class GameHud {
    public Stage stage;
    public SpriteBatch spriteBatch;
    private GameScreen gameScreen;

    public GameHud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(viewport, gameScreen.game.batch);

        Skin mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Label carcassoneLabel = new Label("Join game", mySkin, "big");
        carcassoneLabel.setSize(ELEMENT_WIDTH_UNIT, ELEMENT_HEIGHT_UNIT);
        carcassoneLabel.setPosition(ELEMENT_WIDTH_UNIT, Utils.fromTop(ELEMENT_HEIGHT_UNIT * 2));
        stage.addActor(carcassoneLabel);
    }


}
