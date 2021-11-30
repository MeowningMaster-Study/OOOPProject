package ua.carcassone.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ua.carcassone.game.Utils;
import ua.carcassone.game.networking.ServerQueries;

import java.util.Arrays;
import java.util.List;

public class Tile {
    public TileType type;
    private Meeple meeple;
    public int rotation;
    public TilePurpose purpose = TilePurpose.LEGIT;

    public enum TilePurpose{
        LEGIT,
        IMAGINARY_SELECTED,
        IMAGINARY_FOCUS,
        IMAGINARY_NOT_SELECTED
    }

    /** Copies a tile without meeples **/
    public Tile(Tile tile){
        this.type = tile.type;
        this.rotation = tile.rotation;
        this.purpose = tile.purpose;
        this.meeple = new Meeple(null, 0);
    }

    public Tile(Tile tile, TilePurpose purpose){
        this.type = tile.type;
        this.rotation = tile.rotation;
        this.purpose = purpose;
        this.meeple = new Meeple(null, 0);
    }

    public Tile(TileType type, int rotation) {
        this.type = type;
        this.rotation = rotation;
        this.meeple = new Meeple(
                null, 0);
    }

    public Tile(TileType type, int rotation, TilePurpose purpose) {
        this.type = type;
        this.rotation = rotation;
        this.purpose = purpose;
        this.meeple = new Meeple(null, 0);
    }

    public Tile(ServerQueries.TILE_PUTTED.Tile serverTile, Player relatedPlayer){
        this.type = TileTypes.get(serverTile.type);
        this.rotation = serverTile.rotation;
        if(serverTile.meeple != -1){
            this.meeple = new Meeple(relatedPlayer, serverTile.meeple);
        }
    }


    // temporary solution
    public boolean canBePutTo(Tile tile, Utils.SpacialRelation relation){
        if (tile == null || tile.type == null || tile.purpose != TilePurpose.LEGIT) return true;
        int tileActingSide = tile.type.getSide(relation.ordinal(), tile.rotation);
        int thisActingSide = this.type.getSide((relation.ordinal()+2)%4, this.rotation);
        return TileType.sidesMatch(tileActingSide, thisActingSide);
    }

    public boolean canBePutBetween(Tile upper, Tile right, Tile below, Tile left, boolean canBePutBetweenNull){
        if(!canBePutBetweenNull && upper ==  null && right == null && below == null && left == null)
            return false;
        if(!(
                        (upper != null && upper.purpose == TilePurpose.LEGIT) ||
                        (right != null && right.purpose == TilePurpose.LEGIT) ||
                        (below != null && below.purpose == TilePurpose.LEGIT) ||
                        (left  != null &&  left.purpose == TilePurpose.LEGIT)
        ))
            return false;

        return canBePutTo(upper, Utils.SpacialRelation.BELOW) &&
                canBePutTo(right, Utils.SpacialRelation.LEFT) &&
                canBePutTo(below, Utils.SpacialRelation.ABOVE) &&
                canBePutTo(left, Utils.SpacialRelation.RIGHT);
    }

    public boolean canBePutBetween(Tile upper, Tile right, Tile below, Tile left){
        return canBePutBetween(upper, right, below, left, false);
    }

    public boolean canBePutOn(Map map, int x, int y){
        return canBePutBetween(map.get(x, y+1), map.get(x+1, y), map.get(x, y-1), map.get(x-1, y));
    }

    @Override
    public String toString() {
        return "Tile{" +
                "sides=" + (type != null ? Arrays.toString(type.sides) : "null" )+
                ", rotation=" + rotation +
                '}';
    }

    public boolean hasMeeple(){
        return this.meeple != null && this.meeple.getPosition() != 0 && this.meeple.getPlayer() != null;
    }

    public void setMeeple(Meeple meeple){
        this.meeple = meeple;
    }

    public void setMeeple(Player player, int position){
        this.meeple = new Meeple(player, position);
    }

    public void unsetMeeple(){
        this.meeple = new Meeple(null, 0);
    }

    public Meeple getMeeple(){
        if(!hasMeeple())
            return null;
        return this.meeple;
    }
}