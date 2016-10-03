package com.colorshooter.game;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author pnore_000
 */
public class HighScore implements Comparable {
    private String name;
    private int score;
    private int lastLevel;

    public HighScore() {
        name = "---";
        score = 0;
        lastLevel = 1;
    }

    public HighScore(String n) {
        name = n;
        score = 0;
        lastLevel = 1;
    }

    public HighScore(String name2, int score2, int lastLevel2) {
        name = name2;
        score = score2;
        lastLevel = lastLevel2;
    }

    public String toString() {
        return name + "        " + score + "        " + lastLevel;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    @Override
    public int compareTo(Object o) {
        HighScore h = (HighScore) o;

        if (this.getScore() > h.getScore())
            return 1;
        else if (this.getScore() < h.getScore())
            return -1;
        else
            return 0;
    }
}
