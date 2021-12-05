package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.carcassone.game.CarcassoneGame;

import static ua.carcassone.game.Utils.ELEMENT_HEIGHT_UNIT;

public class SettingsScreen implements Screen {

    private final CarcassoneGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    CheckBox noVolumeBox;
    Slider volumeSlider;

    private Skin skin;

    public SettingsScreen(final CarcassoneGame game) {
        this.game = game;

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        viewport = new FitViewport(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/comic-ui.json"));

        addImages();

        Container<Table> tableContainer = new Container<>();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.3f;
        float ch = sh * 1.1f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        Table table = new Table(skin);

        Label topLabel = new Label("SETTINGS", skin, "big");
        topLabel.setAlignment(Align.center);
        noVolumeBox = makeNoVolumeBox("Off volume");
        volumeSlider = makeVolumeSlider(10);
        Button backButton = makeBackButton("BACK");

        table.row().colspan(6).expandX().fillX();
        table.add(topLabel).fillX();

        table.row().pad(35);
        table.add(noVolumeBox).colspan(1).expandX().fillX();
        table.add(volumeSlider).colspan(5).expandX().fillX();

        table.row().colspan(6).expandX().fillX().pad(30);
        table.add(backButton).fillX();
        table.row().colspan(6).expandX().fillX().pad(30);

        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);

    }

    private void addImages(){
        Image image1 = new Image(new Texture("skins/images/bandit.png"));
        image1.setPosition((Gdx.graphics.getDisplayMode().width*0.2f - image1.getWidth()/2f), (Gdx.graphics.getDisplayMode().height - image1.getHeight())/2.0f);
        stage.addActor(image1);

        Image image2 = new Image(new Texture("skins/images/bandit2.png"));
        image2.setPosition((Gdx.graphics.getDisplayMode().width*0.8f - image2.getWidth()/2f), (Gdx.graphics.getDisplayMode().height - image2.getHeight())/2.0f);
        stage.addActor(image2);
    }

    private Button makeBackButton(String name){
        Button resumeButton = new TextButton(name, skin);
        resumeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        return resumeButton;
    }

    private CheckBox makeNoVolumeBox(String name){
        CheckBox noVolumeBox = new CheckBox(name, skin);
        if(game.getCurrMusicVolume() == 0){
            noVolumeBox.setChecked(true);
        }

        noVolumeBox.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(!noVolumeBox.isChecked());
                if(!noVolumeBox.isChecked()){
                    volumeSlider.setValue(0f);
                }
                else{
                    volumeSlider.setValue(game.getPrevMusicVolume() * 100);
                }
                game.setMusicVolume(volumeSlider.getValue() / 100);
                System.out.println(noVolumeBox.isChecked());
                return false;
            }
        });

        return noVolumeBox;
    }

    private Slider makeVolumeSlider(float stepSize){
        Slider volumeSlider = new Slider(0, 100, stepSize, false, skin);
        volumeSlider.setValue(game.getCurrMusicVolume() * 100);

        volumeSlider.addListener(new ClickListener() {

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                update();
                return true;
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                update();
            }

            private void update(){
                game.setMusicVolume(volumeSlider.getValue() / 100);
                System.out.println(volumeSlider.getValue());

                boolean isZero = volumeSlider.getValue() == 0;
                noVolumeBox.setChecked(isZero);
            }
        });
        return volumeSlider;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(246f/255, 222f/255, 174f/255, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
