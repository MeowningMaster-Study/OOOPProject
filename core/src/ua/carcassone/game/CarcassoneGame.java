package ua.carcassone.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;
import ua.carcassone.game.screens.MainMenuScreen;

import java.util.ArrayList;

import static ua.carcassone.game.Settings.startingMusicVolume;

public class CarcassoneGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public GameWebSocketClient socketClient = new GameWebSocketClient();
	public ArrayList<Music> musicPlaylist;

	public float prevMusicVolume = startingMusicVolume;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		musicPlaylist = new ArrayList<>();
		musicPlaylist.add(Gdx.audio.newMusic(Gdx.files.internal("musics/medieval-1.mp3")));
		musicPlaylist.add(Gdx.audio.newMusic(Gdx.files.internal("musics/medieval-2.mp3")));

		musicPlaylist.get(0).play();

		for(int i = 0; i < musicPlaylist.size(); ++i){
			int finalI = i;
			musicPlaylist.get(i).setVolume(startingMusicVolume);
			musicPlaylist.get(i).setOnCompletionListener(music -> {
				if(finalI == musicPlaylist.size() - 1){
					musicPlaylist.get(0).play();
				}
				else{
					musicPlaylist.get(finalI + 1).play();
				}
			});
		}

		try {
			this.socketClient.connectToServer();
		} catch (IncorrectClientActionException e) {
			e.printStackTrace();
		}

		this.setScreen(new MainMenuScreen(this));

	}

	public void setMusicVolume(float volume){
		prevMusicVolume = musicPlaylist.get(0).getVolume();
		for (Music music : musicPlaylist) {
			music.setVolume(volume);
		}
	}

	public float getPrevMusicVolume() {
		return prevMusicVolume;
	}

	public float getCurrMusicVolume() {
		return musicPlaylist.get(0).getVolume();
	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
	}



}
