package ua.carcassone.game.game;

import com.badlogic.gdx.graphics.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PCLPlayers{
    private ArrayList<Player> players;
    private Player currentPlayer = null;
    private final PropertyChangeSupport support;

    public PCLPlayers(){
        support = new PropertyChangeSupport(this);
        this.players = new ArrayList<>();
    }

    public void addPCLListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void removePCLListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }

    public void setPlayers(ArrayList<Player> newPlayers){
        System.out.println("SETTING PLAYERS FROM "+this.players+" TO "+newPlayers);
        ArrayList<Player> prevPlayers = this.players;
        this.players = newPlayers;
        support.firePropertyChange("players", prevPlayers, newPlayers);
    }

    public void clearPlayers(){
        setPlayers(new ArrayList<>());
    }

    public void addPlayer(Player newPlayer){
        ArrayList<Player> newPlayers = (ArrayList<Player>) this.players.clone();
        newPlayers.add(newPlayer);
        setPlayers(newPlayers);
    }

    public void addPlayer(String playerName, String playerId){
        addPlayer(new Player(playerName, playerId, new Color(new Random().nextInt())));
    }

    public void addPlayer(String playerId, boolean isClient){
        if (isClient)
            addPlayer(new Player("You", playerId, Color.WHITE, true));
        else
            addPlayer(new Player(playerId, playerId, new Color(new Random().nextInt()), false));
    }

    public void addPlayer(String playerId){
        addPlayer(playerId, false);
    }

    public void removePlayer(String playerID){
        System.out.println("Removing player, cur: "+this.players);
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (Player player : this.players){
            if (!Objects.equals(player.getCode(), playerID)){
                newPlayers.add(player);
            }
        }
        System.out.println("Removing player, new: "+newPlayers);
        setPlayers(newPlayers);
    }

    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) this.players.clone();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void passTurn(){
        if (this.players.size() == 1){
            System.out.println("Turn has been passed to the same player as there is only one");
            return;
        }

        currentPlayer = players.get((players.indexOf(currentPlayer)+1)%players.size());
    }

    public boolean isTurnOf(Player player){
        return getCurrentPlayer() == player;
    }


    @Override
    public String toString() {
        return "PCLPlayers{" +
                "players=" + players +
                ", currentPlayer=" + currentPlayer +
                '}';
    }
}