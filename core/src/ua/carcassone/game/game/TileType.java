package ua.carcassone.game.game;

public class TileType {
    byte[] sides = new byte[4];
    byte[] halves = new byte[8];
    boolean monastery;
    boolean shield;

    TileType(byte[] sides, byte[] halves, boolean monastery, boolean shield) {
        this.sides = sides;
        this.halves = halves;
        this.monastery = monastery;
        this.shield = shield;
    }
}
