package ua.carcassone.game;

import com.badlogic.gdx.Gdx;
import ua.carcassone.game.game.TileType;
import ua.carcassone.game.game.TileTypes;

public class Utils {

    private static final int SCALING_COEFFICIENT = 12;

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

    public static int getTileTypeId(TileType tileType){
        return TileTypes.tiles.indexOf(tileType);
    }
}
