package ua.carcassone.game.game;

import ua.carcassone.game.Utils;
import ua.carcassone.game.game.sprites.SpritePolygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileType {
    private int[] sides = new int[4];
    private int[] halves = new int[8];
    private final boolean monastery;
    private final boolean shield;
    private final List<SpritePolygon> spritePolygons;


    public TileType(int[] sides, int[] halves, boolean monastery, boolean shield) {
        this.sides = sides;
        this.halves = halves;
        this.monastery = monastery;
        this.shield = shield;
        this.spritePolygons = new ArrayList<>();
    }

    public TileType(int[] sides, int[] halves, boolean monastery, boolean shield, SpritePolygon... polygons) {
        this.sides = sides;
        this.halves = halves;
        this.monastery = monastery;
        this.shield = shield;
        this.spritePolygons = Arrays.asList(polygons);
    }

    public int getSide(int number, int rotation) {
        if (rotation <= number)
            return this.sides[number-rotation];
        else
            return this.sides[4-(rotation-number)];
    }

    public static boolean sidesMatch(int side1, int side2){
        return  (side1 == side2) ||
                (Utils.numberInRange(side1, 1, 5) && Utils.numberInRange(side2, 1, 5)) ||
                (Utils.numberInRange(side1, 5, 9) && Utils.numberInRange(side2, 5, 9));
    }

    public List<SpritePolygon> getSpritePolygons() {
        return spritePolygons;
    }

    @Override
    public String toString() {
        return "TileType{" +
                "sides=" + Arrays.toString(sides) +
                ", halves=" + Arrays.toString(halves) +
                ", monastery=" + monastery +
                ", shield=" + shield +
                '}';
    }
}
