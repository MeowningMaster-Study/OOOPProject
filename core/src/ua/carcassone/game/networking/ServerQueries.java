package ua.carcassone.game.networking;

import java.util.List;

public class ServerQueries {

    public static class JOIN_TABLE_SUCCESS {
        String action;
        String tableId;
        List<String> players;
    }

    public static class JOIN_TABLE_FAILURE {
        String action;
        // ...
        // TODO create JOIN_TABLE_FAILURE structure
    }

    public static class CREATE_TABLE_SUCCESS {
        String action;
        String tableId;
    }

}
