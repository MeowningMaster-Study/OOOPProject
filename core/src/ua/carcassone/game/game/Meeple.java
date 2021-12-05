package ua.carcassone.game.game;

public class Meeple {
    private Player player;
    private int position;


    public Meeple(Player player, int position) {
        this.player = player;
        this.position = position;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }

}
