package ua.carcassone.game.game;

import ua.carcassone.game.Utils;

import java.util.Arrays;

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

    public int getSide(int number, int rotation) {
        if (rotation <= number)
            return this.sides[number-rotation];
        else
            return this.sides[4-(rotation-number)];
    }

    // temporary solution
    public static boolean sidesMatch(int side1, int side2){
        return  (side1 == side2) ||
                (Utils.numberInRange(side1, 1, 5) && Utils.numberInRange(side2, 1, 5)) ||
                (Utils.numberInRange(side1, 5, 9) && Utils.numberInRange(side2, 5, 9));

    }

    @Override
    public String toString() {
        return "TileType{" +
                "sides=" + Arrays.toString(sides) +
                '}';
    }
}
