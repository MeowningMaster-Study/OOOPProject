package ua.carcassone.game.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
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
            new ArrayList<MeeplePosition>(),
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
                    new MeeplePosition(1, 0.54f, 0.47f),
                    new MeeplePosition(9, 0.9f, 0.75f),
                    new MeeplePosition(5, 0.18f, 0.8f),
                    new MeeplePosition(10, 0.8f, 0.25f)
            ),
            // 11
            Arrays.asList(
                    new MeeplePosition(1, 0.54f, 0.47f),
                    new MeeplePosition(9, 0.9f, 0.75f),
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
                    new MeeplePosition(5, 0.06f, 0.47f),
                    new MeeplePosition(6, 0.46f, 0.91f)
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
                    new MeeplePosition(1, 0.41f, 0.32f),
                    new MeeplePosition(9, 0.18f, 0.18f),
                    new MeeplePosition(10, 0.77f, 0.49f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 18
            Arrays.asList(
                    new MeeplePosition(1, 0.62f, 0.36f),
                    new MeeplePosition(9, 0.24f, 0.44f),
                    new MeeplePosition(10, 0.81f, 0.20f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 19
            Arrays.asList(
                    new MeeplePosition(1, 0.15f, 0.49f),
                    new MeeplePosition(2, 0.87f, 0.45f),
                    new MeeplePosition(3, 0.46f, 0.10f),
                    new MeeplePosition(5, 0.52f, 0.88f),
                    new MeeplePosition(9, 0.16f, 0.17f),
                    new MeeplePosition(10, 0.57f, 0.53f),
                    new MeeplePosition(11, 0.79f, 0.17f)
            ),
            // 20
            Arrays.asList(
                    new MeeplePosition(1, 0.44f, 0.49f),
                    new MeeplePosition(9, 0.64f, 0.20f),
                    new MeeplePosition(10, 0.12f, 0.64f),
                    new MeeplePosition(5, 0.52f, 0.90f)
            ),
            // 21
            Arrays.asList(
                    new MeeplePosition(1, 0.57f, 0.45f),
                    new MeeplePosition(9, 0.20f, 0.36f),
                    new MeeplePosition(10, 0.82f, 0.74f)
            ),
            // 22
            Arrays.asList(
                    new MeeplePosition(1, 0.47f, 0.52f),
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

}
