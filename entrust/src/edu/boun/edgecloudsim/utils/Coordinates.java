package edu.boun.edgecloudsim.utils;

public class Coordinates {

    //for diagram constructions
    private Integer X;
    private Integer Y;
    private Boolean isDead;
    private Integer id;

    private String time;


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
    public Coordinates(int x, int y, boolean isDead, int id, String time){
        this.X=x;
        this.Y=y;
        this.isDead=isDead;
        this.id=id;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "X=" + X +
                ", Y=" + Y +
                ", isDead=" + isDead +
                ", id=" + id +
                ", time='" + time + '\'' +
                '}';
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
