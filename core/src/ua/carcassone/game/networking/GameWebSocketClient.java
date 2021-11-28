package ua.carcassone.game.networking;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import ua.carcassone.game.Settings;
import ua.carcassone.game.game.*;
import ua.carcassone.game.game.Map;
import ua.carcassone.game.networking.ServerQueries.*;

public class GameWebSocketClient extends WebSocketClient {
    private PCLPlayers pclPlayers;
    private PCLCurrentTile pclCurrentTile;
    private Map relatedMap;
    private Tile cachedCurrentTile;
    private final Queue<ServerQueries.TILE_PUTTED.Tile> cachedPuttedTiles = new ArrayDeque<>();

    static class ClientState extends Observable{
        private ClientStateEnum state = ClientStateEnum.NOT_CONNECTED;

        public void set(ClientStateChange stateChange) {
            this.state = stateChange.newState;
            System.out.println("State changing to " + stateChange.newState);
            setChanged();
            notifyObservers(stateChange);
        }

        public void set(ClientStateEnum state){
            set(new ClientStateChange(state, null));
        }

        public boolean  is(ClientStateEnum state){
            return this.state == state;
        }

        public String string(){
            return this.state.name();
        }

        public ClientStateEnum state(){
            return this.state;
        }

    }

    public enum ClientStateEnum {
        NOT_CONNECTED,
        CONNECTING_TO_SERVER,
        CONNECTED_TO_SERVER,
        CONNECTING_TO_TABLE,
        CONNECTED_TO_TABLE,
        FAILED_TO_CONNECT_TO_TABLE,
        CREATING_TABLE,
        IN_GAME
    }

    public static class ClientStateChange{
        public ClientStateEnum newState;
        public Object additionalInfo;

        public ClientStateChange(ClientStateEnum newState, Object additionalInfo) {
            this.newState = newState;
            this.additionalInfo = additionalInfo;
        }
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
        } catch (IllegalArgumentException exception){
            return;
        }

        if (Objects.equals(action, JOIN_TABLE_SUCCESS.class.getSimpleName())){
            if(!this.state.is(ClientStateEnum.CONNECTING_TO_TABLE))
                System.out.println("! Server sent wrong response: \n\tstate is "+this.state.string()+"\n\tserver sent: "+message);

            JOIN_TABLE_SUCCESS response = jsonConverter.fromJson(JOIN_TABLE_SUCCESS.class, message);
            System.out.println(response.tableId+" - 1");
            this.state.set(new ClientStateChange(ClientStateEnum.CONNECTED_TO_TABLE, response.tableId));
        }

        else if (Objects.equals(action, JOIN_TABLE_FAILURE.class.getSimpleName())){
            if(!this.state.is(ClientStateEnum.CONNECTING_TO_TABLE))
                System.out.println("! Server sent wrong response: \n\tstate is "+this.state.string()+"\n\tserver sent: "+message);

            JOIN_TABLE_FAILURE response = jsonConverter.fromJson(JOIN_TABLE_FAILURE.class, message);
            this.state.set(ClientStateEnum.FAILED_TO_CONNECT_TO_TABLE);
        }

        else if (Objects.equals(action, CREATE_TABLE_SUCCESS.class.getSimpleName())){
            if(!this.state.is(ClientStateEnum.CREATING_TABLE))
                System.out.println("! Server sent wrong response: \n\tstate is "+this.state.string()+"\n\tserver sent: "+message);

            CREATE_TABLE_SUCCESS response = jsonConverter.fromJson(CREATE_TABLE_SUCCESS.class, message);
            this.state.set(new ClientStateChange(ClientStateEnum.CONNECTED_TO_TABLE, response.tableId));
        }

        else if (Objects.equals(action, GAME_STARTED.class.getSimpleName())) {
            if (!this.state.is(ClientStateEnum.CONNECTED_TO_TABLE))
                System.out.println("! Server sent wrong response: \n\tstate is " + this.state.string() + "\n\tserver sent: " + message);

            GAME_STARTED response = jsonConverter.fromJson(GAME_STARTED.class, message);
            this.state.set(new ClientStateChange(ClientStateEnum.IN_GAME, response.tiles));
        }

