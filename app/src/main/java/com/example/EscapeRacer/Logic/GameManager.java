package com.example.EscapeRacer.Logic;

public class GameManager {
    private int numOfCollisions;
    private int life;
    private int lastRockColumn;
    private int befotrLastRockColumn;
    private int currentRocksCount;
    private String visibleCar;
    private boolean isGameRunning;

    public GameManager() {
        this.numOfCollisions = 0;
        this.life = 3;
        this.lastRockColumn = -1;
        this.befotrLastRockColumn = -1;
        this.currentRocksCount = 0;
        this.visibleCar = "middle";
        this.isGameRunning = false;
    }

    public int getNumOfCollisions() {
        return numOfCollisions;
    }

    public int getLife() {
        return life;
    }

    public int getLastRockColumn() {
        return lastRockColumn;
    }

    public String getVisibleCar() {
        return visibleCar;
    }

    public int getCurrentRocksCount() {
        return currentRocksCount;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void setNumOfCollisions(int numOfCollisions) {
        this.numOfCollisions = numOfCollisions;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setLastRockColumn(int lastRockColumn) {
        this.lastRockColumn = lastRockColumn;
    }

    public void setVisibleCar(String visibleCar) {
        this.visibleCar = visibleCar;
    }

    public GameManager setCurrentRocksCount(int currentRocksCount) {
        this.currentRocksCount = currentRocksCount;
        return this;
    }

    public boolean isGameLost() {
        return this.life == 0;
    }

    public int getBefotrLastRockColumn() {
        return befotrLastRockColumn;
    }

    public GameManager setBefotrLastRockColumn(int befotrLastRockColumn) {
        this.befotrLastRockColumn = befotrLastRockColumn;
        return this;
    }

    public GameManager setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
        return this;
    }
}
