package com.example.user.snake.communication.Answers;

/**
 * Created by user on 21.12.2016.
 */
public class Score {
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getHits() {
        return hits;
    }

    String name;
    int points;
    int deaths;
    int hits;
}
