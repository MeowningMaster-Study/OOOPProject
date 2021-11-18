package ua.carcassone.game.networking;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import ua.carcassone.game.Settings;
import ua.carcassone.game.networking.ServerQueries.*;

public class GameWebSocketClient extends WebSocketClient {

    static class ClientState extends Observable{
        private ClientStateEnum state = ClientStateEnum.NOT_CONNECTED;

        public void set(ClientStateEnum state){
            this.state = state;
            setChanged();
            notifyObservers(state);
        }

        public boolean is(ClientStateEnum state){
            return this.state == state;
        }

        public String string(){
            return this.state.name();
        }

    }

    enum ClientStateEnum {
        NOT_CONNECTED,
        CONNECTING_TO_SERVER,
        CONNECTED_TO_SERVER,
        CONNECTING_TO_TABLE,
        CONNECTED_TO_TABLE,
    }

    private final ClientState state = new ClientState();
    private final Json jsonConverter = new Json();

    public GameWebSocketClient() {
        super(Settings.getServerURI());
        jsonConverter.setOutputType(JsonWriter.OutputType.json);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        //if (!this.state.is(ClientStateEnum.CONNECTING_TO_SERVER))
        //    throw new IncorrectClientActionException("connection opened but the client is in state "+this.state.string());
        System.out.println("OPENED");
        this.state.set(ClientStateEnum.CONNECTED_TO_SERVER);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);

        String action;
        try{
            JsonValue fromJson = new JsonReader().parse(message);
            action = fromJson.getString("action");
            System.out.println("action is "+action);
        } catch (IllegalArgumentException exception){
            return;
        }

        if (Objects.equals(action, JOIN_TABLE_SUCCESS.class.getSimpleName())){
            JOIN_TABLE_SUCCESS response = jsonConverter.fromJson(JOIN_TABLE_SUCCESS.class, message);
            this.state.set(ClientStateEnum.CONNECTED_TO_TABLE);
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public void connectToServer() throws IncorrectClientActionException {
        if (!this.state.is(ClientStateEnum.NOT_CONNECTED))
            throw new IncorrectClientActionException("client is already connected to a server");
        this.connect();
        this.state.set(ClientStateEnum.CONNECTING_TO_SERVER);
    }

    public void connectToTable(String table_id) throws IncorrectClientActionException {
        if (!this.state.is(ClientStateEnum.CONNECTED_TO_SERVER))
            throw new IncorrectClientActionException("can not connect to a table as client state is "+this.state.string());

        this.send(jsonConverter.toJson(new ClientQueries.JOIN_TABLE(table_id)));

        this.state.set(ClientStateEnum.CONNECTING_TO_TABLE);
    }

    public void addStateObserver(Observer observer){
        state.addObserver(observer);
    }

    public static class onStateChangedObserver implements Observer {
        Runnable runnable;
        @Override
        public void update(Observable o, Object state) {
            runnable.run();
        }

        public onStateChangedObserver(Runnable runnable){
            this.runnable = runnable;
        }
    }
}
