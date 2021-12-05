package ua.carcassone.game.game;

import ua.carcassone.game.game.sprites.SpritePolygon;
import ua.carcassone.game.game.sprites.SpriteType;

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
                false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100},
                            new int[]{0, 100, 100, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            // 2
            new TileType(
                new int[]{0, 0, 0, 1},
                new int[]{9, 9, 9, 9, 9, 9, 9, 9},
                true,
                false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 55, 62, 35, 38},
                            new int[]{0, 100, 100, 0, 0, 66, 66, 0},
                            8,
                            SpriteType.FIELD
                    )
            ),
            // 3
            new TileType(
                new int[]{5, 5, 5, 5},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                false,
                true,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100},
                            new int[]{0, 100, 100, 0},
                            4,
                            SpriteType.TOWN
                    )
            ),
            // 4
            new TileType(
                new int[]{5, 5, 5, 0},
                new int[]{0, 0, 0, 0, 0, 0, 9, 9},
                false,
                false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 73, 32},
                            new int[]{0, 100, 100, 0, 36, 36},
                            6,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 50, 73, 100},
                            new int[]{0, 10, 25, 15, 0},
                            5,
                            SpriteType.FIELD
                    )
            ),
            // 5
            new TileType(
                new int[]{5, 5, 5, 0},
                new int[]{0, 0, 0, 0, 0, 0, 9, 9},
                false,
                true,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 73, 32},
                            new int[]{0, 100, 100, 0, 36, 36},
                            6,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 50, 73, 100},
                            new int[]{0, 10, 25, 15, 0},
                            5,
                            SpriteType.FIELD
                    )
            ),
            // 6
            new TileType(
                new int[]{5, 5, 5, 1},
                new int[]{0, 0, 0, 0, 0, 0, 9, 10},
                false,
                false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 73, 32},
                            new int[]{0, 100, 100, 0, 36, 36},
                            6,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 43, 43},
                            new int[]{0, 10, 21, 0},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{57, 54, 74, 93},
                            new int[]{0, 21, 16, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            // 7
            new TileType(
                new int[]{5, 5, 5, 1},
                new int[]{0, 0, 0, 0, 0, 0, 9, 10},
                false,
                true,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 73, 32},
                            new int[]{0, 100, 100, 0, 36, 36},
                            6,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 43, 43},
                            new int[]{0, 10, 21, 0},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{57, 54, 74, 93},
                            new int[]{0, 21, 16, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            // 8
            new TileType(
                new int[]{5, 5, 0, 0},
                new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                false,
                false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 36},
                            new int[]{0, 100, 100, 74},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 42, 100, 100},
                            new int[]{0, 53, 100, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            // 9
            new TileType(
                new int[]{5, 5, 0, 0},
                new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                false,
                true,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 36},
                            new int[]{0, 100, 100, 74},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 42, 100, 100},
                            new int[]{0, 53, 100, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            // 10
            new TileType(
                    new int[]{5, 5, 1, 1},
                    new int[]{0, 0, 0, 0, 9, 10, 10, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 36},
                            new int[]{0, 100, 100, 74},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 42, 100, 100, 70, 44, 44},
                            new int[]{0, 53, 100, 57, 60, 36, 0},
                            7,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                        new int[]{60, 60, 81, 100, 100},
                        new int[]{0, 24, 37, 37, 0},
                        5,
                        SpriteType.FIELD
                    )
            ),
            // 11
            new TileType(
                    new int[]{5, 5, 1, 1},
                    new int[]{0, 0, 0, 0, 9, 10, 10, 9},
                    false,
                    true,
                    new SpritePolygon(
                            new int[]{0, 0, 100, 36},
                            new int[]{0, 100, 100, 74},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 42, 100, 100, 70, 44, 44},
                            new int[]{0, 53, 100, 57, 60, 36, 0},
                            7,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{60, 60, 81, 100, 100},
                            new int[]{0, 24, 37, 37, 0},
                            5,
                            SpriteType.FIELD
                    )
            ),
            //12
            new TileType(
                    new int[]{5, 0, 5, 0},
                    new int[]{0, 0, 9, 9, 0, 0, 10, 10},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 30, 69, 100},
                            new int[]{0, 100, 74, 74, 100},
                            5,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 50, 73, 100},
                            new int[]{0, 10, 25, 15, 0},
                            5,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 33, 68, 100},
                            new int[]{100, 83, 85, 100},
                            4,
                            SpriteType.FIELD
                    )
            ),
            //13
            new TileType(
                    new int[]{5, 0, 5, 0},
                    new int[]{0, 0, 9, 9, 0, 0, 10, 10},
                    false,
                    true,
                    new SpritePolygon(
                            new int[]{0, 0, 30, 69, 100, 100, 68, 27},
                            new int[]{0, 100, 74, 74, 100, 0, 33, 26},
                            8,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 50, 73, 100},
                            new int[]{0, 10, 25, 15, 0},
                            5,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 33, 68, 100},
                            new int[]{100, 83, 85, 100},
                            4,
                            SpriteType.FIELD
                    )
            ),
            //14
            new TileType(
                    new int[]{5, 6, 0, 0},
                    new int[]{0, 0, 0, 0, 9, 9, 9, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 33, 68, 100},
                            new int[]{100, 83, 85, 100},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 17, 13},
                            new int[]{0, 100, 64, 33},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 31, 25, 50, 85, 100, 100},
                            new int[]{0, 20, 40, 77, 69, 82, 100, 0},
                            4,
                            SpriteType.FIELD
                    )
            ),
            //15
            new TileType(
                    new int[]{0, 5, 0, 6},
                    new int[]{9, 9, 0, 0, 9, 9, 0, 0},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 33, 68, 100},
                            new int[]{100, 83, 85, 100},
                            4,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 21, 50, 73, 100},
                            new int[]{0, 10, 25, 15, 0},
                            5,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 30, 69, 100, 100, 68, 27},
                            new int[]{0, 100, 74, 74, 100, 0, 33, 26},
                            8,
                            SpriteType.FIELD
                    )

            ),
            //16
            new TileType(
                    new int[]{0, 5, 0, 0},
                    new int[]{9, 9, 0, 0, 9, 9, 9, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 26, 66, 100, 100},
                            new int[]{0, 100, 71, 71, 100, 0},
                            6,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 34, 50, 74, 100},
                            new int[]{100, 84, 81, 88, 100},
                            5,
                            SpriteType.TOWN
                    )
            ),
            //17
            new TileType(
                    new int[]{1, 5, 0, 1},
                    new int[]{9, 10, 0, 0, 10, 10, 10, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 34, 50, 74, 100},
                            new int[]{100, 84, 81, 88, 100},
                            5,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 20, 36, 42, 44},
                            new int[]{0, 40, 42, 32, 17, 0},
                            6,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 24, 46, 71, 100, 100, 57, 51, 30},
                            new int[]{57, 100, 76, 71, 78, 100, 0, 0, 33, 58},
                            10,
                            SpriteType.FIELD
                    )
            ),
            //18
            new TileType(
                    new int[]{0, 5, 1, 1},
                    new int[]{9, 9, 0, 0, 9, 10, 10, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 34, 50, 74, 100},
                            new int[]{100, 84, 81, 88, 100},
                            5,
                            SpriteType.TOWN
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 24, 46, 71, 100, 100, 74, 55, 44, 44},
                            new int[]{0, 100, 76, 71, 78, 100, 56, 56, 45, 34, 0},
                            11,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{55, 59, 73, 100, 100},
                            new int[]{0, 23, 42, 44, 0},
                            5,
                            SpriteType.FIELD
                    )
            ),
            //19
            new TileType(
                    new int[]{1, 5, 2, 3},
                    new int[]{9, 10, 0, 0, 10, 11, 11, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 44, 44},
                            new int[]{0, 44, 44, 0},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{60, 60, 100, 100, },
                            new int[]{0, 40, 40, 0, },
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 20, 39, 65, 100, 100, 65, 58, 40, 35},
                            new int[]{54, 100, 75, 75, 74, 100, 57, 56, 69, 69, 56 },
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 20, 35, 57, 73, 93,},
                            new int[]{100, 88, 81, 81, 90, 100,},
                            6,
                            SpriteType.TOWN
                    )
            ),
            //20
            new TileType(
                    new int[]{1, 5, 1, 0},
                    new int[]{9, 10, 0, 0, 10, 9, 9, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 31, 68, 100, 100, 45},
                            new int[]{60, 84, 71, 69, 88, 62, 65},
                            7,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 43, 100, 100},
                            new int[]{0, 39, 43, 39, 0},
                            5,
                            SpriteType.FIELD
                    )

            ),
            //21
            new TileType(
                    new int[]{0, 1, 0, 1},
                    new int[]{9, 9, 9, 10, 10, 10, 10, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 41, 44, 36, 43, 43},
                            new int[]{0, 100, 100, 76, 46, 27, 0},
                            7,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{58, 58, 52, 60, 57, 100, 100},
                            new int[]{0, 33, 46, 76, 100, 100, 0},
                            7,
                            SpriteType.FIELD
                    )
            ),
            //22
            new TileType(
                    new int[]{1, 0, 0, 1},
                    new int[]{9, 10, 10, 10, 10, 10, 10, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 20, 35, 43, 43,},
                            new int[]{0, 45, 43, 38, 29, 0,},
                            6,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 55, 51, 43, 18},
                            new int[]{61, 100, 100, 0, 0, 39, 50, 58},
                            8,
                            SpriteType.FIELD
                    )
            ),
            //23
            new TileType(
                    new int[]{1, 0, 2, 3},
                    new int[]{9, 10, 10, 10, 10, 11, 11, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 41, 41},
                            new int[]{0, 46, 46, 0},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 100, 100, 70, 60, 38, 26},
                            new int[]{62, 100, 100, 62, 62, 80, 81, 62},
                            8,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{53, 61, 60, 100, 100 },
                            new int[]{0, 27, 42, 42, 0 },
                            5,
                            SpriteType.FIELD
                    )
            ),
            //24
            new TileType(
                    new int[]{1, 2, 3, 4},
                    new int[]{9, 10, 10, 11, 11, 12, 12, 9},
                    false,
                    false,
                    new SpritePolygon(
                            new int[]{0, 0, 23, 41},
                            new int[]{0, 46, 28, 0},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{53, 61, 60, 100, 100 },
                            new int[]{0, 27, 42, 42, 0 },
                            5,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{0, 0, 37, 37},
                            new int[]{61, 100, 100, 61},
                            4,
                            SpriteType.FIELD
                    ),
                    new SpritePolygon(
                            new int[]{59, 100, 100, 79, 80, 59, },
                            new int[]{100, 100, 60, 60, 77, 77, },
                            6,
                            SpriteType.FIELD
                    )
            )
        );

        public static TileType get(int i){
            return tiles.get(i);
        }

        public static int indexOf(TileType o) {
                return tiles.indexOf(o);
        }

        public static boolean isGamingTile(TileType tileType){
            return tileType != null && tiles.indexOf(tileType) > 0;
        }

        public static boolean isGamingTile(Tile tile){
            return tile != null && tile.type != null && tiles.indexOf(tile.type) > 0;
        }
}
