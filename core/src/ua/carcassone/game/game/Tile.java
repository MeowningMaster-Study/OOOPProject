package ua.carcassone.game.game;

import ua.carcassone.game.Utils;

import java.util.Arrays;
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


    // temporary solution
    public boolean canBePutTo(Tile tile, Utils.SpacialRelation relation){
        if (tile == null) return true;
        int tileActingSide = tile.type.getSide(relation.ordinal(), tile.rotation);
        int thisActingSide = this.type.getSide((relation.ordinal()+2)%4, this.rotation);
        return TileType.sidesMatch(tileActingSide, thisActingSide);
    }

    public boolean canBePutBetween(Tile upper, Tile right, Tile below, Tile left){
        return canBePutTo(upper, Utils.SpacialRelation.BELOW) &&
                canBePutTo(right, Utils.SpacialRelation.LEFT) &&
                canBePutTo(below, Utils.SpacialRelation.ABOVE) &&
                canBePutTo(left, Utils.SpacialRelation.RIGHT);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "sides=" + Arrays.toString(type.sides) +
                ", rotation=" + rotation +
                '}';
    }
}