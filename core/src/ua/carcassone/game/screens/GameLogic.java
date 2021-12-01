package ua.carcassone.game.screens;

import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.game.PCLCurrentTile;
import ua.carcassone.game.game.Tile;
import ua.carcassone.game.game.TileTypes;

public class GameLogic {
    private final GameScreen gameScreen;


    public GameLogic(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Проводит все нужные операции для перевода текущего тайла
     * чтобы перевести из нулевого состояния в IS_PUT
     * */
    public void setSelection(int x, int y){
        gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_PUT);
        gameScreen.map.setSelectedTile(x, y, gameScreen.currentTile.getCurrentTile().type);
    }

    public void setSelection(Vector2 pos){
        setSelection((int) pos.x, (int) pos.y);
    }

    /**
     * Проводит все нужные операции для перевода текущего тайла
     * чтобы перевести из состояния IS_PUT в IS_PLACE_MEEPLE
     * */
    public void confirmSelectedTilePosition(){
        if (gameScreen.map.hasSelectedTile() && gameScreen.currentTile.isPut()) {
            gameScreen.map.confirmSelectedTilePosition();
            gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_PLACE_MEEPLE);
            gameScreen.field.setZoomToSeeTiles(5);
            gameScreen.field.centerCameraOnTile(gameScreen.map.getSelectedTilePosition());
        } else {
            System.out.println("WARNING: No tile position selected to confirm it (or tile is not PUT)");
        }
    }

    /**
     * Проводит все необходимые операции для текущего тайла
     * чтобы перевести из состояния IS_PLACE_MEEPLE в IS_PUT.
     * */
    public void disproveSelectedTileMeeples(){
        if (gameScreen.map.hasSelectedTile() && gameScreen.currentTile.isPlaceMeeple()) {
            gameScreen.map.disproveSelectedTile();
            gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_PUT);
        } else {
            System.out.println("WARNING: No tile position selected to disprove it (or tile is not PLACE MEEPLE)");
        }
    }

    /**
     * Проводит все необходимые операции для текущего тайла
     * чтобы перевести из состояния IS_PLACE_MEEPLE в IS_STABILIZED.
     * */
    public void confirmSelectedTileMeeples(){
        if(gameScreen.map.hasSelectedTile() && gameScreen.currentTile.isPlaceMeeple()){
            gameScreen.currentTile.setState(PCLCurrentTile.TileState.IS_STABILIZED);
            gameScreen.map.confirmSelectedTile();
        } else {
            System.out.println("WARNING: No meeples can even be put to confirm them");
        }
    }

    /**
     * Проводит нужные операции для установки мипла на тайл.
     * */
    public void setMeepleOnSelectedTile(int instanceId){
        if(instanceId == 0) unsetMeepleOnSelectedTile();
        else if(gameScreen.map.hasSelectedTile() && gameScreen.currentTile.isPlaceMeeple()){
            gameScreen.map.setMeepleOnSelectedTile(instanceId);
        }
    }

    /**
     * Проводит нужные операции для снятия мипла с поставленной позиции.
     * */
    public void unsetMeepleOnSelectedTile(){
        if(gameScreen.map.hasSelectedTile() && gameScreen.currentTile.isPlaceMeeple()){
            gameScreen.map.unsetMeepleOnSelectedTile();
        }
    }

}
