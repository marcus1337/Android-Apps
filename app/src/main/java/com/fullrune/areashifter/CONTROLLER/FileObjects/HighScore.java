package com.fullrune.areashifter.CONTROLLER.FileObjects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Marcus on 2017-05-09.
 */

public class HighScore implements Serializable {

    private static final long serialVersionUID = 687948764005033L;

    private Score[] scores;

    public HighScore() {
        scores = new Score[5];
        for (int i = 0; i < 5; i++) {
            scores[i] = new Score(0, "Anonymus", new Date());
        }
    }

    public boolean isNewScore(long points) {
        return points > scores[4].getScore();
    }

    public void insertScore(long points, String name) {
        for (int i = 0; i < 5; i++) {
            if (points > scores[i].getScore()) {
                for(int j = 4; j > i; j--){
                    scores[j] = scores[j-1];
                }

                scores[i] = new Score(points, name, new Date());
                return;
            }
        }
    }

    public String getName(int index) {
        return scores[index].getName();
    }
    public long getScore(int index) {
        return scores[index].getScore();
    }
    public Date getDate(int index) {
        return scores[index].getDate();
    }

    private class Score implements Serializable {
        public long getScore() {
            return score;
        }

        public String getName() {
            return name;
        }

        public Date getDate() {
            return date;
        }

        private long score;
        private String name;
        private Date date;

        public Score(long score, String name, Date date) {
            this.score = score;
            this.name = name;
            this.date = date;
        }
    }

}
