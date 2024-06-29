package com.example.EscapeRacer.Logic;

public class GameManager {
    private int profit;
    private int numOfCollisions;
    private int life;
    private int first;
    private int second;
    private int currentRocksCount;
    private boolean isGameRunning;
    private int visibleCarColumn;
    private int distance;
    private int updatingNumber;

    public GameManager() {
        this.numOfCollisions = 0;
        this.life = 3;
        this.second = -1;
        this.first = -1;
        this.currentRocksCount = 0;
        this.isGameRunning = false;
        this.updatingNumber = 1;
        this.visibleCarColumn = 2;
        this.profit = 0;
        this.distance = 0;
    }

    public int getNumOfCollisions() {
        return numOfCollisions;
    }

    public int getProfit() {
        return this.profit;
    }

    public int getLife() {
        return life;
    }

    public int getSecond() {
        return second;
    }


    public int getVisibleCarColumn() {
        return visibleCarColumn;
    }

    public int getCurrentRocksCount() {
        return currentRocksCount;
    }

    public int getUpdatingNumber() {
        return updatingNumber;
    }

    public int getFirst() {
        return first;
    }

    public int getDistance() {
        return distance;
    }


    public void setVisibleCarColumn(int visibleCarColumn) {
        this.visibleCarColumn = visibleCarColumn;
    }

    public void setUpdatingNumber(int updatingNumber) {
        this.updatingNumber = updatingNumber;
    }

    public void setNumOfCollisions(int numOfCollisions) {
        this.numOfCollisions = numOfCollisions;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setCurrentRocksCount(int currentRocksCount) {
        this.currentRocksCount = currentRocksCount;
    }

    public void setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
    }

    public void setFirst(int first) {
        this.first = first;
    }


    public boolean isGameRunning() {
        return isGameRunning;
    }


    public boolean isGameLost() {
        return this.life == 0;
    }


    public void increaseDistance() {
        this.distance += 5;
    }


    public void increaseProfit() {
        this.profit ++;
    }
}
