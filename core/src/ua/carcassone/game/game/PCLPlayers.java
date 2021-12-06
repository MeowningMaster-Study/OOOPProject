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

    public void addPlayer(String playerId, boolean isClient, Color color){
        if (isClient)
            addPlayer(new Player("You", playerId, color, true));
        else
            addPlayer(new Player(playerId, playerId, color, false));
    }

    public void addPlayer(String playerId, Color color){
        addPlayer(playerId, false, color);
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

    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) this.players.clone();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getPlayer(String playerId){
        for (Player player :
                this.players) {
            if (Objects.equals(player.getCode(), playerId))return player;
        }
        return null;
    }

    public Player getClient(){
        for (Player player :
                this.players) {
            if (player.isClient()) return player;
        }
        return null;
    }

    public boolean isCurrentPlayerClient(){
        return currentPlayer != null && currentPlayer.isClient();
    }

    public void passTurn(){
        Player prevPlayer = currentPlayer;

        if(currentPlayer == null){
            this.currentPlayer = players.get(0);
        }
        else if (this.players.size() == 1){
            System.out.println("Turn has been passed to the same player as there is only one");
            return;
        }
        else {
            this.currentPlayer = players.get((players.indexOf(currentPlayer)+1)%players.size());
        }

        support.firePropertyChange("currentPlayer", prevPlayer, this.currentPlayer);
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