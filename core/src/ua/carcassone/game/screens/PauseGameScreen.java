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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.Utils;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;
import static ua.carcassone.game.Utils.ELEMENT_WIDTH_UNIT;

public class PauseGameScreen {

    public Stage stage;
    private Viewport viewport;
    private GameScreen gameScreen;
    private Skin skin;

    CheckBox noVolumeBox;
    Slider volumeSlider;
    Image background;

    public PauseGameScreen(GameScreen gameScreen){
        this.gameScreen = gameScreen;

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

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

        float cw = sw * 0.3f;
        float ch = sh * 1.1f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        Table table = new Table(skin);

        Label topLabel = new Label("MENU", skin, "big");
        topLabel.setAlignment(Align.center);
        Button resumeButton = makeResumeButton("RESUME");
        noVolumeBox = makeNoVolumeBox("Off volume");
        volumeSlider = makeVolumeSlider(10);
        Button exitButton = makeExitButton("EXIT");

        table.row().colspan(6).expandX().fillX();
        table.add(topLabel).fillX();
        table.row().colspan(6).expandX().fillX().pad(35);
        table.add(resumeButton).fillX();

        table.row().pad(35);
        table.add(noVolumeBox).colspan(1).expandX().fillX();
        table.add(volumeSlider).colspan(5).expandX().fillX();

        table.row().colspan(6).expandX().fillX().pad(30);
        table.add(exitButton).fillX();
        table.row().colspan(6).expandX().fillX().pad(30);

        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
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

    private CheckBox makeNoVolumeBox(String name){
        CheckBox noVolumeBox = new CheckBox(name, skin);
        if(gameScreen.game.getCurrMusicVolume() == 0){
            noVolumeBox.setChecked(true);
        }

        noVolumeBox.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(!noVolumeBox.isChecked());
                if(!noVolumeBox.isChecked()){
                    volumeSlider.setValue(0f);
                }
                else{
                    volumeSlider.setValue(gameScreen.game.getPrevMusicVolume() * 100);
                }
                gameScreen.game.setMusicVolume(volumeSlider.getValue() / 100);
                System.out.println(noVolumeBox.isChecked());
                return false;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {}
        });

        return noVolumeBox;
    }

    private Slider makeVolumeSlider(float stepSize){
        Slider volumeSlider = new Slider(0, 100, stepSize, false, skin);
        volumeSlider.setValue(gameScreen.game.getCurrMusicVolume() * 100);

        volumeSlider.addListener(new ClickListener() {

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                update();
                return true;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                update();
            }

            private void update(){
                gameScreen.game.setMusicVolume(volumeSlider.getValue() / 100);
                System.out.println(volumeSlider.getValue());

                boolean isZero = volumeSlider.getValue() == 0;
                noVolumeBox.setChecked(isZero);
            }
        });
        return volumeSlider;
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
                            try {
                                gameScreen.game.socketClient.leaveTable();
                            } catch (IncorrectClientActionException e) {
                                e.printStackTrace();
                            }
                            gameScreen.game.setScreen(new MainMenuScreen(gameScreen.game));
                            gameScreen.dispose();
                        }
                    }
                }.show(stage);
            }
        });
        return exitButton;
    }
}
