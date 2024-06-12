package com.example.EscapeRacer.Logic;

public class GameManager {
    private int numOfCollisions;
    private int life;
    private int first;
    private int second;
    private int currentRocksCount;
    private String visibleCar;
    private boolean isGameRunning;

    private int updatingNumber;

    public GameManager() {
        this.numOfCollisions = 0;
        this.life = 3;
        this.second = -1;
        this.first = -1;
        this.currentRocksCount = 0;
        this.visibleCar = "middle";
        this.isGameRunning = false;
        this.updatingNumber = 1;

    }

    public int getNumOfCollisions() {
        return numOfCollisions;
    }

    public int getLife() {
        return life;
    }

    public int getSecond() {
        return second;
    }

    public String getVisibleCar() {
        return visibleCar;
    }

    public int getCurrentRocksCount() {
        return currentRocksCount;
    }

    public int getUpdatingNumber() {
        return updatingNumber;
    }

    public void setUpdatingNumber(int updatingNumber) {
        this.updatingNumber = updatingNumber;
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

    public void setSecond(int second) {
        this.second = second;
    }

    public void setVisibleCar(String visibleCar) {
        this.visibleCar = visibleCar;
    }

    public void setCurrentRocksCount(int currentRocksCount) {
        this.currentRocksCount = currentRocksCount;
    }

    public boolean isGameLost() {
        return this.life == 0;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
    }
}
