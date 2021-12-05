package ua.carcassone.game.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PCLCurrentTile{
    public enum TileState {IS_HANGING, IS_PUT, IS_PLACE_MEEPLE, IS_STABILIZED}

    private TileState state;
    private Tile currentTile;
    private final PropertyChangeSupport support;

    public PCLCurrentTile(){
        support = new PropertyChangeSupport(this);
    }

    public void addPCLListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void removePCLListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }

    public void setTile(Tile newTile){
        state = TileState.IS_HANGING;
        Tile prevTile = this.currentTile;
        this.currentTile = newTile;
        support.firePropertyChange("currentTile", prevTile, newTile);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setState(TileState state) {
        TileState prev = this.state;
        this.state = state;
        support.firePropertyChange("state", prev, this.state);
    }

    public boolean isHanging(){return state == TileState.IS_HANGING; }
    public boolean isPut(){return state == TileState.IS_PUT; }
    public boolean isPlaceMeeple(){return state == TileState.IS_PLACE_MEEPLE;}
    public boolean isStabilized(){return state == TileState.IS_STABILIZED; }

    public TileState getState() {
        return state;
    }

    public boolean isSet(){
        return currentTile != null && currentTile.type != null && state != null;
    }
}