package ua.carcassone.game.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Objects;

public class PCLPlayers{
    private ArrayList<Player> players;
    private int currentPlayerIndex = -1;
    private final PropertyChangeSupport support;

    public PCLPlayers(){
        support = new PropertyChangeSupport(this);
    }

    public void addPCLListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void removePCLListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }

    public void setPlayers(ArrayList<Player> newPlayers){
        support.firePropertyChange("players", this.players, newPlayers);
        this.players = newPlayers;
    }

    public void addPlayer(Player newPlayer){
        ArrayList<Player> newPlayers = (ArrayList<Player>) this.players.clone();
        newPlayers.add(newPlayer);
        setPlayers(newPlayers);
    }

    public void removePlayer(String playerID){
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (Player player : this.players){
            if (!Objects.equals(player.getCode(), playerID)){
                newPlayers.add(player);
            }
        }
        setPlayers(newPlayers);
    }

    public void playerLeft(String playerID){
        for (Player player : this.players){
            if (Objects.equals(player.getCode(), playerID)){
                player.left = true;
            }
        }
        // TODO maybe change this to an normal solution? (nah)
        support.firePropertyChange("players", null, this.players);
    }

    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }

    public Player getCurrentPlayer(){
        if (currentPlayerIndex == -1)
            return null;
        return players.get(currentPlayerIndex);
    }

    public void passTurn(){
        currentPlayerIndex = (currentPlayerIndex+1)%players.size();
        while (players.get(currentPlayerIndex).left){
            currentPlayerIndex = (currentPlayerIndex+1)%players.size();
        }
    }

    public boolean isTurnOf(Player player){
        return getCurrentPlayer() == player;
    }
}