package ua.carcassone.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ua.carcassone.game.screens.MainMenuScreen;

public class CarcassoneGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Screen mainMenuScreen, joinGameScreen, gameScreen;

	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		mainMenuScreen = new MainMenuScreen(this);
		this.setScreen(mainMenuScreen);

	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
		mainMenuScreen.dispose();
	}
}
