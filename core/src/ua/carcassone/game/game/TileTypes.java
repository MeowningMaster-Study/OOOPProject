package ua.carcassone.game.game;

import java.util.Arrays;
import java.util.List;

public class TileTypes {
        private static final List<TileType> tiles = Arrays.asList(
            null,
            // 1
            new TileType(
                new int[]{0, 0, 0, 0},
                new int[]{9, 9, 9, 9, 9, 9, 9, 9},
                true,
                false
            ),
            // 2
            new TileType(
                new int[]{0, 0, 0, 1},
                new int[]{9, 9, 9, 9, 9, 9, 9, 9},
                true,
                false
            ),
            // 3
            new TileType(
                new int[]{5, 5, 5, 5},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                false,
                true
            ),
            // 4
            new TileType(
                new int[]{5, 5, 5, 0},
                new int[]{0, 0, 0, 0, 0, 0, 9, 9},
                false,
                false
            ),
            // 5
            new TileType(
                new int[]{5, 5, 5, 0},
                new int[]{0, 0, 0, 0, 0, 0, 9, 9},
                false,
                true
            ),
            // 6
            new TileType(
                new int[]{5, 5, 5, 1},
                new int[]{0, 0, 0, 0, 0, 0, 9, 10},
                false,
                false
            ),
            // 7
            new TileType(
                new int[]{5, 5, 5, 1},
                new int[]{0, 0, 0, 0, 0, 0, 9, 10},
                false,
                true
            ),
            // 8
            new TileType(
                new int[]{5, 5, 0, 0},
                new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                false,
                false
            ),
            // 9
            new TileType(
                new int[]{5, 5, 0, 0},
                new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                false,
                true
            ),
            // 10
            new TileType(
                    new int[]{5, 5, 1, 1},
                    new int[]{0, 0, 0, 0, 9, 10, 10, 9},
                    false,
                    false
            ),
            // 11
            new TileType(
                    new int[]{5, 5, 1, 1},
                    new int[]{0, 0, 0, 0, 9, 10, 10, 9},
                    false,
                    true
            ),
            //12
            new TileType(
                    new int[]{5, 0, 5, 0},
                    new int[]{0, 0, 9, 9, 0, 0, 10, 10},
                    false,
                    false
            ),
            //13
            new TileType(
                    new int[]{5, 0, 5, 0},
                    new int[]{0, 0, 9, 9, 0, 0, 10, 10},
                    false,
                    true
            ),
            //14
            new TileType(
                    new int[]{5, 6, 0, 0},
                    new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                    false,
                    false
            ),
            //15
            new TileType(
                    new int[]{0, 5, 0, 6},
                    new int[]{9, 9, 0, 0, 9, 9, 0, 0},
                    false,
                    false
            ),
            //16
            new TileType(
                    new int[]{0, 5, 0, 0},
                    new int[]{9, 9, 0, 0, 9, 9, 9, 9},
                    false,
                    false
            ),
            //17
            new TileType(
                    new int[]{1, 5, 0, 1},
                    new int[]{9, 10, 0, 0, 10, 10, 10, 9},
                    false,
                    false
            ),
            //18
            new TileType(
                    new int[]{0, 5, 1, 1},
                    new int[]{9, 9, 0, 0, 9, 10, 10, 9},
                    false,
                    false
            ),
            //19
            new TileType(
                    new int[]{1, 5, 2, 3},
                    new int[]{9, 10, 0, 0, 10, 11, 11, 9},
                    false,
                    false
            ),
            //20
            new TileType(
                    new int[]{1, 5, 1, 0},
                    new int[]{9, 10, 0, 0, 10, 9, 9, 9},
                    false,
                    false
            ),
            //21
            new TileType(
                    new int[]{0, 1, 0, 1},
                    new int[]{9, 9, 9, 10, 10, 10, 10, 9},
                    false,
                    false
            ),
            //22
            new TileType(
                    new int[]{1, 0, 0, 1},
                    new int[]{9, 10, 10, 10, 10, 10, 10, 9},
                    false,
                    false
            ),
            //23
            new TileType(
                    new int[]{1, 0, 2, 3},
                    new int[]{9, 10, 10, 10, 10, 11, 11, 9},
                    false,
                    false
            ),
            //24
            new TileType(
                    new int[]{1, 2, 3, 4},
                    new int[]{9, 10, 10, 11, 11, 12, 12, 9},
                    false,
                    false
            )
        );

        public static TileType get(int i){
            return tiles.get(i);
        }

        public static int indexOf(TileType o) {
                return tiles.indexOf(o);
        }
}
