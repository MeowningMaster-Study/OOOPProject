package ua.carcassone.game.game.sprites;

import java.awt.*;

public class SpritePolygon extends Polygon {
    private final SpriteType spriteType;

    public SpritePolygon(int[] xpoints, int[] ypoints, int npoints, SpriteType spriteType) {
        super(xpoints, ypoints, npoints);
        this.spriteType = spriteType;
    }

    public SpriteType getSpriteType() {
        return spriteType;
    }
}
