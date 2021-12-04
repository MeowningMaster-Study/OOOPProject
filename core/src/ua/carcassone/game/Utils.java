package ua.carcassone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.game.MeeplePosition;
import ua.carcassone.game.game.TileType;
import ua.carcassone.game.game.TileTypes;

import java.awt.*;
import java.util.ArrayList;

public class Utils {

    private static final int SCALING_COEFFICIENT = 12;

    public enum SpacialRelation{
        LEFT, ABOVE, RIGHT, BELOW
    }

    /**
     * standard unit for interactive elements on the screen (buttons, labels, etc.).
     * */
    public static final int ELEMENT_HEIGHT_UNIT = Gdx.graphics.getHeight() / SCALING_COEFFICIENT;
    public static final int ELEMENT_WIDTH_UNIT = Gdx.graphics.getWidth() / SCALING_COEFFICIENT;

    /**
     * Returns an amount of pixels from the bottom of the screen to match some amount of pixels from the top.
     * @param y pixels from the top
     * @return pixels from the bottom
     */
    public static int fromTop(int y){
        return Gdx.graphics.getDisplayMode().height-y;
    }

    public static float fromTop(float y){
        return (Gdx.graphics.getDisplayMode().height-y);
    }

    public static int fromRight(int x){
        return Gdx.graphics.getDisplayMode().width-x;
    }

    public static float fromRight(float x){
        return (Gdx.graphics.getDisplayMode().width-x);
    }

    public static int getTileTypeId(TileType tileType){
        return TileTypes.indexOf(tileType);
    }

    public static boolean numberInRange(int number, int min, int upperBound){
        return (min <= number) && (number < upperBound);
    }

    public static float min(Vector2 vec){
        return Math.min(vec.x, vec.y);
    }

    /**
     * Ratates a point relatively to (0.5, 0.5) (or in square with side 1)
     * @param point point to rotate
     * @param times clockwise rotation times
     * @return rotated point
     */
    public static Vector2 rotatedIn1_1(Vector2 point, int times){
        if(times == 0) return point;
        if (times < 0) times = 4-Math.abs(times)%4;

        Vector2 res = point.cpy();
        for (int i = 0; i < times; i++) {
            res.set(res.y, 1-res.x);
        }
        return res;
    }

    public static double polygonArea(Polygon polygon){

        java.util.List<Vector2> vertices = new ArrayList<>();
        for (int i = 0; i < polygon.npoints; i++) {
            vertices.add(new Vector2(polygon.xpoints[i], polygon.ypoints[i]));
        }

        double sum = 0;
        for (int i = 0; i < vertices.size() ; i++)
        {
            if (i == 0) {
                sum += vertices.get(i).x * (vertices.get(i + 1).y - vertices.get(vertices.size() - 1).y);
            } else if (i == vertices.size() - 1) {
                sum += vertices.get(i).x * (vertices.get(0).y - vertices.get(i - 1).y);
            } else {
                sum += vertices.get(i).x * (vertices.get(i + 1).y - vertices.get(i - 1).y);
            }
        }

        return 0.5 * Math.abs(sum);
    }

    public static double polygonArea(Polygon polygon, float relativeSize){
        return polygonArea(polygon)/((100/relativeSize)*(100/relativeSize));
    }

}
