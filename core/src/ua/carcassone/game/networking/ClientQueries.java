package ua.carcassone.game.networking;

import ua.carcassone.game.game.MeeplePosition;

public class ClientQueries {

    public static class JOIN_TABLE {
        final String action = "JOIN_TABLE";
        public String tableId;

        public JOIN_TABLE(String tableId){
            this.tableId = tableId;
        }
    }

    public static class CREATE_TABLE {
        String action;
        String tableName;

        public CREATE_TABLE(String tableName){
            this.tableName = tableName;
            this.action = "CREATE_TABLE";
        }
    }

    public static class PUT_TILE {
        final String action = "PUT_TILE";

        static class Position{
            public int x;
            public int y;
        }

        public Position position;
        public int rotation;
        public MeeplePosition meeple;

        public PUT_TILE(int x, int y, int rotation, MeeplePosition meeplePosition){
            this.position = new Position();
            this.position.x = x;
            this.position.y = y;
            this.rotation = rotation;
            this.meeple = meeplePosition;
        }
    }

}
