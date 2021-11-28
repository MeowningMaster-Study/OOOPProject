package ua.carcassone.game.game;

import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Settings;
import ua.carcassone.game.screens.GameField;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {
    private final Tile[][] map;
    private final int zeroTileXPositionInArray;
    private final int zeroTileYPositionInArray;
    private final Vector2 size;
    private final Vector2 minOccupiedCoordinate;
    private final Vector2 maxOccupiedCoordinate;
    private final List<GameField> linkedGameFields = new LinkedList<>();

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

    private void setWithoutUpdate(int x, int y, Tile tile){
        map[zeroTileXPositionInArray-y][zeroTileYPositionInArray+x] = tile;
        if(tile != null){
            if (x < minOccupiedCoordinate.x) minOccupiedCoordinate.x = x;
            if (x > maxOccupiedCoordinate.x) maxOccupiedCoordinate.x = x;
            if (y < minOccupiedCoordinate.y) minOccupiedCoordinate.y = y;
            if (y > maxOccupiedCoordinate.y) maxOccupiedCoordinate.y = y;
        }
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
        return maxOccupiedCoordinate.cpy().sub(minOccupiedCoordinate);
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
                    Tile tile = new Tile(TileTypes.tiles.get(1+random.nextInt(24)), random.nextInt(4));
                    if (tile.canBePutBetween(this.get(i,j+1), this.get(i+1,j), this.get(i,j-1), this.get(i-1,j))) {
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

    private void updateLinkedStages(){
        for (GameField gameField : linkedGameFields){
            gameField.updateStage();
        }
    }

    public void print(String comment){
        System.out.println(comment+":\n");
        for (int y = (int) maxOccupiedCoordinate.y; y >= minOccupiedCoordinate.y; y--){
            for (int x = (int) minOccupiedCoordinate.x; x <= maxOccupiedCoordinate.x; x++) {
                if (get(x, y) == null)
                    System.out.print("  ");
                else
                    System.out.print(TileTypes.tiles.indexOf(get(x, y).type)+"-"+get(x, y).rotation+" ");
            }
            System.out.print("\n");
        }
        System.out.println("[][]:\n");
        for (int y = 0; y <= size().y-1; y++){
            for (int x = 0; x <= size().x-1; x++) {
                if (map[y][x] == null)
                    System.out.print("  ");
                else
                    System.out.print(TileTypes.tiles.indexOf(map[y][x].type)+"-"+map[y][x].rotation+" ");
            }
            System.out.print("\n");
        }
    }
}
