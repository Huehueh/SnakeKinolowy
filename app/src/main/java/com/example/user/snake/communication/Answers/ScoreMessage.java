package com.example.user.snake.communication.Answers;

import java.io.Serializable;

/**
 * Created by user on 21.12.2016.
 */
public class ScoreMessage implements Serializable{
    public Score[] getScores() {
        return scores;
    }

    Score [] scores;
}
