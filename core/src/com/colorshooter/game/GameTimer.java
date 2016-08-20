package com.colorshooter.game;

/**
 * Created by pnore_000 on 8/19/2016.
 */
public class GameTimer {
    private float time;

    public GameTimer(float t) {
        time = t;
    }

    public GameTimer() {
        time = 90f;
    }

    @Override
    public String toString() {
        return "" + (int) (time / 60f) + ":" + String.format("%02d", (int) (time % 60f));
    }

    public void increaseTimer(float dt) {
        time += dt;
    }

    public void decreaseTimer(float dt) {
        time -= dt;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float t) {
        time = t;
    }

    public boolean checkIfFinished() {
        if (time <= 0f) {
            return true;
        }
        return false;
    }
}