        else if (Objects.equals(action, PLAYER_JOINED.class.getSimpleName())) {
            if (!this.state.is(ClientStateEnum.CONNECTED_TO_TABLE))
                System.out.println("! Server sent wrong response: \n\tstate is " + this.state.string() + "\n\tserver sent: " + message);

            PLAYER_JOINED response = jsonConverter.fromJson(PLAYER_JOINED.class, message);

            if(this.pclPlayers == null){
                System.out.println("! WARNING: New player arrives, but not handled");
            } else {
                Gdx.app.postRunnable(() -> {
                    Player newPlayer = new Player(response.playerId, response.playerId, new Color(new Random().nextInt()));
                    pclPlayers.addPlayer(newPlayer);
                });

            }
        }

        else if (Objects.equals(action, PLAYER_LEFT.class.getSimpleName())) {
            if (!this.state.is(ClientStateEnum.CONNECTED_TO_TABLE) || !this.state.is(ClientStateEnum.IN_GAME))
                System.out.println("! Server sent wrong response: \n\tstate is " + this.state.string() + "\n\tserver sent: " + message);

            PLAYER_LEFT response = jsonConverter.fromJson(PLAYER_LEFT.class, message);

            if(this.pclPlayers == null){
                System.out.println("! WARNING: Player left, but not handled");
            } else {
                if(state.is(ClientStateEnum.IN_GAME))
                    Gdx.app.postRunnable(() -> pclPlayers.playerLeft(response.playerId));
                else
                    Gdx.app.postRunnable(() -> pclPlayers.removePlayer(response.playerId));
            }
        }

        else if (Objects.equals(action, TILE_DRAWN.class.getSimpleName())) {
            if (!this.state.is(ClientStateEnum.IN_GAME))
                System.out.println("! Server sent wrong response: \n\tstate is " + this.state.string() + "\n\tserver sent: " + message);

            TILE_DRAWN response = jsonConverter.fromJson(TILE_DRAWN.class, message);
            Tile tileGot = new Tile(TileTypes.get(response.tileType), 0);
            if(this.pclCurrentTile == null){
                System.out.println("! WARNING: Tile drawn, but not handled, caching");
                this.cachedCurrentTile = tileGot;
            } else {
                Gdx.app.postRunnable(() -> pclCurrentTile.setTile(tileGot));
            }
        }

        else if (Objects.equals(action, TILE_PUTTED.class.getSimpleName())) {
            if (!this.state.is(ClientStateEnum.IN_GAME))
                System.out.println("! Server sent wrong response: \n\tstate is " + this.state.string() + "\n\tserver sent: " + message);

            TILE_PUTTED response = jsonConverter.fromJson(TILE_PUTTED.class, message);

            if(this.relatedMap == null || this.pclPlayers == null){
                System.out.println("! WARNING: Tile put, but not handled, caching");
                this.cachedPuttedTiles.add(response.tile);
            } else {
                Gdx.app.postRunnable(() -> relatedMap.setByPlayer(response.tile));
            }
        }

        else if (Objects.equals(action, ERROR.class.getSimpleName())){
            ERROR response = jsonConverter.fromJson(ERROR.class, message);
            System.out.println("ERROR\n"+response.description.action);
        }

        else {
            System.out.println("Unknown command");
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

        this.state.set(ClientStateEnum.CONNECTING_TO_SERVER);
        this.connect();
    }

    public void connectToTable(String tableId) throws IncorrectClientActionException {
        if (!(this.state.is(ClientStateEnum.CONNECTED_TO_SERVER) || this.state.is(ClientStateEnum.CREATING_TABLE)))
            throw new IncorrectClientActionException("can not connect to a table as client state is " + this.state.string());

        this.sendJSON(new ClientQueries.JOIN_TABLE(tableId));
        this.state.set(ClientStateEnum.CONNECTING_TO_TABLE);
    }

    public void startGame() throws IncorrectClientActionException {
        if (!this.state.is(ClientStateEnum.CONNECTED_TO_TABLE))
            throw new IncorrectClientActionException("can not start game as client state is " + this.state.string());

        this.sendJSON(new ClientQueries.START_GAME("https://youtu.be/j5a0jTc9S10"));
    }

    public void leaveTable() throws IncorrectClientActionException {
        if (!this.state.is(ClientStateEnum.CONNECTED_TO_TABLE))
            throw new IncorrectClientActionException("can not leave table as client state is " + this.state.string());

        this.sendJSON(new ClientQueries.LEAVE_TABLE("i am leaving"));
        this.state.set(ClientStateEnum.CONNECTED_TO_SERVER);
    }

    public void createTable(String tableName) throws IncorrectClientActionException {
        if (!this.state.is(ClientStateEnum.CONNECTED_TO_SERVER))
            throw new IncorrectClientActionException("can not connect to a table as client state is " + this.state.string());

        this.sendJSON(new ClientQueries.CREATE_TABLE(tableName));
        this.state.set(ClientStateEnum.CREATING_TABLE);
    }

