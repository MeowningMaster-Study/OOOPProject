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
        support.firePropertyChange("currentTile", this.currentTile, newTile);
        this.currentTile = newTile;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public boolean isSet(){
        return currentTile != null && currentTile.type != null;
    }
}