package ua.carcassone.game.networking;

import java.util.List;

public class ServerQueries {

    public static class PLAYER_JOINED {
        public String action;
        public String playerId;
    }

    public static class PLAYER_LEFT {
        public String action;
        public String playerId;
    }

    public static class CREATE_TABLE_SUCCESS {
        public String action;
        public String tableId;
    }

    public static class JOIN_TABLE_SUCCESS {
        public String action;
        public String tableId;
        public List<String> players;
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
        // ...
    }

    public static class TILE_DRAWN {
        public String action;
        public int tileType;
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

            public static class Position{
                public int x;
                public int y;
            }
        }
    }
}
