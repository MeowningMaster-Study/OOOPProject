package ua.carcassone.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;
import ua.carcassone.game.screens.MainMenuScreen;

import java.util.Observable;
import java.util.Observer;

public class CarcassoneGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public GameWebSocketClient socketClient = new GameWebSocketClient();

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		try {
			this.socketClient.connectToServer();
		} catch (IncorrectClientActionException e) {
			e.printStackTrace();
		}

		this.setScreen(new MainMenuScreen(this));

	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
	}


}
