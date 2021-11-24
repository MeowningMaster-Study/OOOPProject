package ua.carcassone.game.game;

public class TileType {
    int[] sides = new int[4];
    int[] halves = new int[8];
    boolean monastery;
    boolean shield;

    TileType(int[] sides, int[] halves, boolean monastery, boolean shield) {
        this.sides = sides;
        this.halves = halves;
        this.monastery = monastery;
        this.shield = shield;
    }
}
