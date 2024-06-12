package edu.boun.edgecloudsim.utils;

import java.util.LinkedList;

public class DeadHost {
   LinkedList<Integer> deadhostlist;

   public DeadHost(){
      deadhostlist=new LinkedList<>();

   }

   public LinkedList<Integer> getDeadhostlist() {
      return deadhostlist;
   }


  public void add(int id) {
      deadhostlist.add(id);

   }
   
}
