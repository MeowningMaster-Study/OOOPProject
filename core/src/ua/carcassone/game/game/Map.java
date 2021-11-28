package ua.carcassone.game.game;

import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Settings;
import ua.carcassone.game.networking.GameWebSocketClient;
import ua.carcassone.game.networking.IncorrectClientActionException;
import ua.carcassone.game.networking.ServerQueries;
import ua.carcassone.game.screens.GameField;
import ua.carcassone.game.screens.GameHud;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {
    private final Tile[][] map;
    private final int zeroTileXPositionInArray;
    private final int zeroTileYPositionInArray;
    private final Vector2 size;
    private int tilesCount;
    private final Vector2 minOccupiedCoordinate;
    private final Vector2 maxOccupiedCoordinate;
    private Vector2 selectedTileCoordinate;
    private final List<GameField> linkedGameFields = new LinkedList<>();
    private final List<GameHud> linkedGameHuds = new LinkedList<>();
    private PCLPlayers relatedPlayers;
    private GameWebSocketClient relatedClient;

    public Map(Tile tile, int columns, int rows) {
        this.map = new Tile[rows][columns];
        this.zeroTileXPositionInArray = rows/2;
        this.zeroTileYPositionInArray = columns/2;
        this.size = new Vector2(columns, rows);
        this.maxOccupiedCoordinate = new Vector2(0, 0);
        this.minOccupiedCoordinate = new Vector2(0, 0);
        set(0, 0, tile);
//        print("Constructed");
    }

    public Map(Tile tile) {
        this.map = new Tile[(int) Settings.fieldTileCount.y][(int) Settings.fieldTileCount.x];
        this.zeroTileXPositionInArray = (int) Settings.fieldTileCount.y/2;
        this.zeroTileYPositionInArray = (int) Settings.fieldTileCount.x/2;
        this.size = new Vector2((int) Settings.fieldTileCount.x, (int) Settings.fieldTileCount.y);
        this.maxOccupiedCoordinate = new Vector2(0, 0);
        this.minOccupiedCoordinate = new Vector2(0, 0);
        set(0, 0, tile);
    }

    public Map() {
        this.map = new Tile[(int) Settings.fieldTileCount.y][(int) Settings.fieldTileCount.x];
        this.zeroTileXPositionInArray = (int) Settings.fieldTileCount.y/2;
        this.zeroTileYPositionInArray = (int) Settings.fieldTileCount.x/2;
        this.size = new Vector2((int) Settings.fieldTileCount.x, (int) Settings.fieldTileCount.y);
        this.maxOccupiedCoordinate = new Vector2(0, 0);
        this.minOccupiedCoordinate = new Vector2(0, 0);
    }

    public Tile get(int x, int y){
        return map[zeroTileXPositionInArray-y][zeroTileYPositionInArray+x];
    }

    public Tile get(Vector2 pos){
        return get((int) pos.x, (int) pos.y);
    }

    public void set(int x, int y, Tile tile){
        setWithoutUpdate(x, y, tile);
        updateLinkedStages();
    }

    public void set(ServerQueries.TILE_PUTTED.Tile tile){
        set(tile.position.x, tile.position.y, new Tile(tile, null));
    }

    public void setByPlayer(int x, int y, Tile tile){
        set(x, y, tile);
        if(relatedPlayers.isCurrentPlayerClient()){
            try {
                relatedClient.putTile(x, y, tile.rotation, tile.meeple.position);
            } catch (IncorrectClientActionException e) {
                e.printStackTrace();
            }
        }
        relatedPlayers.passTurn();
    }

    public void setByPlayer(Vector2 pos, Tile tile){
        setByPlayer((int) pos.x, (int) pos.y, tile);
    }

    public void setByPlayer(ServerQueries.TILE_PUTTED.Tile tile){
        set(tile.position.x, tile.position.y, new Tile(tile, relatedPlayers.getCurrentPlayer()));
        System.out.println("TURN PASSED FROM "+relatedPlayers.getCurrentPlayer());
        relatedPlayers.passTurn();
        System.out.println("TURN PASSED TO "+relatedPlayers.getCurrentPlayer());
    }

    private void setWithoutUpdate(int x, int y, Tile tile){
        if(map[zeroTileXPositionInArray-y][zeroTileYPositionInArray+x] == null && tile != null)
            this.tilesCount++;
        else if(map[zeroTileXPositionInArray-y][zeroTileYPositionInArray+x] != null && tile == null)
            this.tilesCount--;

        map[zeroTileXPositionInArray-y][zeroTileYPositionInArray+x] = tile;
        if(tile != null){
            if (x < minOccupiedCoordinate.x) minOccupiedCoordinate.x = x;
            if (x > maxOccupiedCoordinate.x) maxOccupiedCoordinate.x = x;
            if (y < minOccupiedCoordinate.y) minOccupiedCoordinate.y = y;
            if (y > maxOccupiedCoordinate.y) maxOccupiedCoordinate.y = y;
        }
    }

    public void confirmSelectedTile(){
        Vector2 coordinate = this.selectedTileCoordinate;
        Tile selected = this.get(coordinate);
        selected.purpose = Tile.TilePurpose.LEGIT;
        this.selectedTileCoordinate = null;
        setByPlayer(coordinate, selected);
    }

    private void recalculateOccupiedCoordinates(){
        minOccupiedCoordinate.setZero();
        maxOccupiedCoordinate.setZero();
        for (int x=minX();x<=maxX();x++){
            for (int y=minY();y<=maxY();y++) {
                if(get(x, y) != null){
                    if (x < minOccupiedCoordinate.x) minOccupiedCoordinate.x = x;
                    if (x > maxOccupiedCoordinate.x) maxOccupiedCoordinate.x = x;
                    if (y < minOccupiedCoordinate.y) minOccupiedCoordinate.y = y;
                    if (y > maxOccupiedCoordinate.y) maxOccupiedCoordinate.y = y;
                }
            }
        }
    }

    public void setRelatedPlayers(PCLPlayers relatedPlayers) {
        this.relatedPlayers = relatedPlayers;
    }

    public void set(Vector2 pos, Tile tile){
        set((int) pos.x, (int) pos.y, tile);
    }

    public int width(){
        return (int) size.x;
    }

    public int height(){
        return (int) size.y;
    }

    public Vector2 size(){
        return size;
    }

    public int minX(){
        return -zeroTileYPositionInArray;
    }

    public int minY(){
        return -zeroTileXPositionInArray;
    }

    public int maxX(){
        return (int) size.x - zeroTileYPositionInArray - 1;
    }

    public int maxY(){
        return (int) size.y - zeroTileXPositionInArray - 1;
    }

    public Vector2 getMinOccupiedCoordinate() {
        return minOccupiedCoordinate;
    }

    public Vector2 getMaxOccupiedCoordinate() {
        return maxOccupiedCoordinate;
    }

    public Vector2 getOccupiedSize(){
        if(tilesCount == 0)
            return new Vector2(0,0);
        return maxOccupiedCoordinate.cpy().sub(minOccupiedCoordinate).add(1, 1);
    }

    public void generateRandom(int size){
        int minOccupy = (int) Math.ceil(-size/2.0);
        int maxOccupy = (int) Math.floor(size/2.0);
        Random random = new Random();
        for (int i = minX(); i <= maxX(); i++){ // для каждого столбца
            for (int j = maxY(); j >= minY(); j--){ // для каждой строки
                if (i < minOccupy || j < minOccupy || i > maxOccupy || j > maxOccupy) {
                    this.setWithoutUpdate(i, j, null);
                    continue;
                }

                int tries = 0;
                while (tries < 96){
                    Tile tile = new Tile(TileTypes.get(1+random.nextInt(24)), random.nextInt(4));
                    if (tile.canBePutBetween(this.get(i,j+1), this.get(i+1,j), this.get(i,j-1), this.get(i-1,j), true)) {
                        this.setWithoutUpdate(i, j, tile);
                        break;
                    }
                    tries++;
                }
            }

        }
        recalculateOccupiedCoordinates();
        updateLinkedStages();
    }

    public void linkGameField(GameField gameField){
        linkedGameFields.add(gameField);
    }

    public void linkGameHud(GameHud gameHud){
        linkedGameHuds.add(gameHud);
    }

    private void updateLinkedStages(){
        for (GameField gameField : linkedGameFields){
            gameField.updateStage();
        }
        for (GameHud gameHud : linkedGameHuds){
            gameHud.updateStage();
        }
    }

    public void print(String comment){
        System.out.println(comment+":\n");
        for (int y = (int) maxOccupiedCoordinate.y; y >= minOccupiedCoordinate.y; y--){
            for (int x = (int) minOccupiedCoordinate.x; x <= maxOccupiedCoordinate.x; x++) {
                if (get(x, y) == null)
                    System.out.print("  ");
                else
                    System.out.print(TileTypes.indexOf(get(x, y).type)+"-"+get(x, y).rotation+" ");
            }
            System.out.print("\n");
        }
        System.out.println("[][]:\n");
        for (int y = 0; y <= size().y-1; y++){
            for (int x = 0; x <= size().x-1; x++) {
                if (map[y][x] == null)
                    System.out.print("  ");
                else
                    System.out.print(TileTypes.indexOf(map[y][x].type)+"-"+map[y][x].rotation+" ");
            }
            System.out.print("\n");
        }
    }

    public ArrayList<Vector2> getAvailableSpots(TileType tileType){
        ArrayList<Vector2> res = new ArrayList<>();
        ArrayList<Tile> testTiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            testTiles.add(new Tile(tileType, i));
        }
        for (int i = (int) (minOccupiedCoordinate.x-1); i <= maxOccupiedCoordinate.x+1; i++) {
            for (int j = (int) (minOccupiedCoordinate.y-1); j <= maxOccupiedCoordinate.y+1; j++) {
                if (this.get(i, j) == null){
                    boolean available = false;
                    for (Tile tile : testTiles) {
                        if(tile.canBePutOn(this, i, j)){
                            available = true;
                            break;
                        }
                    }
                    if (available)
                        res.add(new Vector2(i, j));
                }
            }
        }
        return res;
    }

    public ArrayList<Integer> getAvailableRotations(int x, int y, TileType tileType){
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
           if(new Tile(tileType, i).canBePutOn(this, x, y))
               res.add(i);
        }
        return res;
    }

    public ArrayList<Integer> getAvailableRotations(Vector2 pos, TileType tileType){
        return getAvailableRotations((int) pos.x, (int) pos.y, tileType);
    }

    public void setSelectedTile(int x, int y, TileType tile){
        if (this.selectedTileCoordinate != null){
            System.out.println("SELECTED != null");
            this.set(this.selectedTileCoordinate, null);
        }
        ArrayList<Integer> availableRotations = getAvailableRotations(x, y, tile);
        if (availableRotations.size() == 0){
            availableRotations = getAvailableRotations(x, y, tile);
        }
        this.set(x, y, new Tile(tile, availableRotations.get(0), Tile.TilePurpose.IMAGINARY_SELECTED));
        this.selectedTileCoordinate = new Vector2(x, y);
        updateLinkedStages();
    }

    public void rotateSelectedTile(){
        if(selectedTileCoordinate == null)
            return;
        Tile selectedTile = this.get(selectedTileCoordinate);
        ArrayList<Integer> availableRotations = this.getAvailableRotations(selectedTileCoordinate, selectedTile.type);
        int currentRotationIndex = availableRotations.indexOf(selectedTile.rotation);
        selectedTile.rotation = availableRotations.get((currentRotationIndex+1)%availableRotations.size());
        updateLinkedStages();
    }


    public void setRelatedClient(GameWebSocketClient relatedClient) {
        this.relatedClient = relatedClient;
    }

    public boolean hasSelectedTile(){
        return this.selectedTileCoordinate != null;
    }
}
