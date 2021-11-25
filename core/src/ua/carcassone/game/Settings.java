package ua.carcassone.game;

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

    public static float maxTranslationSpeed = 100;
    public static float translationSpeedDecrease = maxTranslationSpeed*0.95f;

    public static float maxZoomSpeed = 4;
    public static float zoomSpeedDecrease = maxZoomSpeed*2f;

    // чем больше тем больше можно будет отдалить
    public static float maxCameraZoom = 16f;

    // чем меньше тем больше можно будет приблизить
    public static float minCameraZoom = 0.4f;
}
