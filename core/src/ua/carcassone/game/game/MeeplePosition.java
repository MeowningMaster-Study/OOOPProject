package ua.carcassone.game.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MeeplePosition {
    public int entityId;
    public Vector2 position;

    public MeeplePosition(int entityId, Vector2 position) {
        this.entityId = entityId;
        this.position = position;
    }

    public MeeplePosition(int entityId, float x, float y) {
        this.entityId = entityId;
        this.position = new Vector2(x, y);
    }

    private static List<List<MeeplePosition>> tileTypesMeeples = Arrays.asList(
            // 0
            new ArrayList<>(),
            // 1
            Arrays.asList(
                    new MeeplePosition(13, 0.5f, 0.5f),
                    new MeeplePosition(9, 0.8f, 0.2f)
            ),
            // 2
            Arrays.asList(
                    new MeeplePosition(13, 0.5f, 0.5f),
                    new MeeplePosition(1, 0.47f, 0.16f),
                    new MeeplePosition(9, 0.8f, 0.8f)
            ),
            // 3
            Arrays.asList(
                    new MeeplePosition(5, 0.5f, 0.5f)
            ),
            // 4
            Arrays.asList(
                    new MeeplePosition(5, 0.5f, 0.75f),
                    new MeeplePosition(9, 0.56f, 0.15f)
            ),
            // 5
            Arrays.asList(
                    new MeeplePosition(5, 0.5f, 0.75f),
                    new MeeplePosition(9, 0.56f, 0.15f)
            ),
            // 6
            Arrays.asList(
                    new MeeplePosition(5, 0.5f, 0.75f),
                    new MeeplePosition(1, 0.5f, 0.15f),
                    new MeeplePosition(9, 0.75f, 0.12f),
                    new MeeplePosition(10, 0.3f, 0.1f)
            ),
            // 7
            Arrays.asList(
                    new MeeplePosition(5, 0.5f, 0.75f),
                    new MeeplePosition(1, 0.5f, 0.15f),
                    new MeeplePosition(9, 0.75f, 0.12f),
                    new MeeplePosition(10, 0.3f, 0.1f)
            ),
            // 8
            Arrays.asList(
                    new MeeplePosition(5, 0.18f, 0.8f),
                    new MeeplePosition(9, 0.72f, 0.34f)
            ),
            // 9
            Arrays.asList(
                    new MeeplePosition(5, 0.18f, 0.8f),
                    new MeeplePosition(9, 0.72f, 0.34f)
            ),
            // 10
            Arrays.asList(
                    new MeeplePosition(1, 0.60f, 0.36f),
                    new MeeplePosition(9, 0.84f, 0.71f),
                    new MeeplePosition(5, 0.18f, 0.8f),
                    new MeeplePosition(10, 0.8f, 0.25f)
            ),
            // 11
            Arrays.asList(
                    new MeeplePosition(1, 0.60f, 0.36f),
                    new MeeplePosition(9, 0.84f, 0.71f),
                    new MeeplePosition(5, 0.18f, 0.8f),
                    new MeeplePosition(10, 0.8f, 0.25f)
            ),
            // 12
            Arrays.asList(
                    new MeeplePosition(9, 0.55f, 0.93f),
                    new MeeplePosition(5, 0.55f, 0.55f),
                    new MeeplePosition(10, 0.5f, 0.1f)
            ),
            // 13
            Arrays.asList(
                    new MeeplePosition(9, 0.55f, 0.95f),
                    new MeeplePosition(5, 0.55f, 0.55f),
                    new MeeplePosition(10, 0.5f, 0.1f)
            ),
            // 14
            Arrays.asList(
                    new MeeplePosition(9, 0.64f, 0.41f),
                    new MeeplePosition(5, 0.11f, 0.50f),
                    new MeeplePosition(6, 0.47f, 0.89f)
            ),
            // 15
            Arrays.asList(
                    new MeeplePosition(9, 0.2f, 0.5f),
                    new MeeplePosition(5, 0.52f, 0.90f),
                    new MeeplePosition(6, 0.46f, 0.12f)
            ),
            // 16
            Arrays.asList(
                    new MeeplePosition(9, 0.5f, 0.3f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 17
            Arrays.asList(
                    new MeeplePosition(1, 0.37f, 0.37f),
                    new MeeplePosition(9, 0.18f, 0.18f),
                    new MeeplePosition(10, 0.74f, 0.46f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 18
            Arrays.asList(
                    new MeeplePosition(1, 0.60f, 0.36f),
                    new MeeplePosition(9, 0.26f, 0.44f),
                    new MeeplePosition(10, 0.81f, 0.20f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 19
            Arrays.asList(
                    new MeeplePosition(1, 0.20f, 0.50f),
                    new MeeplePosition(2, 0.85f, 0.50f),
                    new MeeplePosition(3, 0.53f, 0.24f),
                    new MeeplePosition(5, 0.52f, 0.88f),
                    new MeeplePosition(9, 0.16f, 0.17f),
                    new MeeplePosition(10, 0.18f, 0.64f),
                    new MeeplePosition(11, 0.79f, 0.17f)
            ),
            // 20
            Arrays.asList(
                    new MeeplePosition(1, 0.45f, 0.57f),
                    new MeeplePosition(9, 0.64f, 0.20f),
                    new MeeplePosition(10, 0.12f, 0.64f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 21
            Arrays.asList(
                    new MeeplePosition(1, 0.45f, 0.47f),
                    new MeeplePosition(9, 0.20f, 0.36f),
                    new MeeplePosition(10, 0.82f, 0.74f)
            ),
            // 22
            Arrays.asList(
                    new MeeplePosition(1, 0.39f, 0.44f),
                    new MeeplePosition(9, 0.23f, 0.23f),
                    new MeeplePosition(10, 0.69f, 0.79f)
            ),
            // 23
            Arrays.asList(
                    new MeeplePosition(1, 0.17f, 0.47f),
                    new MeeplePosition(2, 0.87f, 0.44f),
                    new MeeplePosition(3, 0.47f, 0.11f),
                    new MeeplePosition(9, 0.18f, 0.20f),
                    new MeeplePosition(10, 0.56f, 0.75f),
                    new MeeplePosition(11, 0.8f, 0.19f)
            ),
            // 24
            Arrays.asList(
                    new MeeplePosition(1, 0.19f, 0.53f),
                    new MeeplePosition(2, 0.57f, 0.87f),
                    new MeeplePosition(3, 0.89f, 0.49f),
                    new MeeplePosition(4, 0.49f, 0.18f),
                    new MeeplePosition(9, 0.19f, 0.20f),
                    new MeeplePosition(10, 0.21f, 0.81f),
                    new MeeplePosition(11, 0.82f, 0.83f),
                    new MeeplePosition(12, 0.77f, 0.22f)
            )
            );

    public MeeplePosition copy(){
        return new MeeplePosition(this.entityId, this.position.cpy());
    }

    public void rotate(int times){
        if(times <= 0) return;
        for (int i = 0; i < times; i++) {
            this.position.set(this.position.y, 1-this.position.x);
        }
    }

    public MeeplePosition rotated(int times){
        // TODO with Utils.rotated
        if(times == 0) return this;
        if (times < 0) times = 4-Math.abs(times)%4;

        MeeplePosition res = this.copy();
        for (int i = 0; i < times; i++) {
            res.position.set(res.position.y, 1-res.position.x);
        }
        return res;
    }

    public static List<MeeplePosition> get(TileType tileType, int rotation){
        List<MeeplePosition> res = new ArrayList<>();
        for (MeeplePosition meeplePosition: MeeplePosition.tileTypesMeeples.get(TileTypes.indexOf(tileType))) {
            res.add(meeplePosition.copy().rotated(rotation));
        }
        return res;
    }

    public static List<MeeplePosition> get(Tile tile){
        if(tile == null) return Collections.emptyList();
        return get(tile.type, tile.rotation);
    }

    public static Vector2 getPosition(Meeple meeple, Tile tile){
        List<MeeplePosition> positions = get(tile.type, tile.rotation);
        for (MeeplePosition position : positions){
            if (position.entityId == meeple.getPosition())
                return position.position;
        }
        return null;
    }

}
