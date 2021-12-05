package ua.carcassone.game.game;

import com.badlogic.gdx.graphics.Color;

public class Player {
    private final String name;
    private final String code;
    private Color color;

    private int meepleCount = 8;
    private int score = 0;
    private boolean isClient = false;

    public Player(String name, String code, Color color) {
        this.name = name;
        this.code = code;
        this.color = color;
    }

    public Player(String name, String code, Color color, boolean isClient) {
        this.name = name;
        this.code = code;
        this.color = color;
        this.isClient = isClient;
    }

    public void alterMeeples(int delta) {
        this.meepleCount += delta;
    }

    public void alterScore(int delta) {
        this.score += delta;
    }


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isClient() {
        return isClient;
    }

    public int getMeepleCount() {
        return meepleCount;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        this.score += points;
    }
}
