package ua.carcassone.game;

import com.badlogic.gdx.Gdx;

public class Utils {
    /**
     * Returns an amount of pixels from the bottom of the screen to match some amount of pixels from the top.
     * @param y pixels from the top
     * @return pixels from the bottom
     */
    public static int fromTop(int y){
        return Gdx.graphics.getDisplayMode().height-y;
    }
}
