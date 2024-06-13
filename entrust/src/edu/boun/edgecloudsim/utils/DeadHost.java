package edu.boun.edgecloudsim.utils;

import java.util.LinkedList;

public class DeadHost {
    // unica istanza della classe
    private static DeadHost instance;
   LinkedList<Integer> deadhostlist;

   private DeadHost(){
      deadhostlist=new LinkedList<>();

   }

    public static synchronized DeadHost getInstance() {
        if (instance == null) {
            instance = new DeadHost();
        }
        return instance;
    }


   public LinkedList<Integer> getDeadhostlist() {
      return deadhostlist;
   }


  public void add(int id) {
      if (!deadhostlist.contains(id)) {
          deadhostlist.add(id);

   }
   }

}
