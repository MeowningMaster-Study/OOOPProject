package ua.carcassone.game.game;

import java.util.List;

class Meeple {
    Player player;
    byte position;
}

public class Tile {
    TileType type;
    List<Meeple> meeples;
}