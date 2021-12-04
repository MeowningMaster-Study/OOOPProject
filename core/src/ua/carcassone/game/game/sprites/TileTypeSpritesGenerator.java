package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileTypeSpritesGenerator {
    private static final SpriteManager spriteManager = new SpriteManager();

    public static List<PointTypeSprite> generatePointTypeSprites(TileType tileType, int seed, float tileSize){
        Random random = new Random(seed);
        List<Vector2> points = new ArrayList<>();
        double areasSize = 0;
        for (SpritePolygon poly : tileType.getSpritePolygons()) {
            areasSize += Utils.polygonArea(poly, 1);
        }

        for (int i = 0; i < areasSize * Settings.spritesPerTile; i++) {
            points.add(new Vector2(random.nextFloat(), random.nextFloat()));
        }

        points.sort((o1, o2) -> {
            if(o1.y == o2.y) return 0;
            return (o1.y < o2.y ? 1 : -1);
        });


        List<PointTypeSprite> res = new ArrayList<>();
        for (SpritePolygon polygon : tileType.getSpritePolygons()) {
            List<TypeSprite> availableSprites = spriteManager.getAvailableSprites(polygon.getSpriteType());
            if(availableSprites.size() == 0)
                continue;
            for (Vector2 point : points) {
                if (polygon.contains(point.x * Settings.polygonScale, point.y * Settings.polygonScale)){

                    TypeSprite chosenSprite = availableSprites.get(random.nextInt(availableSprites.size()));
                    double halfWidth = chosenSprite.getImage().getWidth()/2;
                    if(!polygon.contains(((point.x * tileSize)-halfWidth)*(Settings.polygonScale/tileSize), point.y * Settings.polygonScale) ||
                            !polygon.contains(((point.x * tileSize)+halfWidth)*(Settings.polygonScale/tileSize), point.y * Settings.polygonScale)) {
                        continue;
                    }
                    PointTypeSprite pointTypeSprite = new PointTypeSprite(
                            chosenSprite.texture,
                            chosenSprite.bottomStart,
                            chosenSprite.spriteType,
                            point.x,
                            point.y
                    );
                    res.add(pointTypeSprite);
                }
            }
        }

        return res;
    }
}
