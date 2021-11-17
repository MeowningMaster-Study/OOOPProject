package ua.carcassone.game;

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
}
