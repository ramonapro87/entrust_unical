package edu.boun.edgecloudsim.utils;

import java.util.LinkedList;

public class DeadHost {
    // unica istanza della classe
    private static DeadHost instance;
    LinkedList<Integer> mobileHost;
    LinkedList<Integer> edgeHost;


    private DeadHost() {
        mobileHost = new LinkedList<>();
        edgeHost = new LinkedList<>();
    }

    public static synchronized DeadHost getInstance() {
        if (instance == null) {
            instance = new DeadHost();
        }
        return instance;
    }


    public LinkedList<Integer> getMobileHost() {
        return mobileHost;
    }


    public void addMobileHost(int id) {
        if (!mobileHost.contains(id)) {
            mobileHost.add(id);
        }
    }

    public void addEdgeHost(int id) {
        if (!edgeHost.contains(id)) {
            edgeHost.add(id);
        }
    }


    public boolean mobileHostIsDead(int hostId){
        return mobileHost.contains(hostId);
    }

    public boolean edgeHostIsDead(int hostId){
        return edgeHost.contains(hostId);
    }

    public void setMobileHost(LinkedList<Integer> mobileHost) {
        this.mobileHost = mobileHost;
    }

    public LinkedList<Integer> getEdgeHost() {
        return edgeHost;
    }

    public void setEdgeHost(LinkedList<Integer> edgeHost) {
        this.edgeHost = edgeHost;
    }

    public int getMobileHostSize(){
        return mobileHost.size();
    }

    public int getEdgeHostSize(){
        return edgeHost.size();
    }
}
