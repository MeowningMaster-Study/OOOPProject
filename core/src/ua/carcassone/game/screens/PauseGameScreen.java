package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Utils;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class PauseGameScreen {

    public Stage stage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin skin;

    Image background;

    public PauseGameScreen(GameScreen gameScreen){
        this.gameScreen = gameScreen;

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.getColor().a = 0.6f;
        stage.addActor(background);

        Container<Table> tableContainer = new Container<>();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.5f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        Table table = new Table(skin);

        Label topLabel = new Label("MENU", skin, "big");
        topLabel.setAlignment(Align.center);
        Button resumeButton = makeResumeButton("RESUME");
        Slider soundVolume = new Slider(0, 100, 1, false, skin);
        Button exitButton = makeExitButton("EXIT");

        table.row().colspan(5).expandX().fillX();
        table.add(topLabel).fillX();
        table.row().colspan(5).expandX().fillX();
        table.add(resumeButton).fillX();
        table.row().colspan(5).expandX().fillX();
        table.add(soundVolume).fillX();
        table.row().colspan(5).expandX().fillX();
        table.add(exitButton).fillX();
        table.row().colspan(5).expandX().fillX();

        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    private Button makeExitButton(String name){
        Button exitButton = new TextButton(name, skin);
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                new Dialog("Do you really want to leave", skin){

                    {
                        button("Yes", true);
                        button("No", false);
                    }

                    @Override
                    protected void result(final Object object) {
                        if(object.equals(true)){
                            // exit table
                        }
                        else{
                            // nothing
                        }
                    }
                }.show(stage);
            }
        });
        return exitButton;
    }

    private Button makeResumeButton(String name){
        Button resumeButton = new TextButton(name, skin);
        resumeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.isPaused = false;
                gameScreen.hud.resume();
                gameScreen.hud.updateStage();
            }
        });
        return resumeButton;
    }
}
