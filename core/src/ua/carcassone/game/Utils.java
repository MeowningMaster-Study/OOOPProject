package ua.carcassone.game;

import com.badlogic.gdx.Gdx;

public class Utils {

    private static final float SCALING_COEFFICIENT = 12;

    /**
     * standard unit for interactive elements on the screen (buttons, labels, etc.).
     * */
    public static final float ELEMENT_HEIGHT_UNIT = Gdx.graphics.getHeight() / SCALING_COEFFICIENT;
    public static final float ELEMENT_WIDTH_UNIT = Gdx.graphics.getWidth() / SCALING_COEFFICIENT;

    /**
     * Returns an amount of pixels from the bottom of the screen to match some amount of pixels from the top.
     * @param y pixels from the top
     * @return pixels from the bottom
     */
    public static int fromTop(int y){
        return Gdx.graphics.getDisplayMode().height-y;
    }
}
