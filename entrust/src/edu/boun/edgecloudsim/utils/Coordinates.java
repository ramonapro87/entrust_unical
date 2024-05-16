package edu.boun.edgecloudsim.utils;

public class Coordinates {

    //for diagram constructions
    private Integer X;
    private Integer Y;
    private Boolean isDead;


    public Integer getX() {
        return X;
    }

    public Integer getY() {
        return Y;
    }

    public void setX(Integer x) {
        X = x;
    }

    public void setY(Integer y) {
        Y = y;
    }

    public Boolean isDead() {
        return isDead;
    }
    public Coordinates(int x, int y, boolean isDead){
        this.X=x;
        this.Y=y;
        this.isDead=isDead;

    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "X=" + X +
                ", Y=" + Y +
                ", isDead=" + isDead +
                '}';
    }
}
