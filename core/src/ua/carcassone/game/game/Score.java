package ua.carcassone.game.game;

public class Score {
    public Integer roads;
    public Integer castles;
    public Integer monasteries;
    public Integer fields;
    public Integer summary;


    public Score(Integer roads, Integer castles, Integer monasteries, Integer fields, Integer summary) {
        this.roads = roads;
        this.castles = castles;
        this.monasteries = monasteries;
        this.fields = fields;
        this.summary = summary;
    }
}
