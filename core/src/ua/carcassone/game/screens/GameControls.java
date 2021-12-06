package ua.carcassone.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Settings;

public class GameControls implements InputProcessor {
    private final GameScreen gameScreen;
    private final Vector2 prevMousePosition = new Vector2(0,0);

    public GameControls(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        gameScreen.inputMultiplexer.addProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ENTER) {
            if(gameScreen.currentTile.isPut())
                gameScreen.gameLogic.confirmSelectedTilePosition();
            else if(gameScreen.currentTile.isPlaceMeeple())
                gameScreen.gameLogic.confirmSelectedTileMeeples();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){
            gameScreen.field.translateCamera(new Vector2(
                    prevMousePosition.x - screenX,
                    screenY - prevMousePosition.y
            ));
        }
        prevMousePosition.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        gameScreen.setDebugLabel(screenX+" "+screenY);

        prevMousePosition.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        gameScreen.field.setZoomSpeed(amountY * Settings.mouseScrollSpeed);
        return false;
    }
}
