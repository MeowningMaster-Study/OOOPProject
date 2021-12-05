package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.Map;
import ua.carcassone.game.game.Tile;
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


        List<PointTypeSprite> res = new ArrayList<>();
        for (SpritePolygon polygon : tileType.getSpritePolygons()) {
            List<TypeSprite> availableSprites = spriteManager.getAvailableSprites(polygon.getSpriteType());
            if(availableSprites.size() == 0)
                continue;
            for (Vector2 point : points) {
                if (polygon.contains(point.x * Settings.polygonScale, point.y * Settings.polygonScale)){

                    TypeSprite chosenSprite = availableSprites.get(random.nextInt(availableSprites.size()));
                    double halfWidth = chosenSprite.getImage().getWidth()/2;
//                    if(!polygon.contains(((point.x * tileSize)-halfWidth)*(Settings.polygonScale/tileSize), point.y * Settings.polygonScale) ||
//                            !polygon.contains(((point.x * tileSize)+halfWidth)*(Settings.polygonScale/tileSize), point.y * Settings.polygonScale)) {
//                        continue;
//                    }
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


    public static List<PointTypeSprite> generateMandatorySprites(Map map, int x, int y, int seed){
        Random random = new Random(seed);

        List<PointTypeSprite> res = new ArrayList<>();

        Tile tile = map.get(x, y);
        if (tile != null){
            if (tile.type.hasMonastery()){
                boolean finished = true;
                for (int i = x-1; i <= x + 1; i++) {
                    for (int j = y+1; j >= y-1; j--) {
                        Tile tile1 = map.get(i, j);
                        if (tile1 == null || tile1.purpose != Tile.TilePurpose.LEGIT){
                            finished = false;
                            break;
                        }
                    }
                }
                List<TypeSprite> availableVariations = spriteManager.getMandatorySprites(SpriteType.MONASTERY, finished);

                TypeSprite chosenSprite = availableVariations.get(random.nextInt(availableVariations.size()));
                Vector2 pos = spriteManager.getMandatorySpritePosition(tile.type, tile.rotation);
                PointTypeSprite pointTypeSprite = new PointTypeSprite(
                        chosenSprite.texture,
                        chosenSprite.bottomStart,
                        chosenSprite.spriteType,
                        pos.x,
                        pos.y
                );
                res.add(pointTypeSprite);

            }

            if (tile.type.hasShield()){
                List<TypeSprite> availableVariations = spriteManager.getMandatorySprites(SpriteType.SHIELD, true);

                TypeSprite chosenSprite = availableVariations.get(random.nextInt(availableVariations.size()));
                Vector2 pos = spriteManager.getMandatorySpritePosition(tile.type, tile.rotation);
                PointTypeSprite pointTypeSprite = new PointTypeSprite(
                        chosenSprite.texture,
                        chosenSprite.bottomStart,
                        chosenSprite.spriteType,
                        pos.x,
                        pos.y
                );
                res.add(pointTypeSprite);

            }
        }

        return res;
    }

    public static List<PointTypeSprite> generateMandatorySprites(TileType tileType, int rotation, int seed){
        Random random = new Random(seed);

        List<PointTypeSprite> res = new ArrayList<>();

        if (tileType != null){

            if (tileType.hasMonastery()){
                boolean finished = false;
                List<TypeSprite> availableVariations = spriteManager.getMandatorySprites(SpriteType.MONASTERY, finished);

                TypeSprite chosenSprite = availableVariations.get(random.nextInt(availableVariations.size()));
                Vector2 pos = spriteManager.getMandatorySpritePosition(tileType, rotation);
                PointTypeSprite pointTypeSprite = new PointTypeSprite(
                        chosenSprite.texture,
                        chosenSprite.bottomStart,
                        chosenSprite.spriteType,
                        pos.x,
                        pos.y
                );
                res.add(pointTypeSprite);
            }

            if (tileType.hasShield()){
                List<TypeSprite> availableVariations = spriteManager.getMandatorySprites(SpriteType.SHIELD, true);

                TypeSprite chosenSprite = availableVariations.get(random.nextInt(availableVariations.size()));
                Vector2 pos = spriteManager.getMandatorySpritePosition(tileType, rotation);
                PointTypeSprite pointTypeSprite = new PointTypeSprite(
                        chosenSprite.texture,
                        chosenSprite.bottomStart,
                        chosenSprite.spriteType,
                        pos.x,
                        pos.y
                );
                res.add(pointTypeSprite);
            }

        }

        return res;
    }
}