    public void restoreServerConnection() throws IncorrectClientActionException {
        System.out.println("Restoring connection");
        switch (this.state.state()){
            case NOT_CONNECTED:
            case CONNECTING_TO_SERVER:
                throw new IncorrectClientActionException("can not restore as client state is " + this.state.string());
            case CONNECTED_TO_SERVER:
                return;
            case CONNECTED_TO_TABLE:
                leaveTable();
            case CONNECTING_TO_TABLE:
                this.state.set(ClientStateEnum.CONNECTED_TO_SERVER);
            case FAILED_TO_CONNECT_TO_TABLE:
                this.state.set(ClientStateEnum.CONNECTED_TO_SERVER);
        }

    }

    public void addStateObserver(Observer observer){
        state.addObserver(observer);
    }

    public void setPCLPlayers(PCLPlayers pclPlayers){
        this.pclPlayers = pclPlayers;
        if(this.relatedMap != null && !cachedPuttedTiles.isEmpty()){
            while (!cachedPuttedTiles.isEmpty()){
                TILE_PUTTED.Tile tile = cachedPuttedTiles.remove();
                this.relatedMap.setByPlayer(tile);
            }
        }
    }

    public void setMap(Map relatedMap) {
        this.relatedMap = relatedMap;
        if(this.pclPlayers != null && !cachedPuttedTiles.isEmpty()){
            while (!cachedPuttedTiles.isEmpty()){
                TILE_PUTTED.Tile tile = cachedPuttedTiles.remove();
                this.relatedMap.setByPlayer(tile);
            }
        }
    }

    public void setPCLCurrentTile(PCLCurrentTile pclCurrentTile) {
        this.pclCurrentTile = pclCurrentTile;
        if (cachedCurrentTile != null){
            System.out.println("Uncaching tile to currentTile..");
            pclCurrentTile.setTile(cachedCurrentTile);
            cachedCurrentTile = null;
        }
    }

    /**
     * Observer which accepts all states, and accepts and deletes itself after getting a stoppingState (if provided).
     */
    public static class stateMultipleObserver implements Observer {

        Consumer<ClientStateChange> consumer;
        ClientStateEnum stoppingState = null;

        @Override
        public void update(Observable o, Object state) {
            consumer.accept((ClientStateChange) state);
            if (((ClientStateChange) state).newState == stoppingState)
                o.deleteObserver(this);
        }

        public stateMultipleObserver(Consumer<ClientStateChange> consumer){
            this.consumer = consumer;
        }

        public stateMultipleObserver(ClientStateEnum stoppingState, Consumer<ClientStateChange> consumer){
            this.consumer = consumer;
            this.stoppingState = stoppingState;
        }
    }

    /**
     * Observer which accepts a state, and deletes itself.
     */
    public static class stateSingleObserver implements Observer {
        Consumer<ClientStateChange> consumer;

        @Override
        public void update(Observable o, Object state) {
            consumer.accept((ClientStateChange) state);
            o.deleteObserver(this);
        }

        public stateSingleObserver(Consumer<ClientStateChange> consumer){
            this.consumer = consumer;
        }

    }

    /**
     * Observer which accepts all states from "acceptable" list, and deletes itself after getting stoppingState or a state not in "acceptable" list.
     */
    public static class stateAcceptableObserver implements Observer {
        Consumer<ClientStateChange> consumer;
        List<ClientStateEnum> acceptable;
        ClientStateEnum stoppingState;

        @Override
        public void update(Observable o, Object state) {
            if (acceptable.contains(((ClientStateChange) state).newState)){
                consumer.accept((ClientStateChange) state);
                if (((ClientStateChange) state).newState == stoppingState)
                    o.deleteObserver(this);
            } else {
                o.deleteObserver(this);
            }

        }

        public stateAcceptableObserver(ClientStateEnum stoppingState, List<ClientStateEnum> acceptable,
                                       Consumer<ClientStateChange> consumer){
            this.stoppingState = stoppingState;
            this.acceptable = new ArrayList<>(acceptable);

            if(!this.acceptable.contains(this.stoppingState))
                this.acceptable.add(this.stoppingState);

            this.consumer = consumer;
        }

    }

    public void sendJSON(Object o){
        this.send(jsonConverter.toJson(o));
    }
    public ClientStateEnum getState(){
        return this.state.state();
    }
    public boolean isConnected(){
        return !this.state.is(ClientStateEnum.NOT_CONNECTED);
    }
}
