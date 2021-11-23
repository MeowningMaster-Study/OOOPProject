package ua.carcassone.game.networking;

import java.util.List;

public class ServerQueries {

    public static class PLAYER_JOINED {
        String action;
        String playerId;
    }

    public static class PLAYER_LEFT {
        String action;
        String playerId;
    }

    public static class CREATE_TABLE_SUCCESS {
        String action;
        String tableId;
    }

    public static class JOIN_TABLE_SUCCESS {
        String action;
        String tableId;
        List<String> players;
    }

    public static class JOIN_TABLE_FAILURE {
        String action;
        String tableId;
    }

    public static class ERROR {
        String action;
        Description description;

        static class Description{
            List<String> action;
        }
    }

    public static class GAME_STARTED {
        String action;
    }

    public static class GAME_ENDED {
        String action;
        // ...
    }

    public static class TILE_DRAWN {
        String action;
        byte tileType;
    }

    public static class TILE_PUTTED {
        String action;
        Tile tile;

        static class Tile{
            byte type;
            Position position;
            byte rotation;
            byte meeple;

            static class Position{
                int x;
                int y;
            }
        }
    }
}
