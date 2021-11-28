package ua.carcassone.game.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PCLCurrentTile{
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
        Tile prevTile = this.currentTile;
        this.currentTile = newTile;
        support.firePropertyChange("currentTile", prevTile, newTile);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public boolean isSet(){
        return currentTile != null && currentTile.type != null;
    }
}