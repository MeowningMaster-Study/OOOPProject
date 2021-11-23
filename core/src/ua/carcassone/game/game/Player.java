package ua.carcassone.game.game;

import com.badlogic.gdx.graphics.Color;

public class Player {
    private String name;
    private String code;
    private Color color;
    private int meepleCount = 0;

    public Player(String name, String code, Color color) {
        this.name = name;
        this.code = code;
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
