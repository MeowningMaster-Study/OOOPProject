package ua.carcassone.game.networking;

import jdk.nashorn.internal.runtime.Scope;

import java.util.List;
import java.util.Map;

public class ServerQueries {

    public static class PLAYER_JOINED {
        public String action;
        public String playerId;
        public Color color;

        public static class Color{
            public int r, g, b;
        }

    }

    public static class PLAYER_LEFT {
        public String action;
        public String playerId;
    }

    public static class CREATE_TABLE_SUCCESS {
        public String action;
        public String tableId;
        public Color color;

        public static class Color{
            public int r, g, b;
        }
    }

    public static class JOIN_TABLE_SUCCESS {
        public String action;
        public String tableId;
        public String tableName;
        public List<String> players;
        public List<Color> colors;

        public static class Color{
            public int r, g, b;
        }
    }

    public static class JOIN_TABLE_FAILURE {
        public String action;
        public String tableId;
    }

    public static class ERROR {
        public String action;
        public Description description;

        public static class Description{
            public List<String> action;
        }
    }

    public static class GAME_STARTED {
        public String action;
        public int tiles;
    }

    public static class GAME_ENDED {
        public String action;
        public Map<String, Score> scores;

        public static class Score{
            int roads;
            int towns;
            int fields;
            int monasteries;
            int summary;
        }
    }

    public static class TILE_DRAWN {
        public String action;
        public Tile tile;

        public static class Tile{
            public int type;
            public int seed;
        }

    }

    public static class TILE_PUTTED {
        public String action;
        public String playerId;
        public Tile tile;


        public static class Tile{
            public int type;
            public Position position;
            public int rotation;
            public int meeple;
            public int seed;


            public static class Position{
                public int x;
                public int y;
            }
        }
    }

    public static class OBJECT_FINISHED{
        String action;
        Object object;

        public static class Object{
            int type;
            List<Position> tiles;
            List<Score> scores;

            public static class Position{
                int x;
                int y;
            }

            public static class Score{
                String playerId;
                int amount;
            }
        }
    }
}
