package ua.carcassone.game.networking;

import ua.carcassone.game.game.MeeplePosition;

public class ClientQueries {

    public static class CONNECT_TO_TABLE {
        String action = "CONNECT_TO_TABLE";
        Data data = new Data();
        static class Data{
            public String tableId;
        }

        public CONNECT_TO_TABLE(String tableId){
            data.tableId = tableId;
        }
    }

    public static class CREATE_TABLE {
        String action = "CREATE_TABLE";
        Data data = new Data();
        static class Data{
        }

        public CREATE_TABLE(){
        }
    }

    public static class PUT_TILE {
        String action = "PUT_TILE";
        Data data = new Data();
        static class Data{
            static class Position{
                public int x;
                public int y;
            }

            public Position position;
            public int rotation;
            public MeeplePosition meeple;
        }

        public PUT_TILE(int x, int y, int rotation, MeeplePosition meeplePosition){
            data.position = new Data.Position();
            data.position.x = x;
            data.position.y = y;
            data.rotation = rotation;
            data.meeple = meeplePosition;
        }
    }

}
