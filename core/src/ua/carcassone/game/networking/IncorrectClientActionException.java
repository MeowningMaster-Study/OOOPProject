package ua.carcassone.game.networking;

public class IncorrectClientActionException extends Exception {
    public IncorrectClientActionException(String errorMessage) {
        super(errorMessage);
    }
}
