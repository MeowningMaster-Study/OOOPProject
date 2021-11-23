package ua.carcassone.game.game;

import java.util.List;

class Meeple {
    public Player player;
    public int position;
}

public class Tile {
    public TileType type;
    public List<Meeple> meeples;
    public int rotation;

    public Tile(TileType type, int rotation) {
        this.type = type;
        this.rotation = rotation;
    }

}