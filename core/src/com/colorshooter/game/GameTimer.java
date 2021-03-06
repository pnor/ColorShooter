package com.colorshooter.game;

/**
 * Created by pnore_000 on 8/19/2016.
 */
public class GameTimer {
    private final float initialTime;
    private float time;

    /**
     * Constructs a {@code GameTimer} with a given time.
     * @param t time
     */
    public GameTimer(float t) {
        time = t;
        initialTime = time;
    }

    /**
     * Constructs a {@code GameTimer} with the starting time of 90.
     */
    public GameTimer() {
        time = 90f;
        initialTime = 90f;
    }

    @Override
    public String toString() {
        return "" + (int) (time / 60f) + ":" + String.format("%02d", (int) (time % 60f));
    }

    /**
     * Checks if timer is finished. If the time is less than or equal to 0, returns true. Else returns false.
     * @return
     */
    public boolean checkIfFinished() {
        if (time <= 0f)
            return true;

        return false;
    }

    /**
     * Increases the timer by the given time
     * @param dt time between frames
     */
    public void increaseTimer(float dt) {
        time += dt;
    }

    /**
     * Decreases the timer by the given time
     * @param dt time between frames
     */
    public void decreaseTimer(float dt) {
        time -= dt;
    }

    /**
     * Returns the gap between the initial time on the {@code GameTimer} when it was initialized, and its
     * current time.
     * @return The difference between the initialized time and the current time.
     */
    public float getGap() {
        return initialTime - time;
    }

    public GameTimer copy() {
        return new GameTimer(time);
    }

    public float getTime() {
        return time;
    }

    public float getInitialTime() {
        return initialTime;
    }

    public void setTime(float t) {
        time = t;
    }
}
