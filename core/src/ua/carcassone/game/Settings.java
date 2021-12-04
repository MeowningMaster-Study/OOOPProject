package ua.carcassone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.net.URI;
import java.net.URISyntaxException;

public class Settings {
    private static URI serverURI = null;

    static {
        try {
            serverURI = new URI("wss://carcassonne-server.deno.dev/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static URI getServerURI(){
        return serverURI;
    }

    public static final Vector2 fieldTileCount = new Vector2(143, 143);

    public static float maxTranslationSpeed = 160f;
    public static float maxTranslationSpeedTilesPower = 1f;
    public static float translationSpeedDecrease = maxTranslationSpeed*1.35f;
    public static float shiftTranslationCoefficient = 1.2f;

    public static float maxZoomSpeed = 4;
    public static float zoomSpeedDecrease = maxZoomSpeed*4.2f;

    // чем больше тем больше можно будет отдалить
    public static float maxCameraZoom = 5.0f;
    public static float maxCameraZoomTilesPower = 0.6f;

    // чем меньше тем больше можно будет приблизить
    public static float minCameraZoom = 2.0f;

    // процент расстояния который может занимать пустой стол
    public static final float possibleEmptyCameraPercent = 0.95f;

    // the starting volume of the music. value from 0 to 1.
    public static final float startingMusicVolume = 0.0f;

    public static float pointRadius = 0.07f;
    public static float meepleHeight = 0.25f;

    public static float spritesPerTile = 30;
    public static float polygonScale = 100;

    public static final Color meepleGreenscreenColor = new Color(0.0f, 1.0f, 0.0f, 1.0f);
}
