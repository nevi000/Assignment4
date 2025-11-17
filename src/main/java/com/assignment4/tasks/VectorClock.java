package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

  private final int[] timestamps;

  public VectorClock(int numOfClients) {
    timestamps = new int[numOfClients];
    Arrays.fill(timestamps, 0);
  }

  public synchronized void setVectorClock(int processId, int time) {
    timestamps[processId] = time;
  }

  public synchronized void tick(int processId) {
    timestamps[processId]++;
  }

  public synchronized int getCurrentTimestamp(int processId) {
    return timestamps[processId];
  }

  public synchronized void updateClock(VectorClock other) {
    for (int i = 0; i < timestamps.length; i++) {
      timestamps[i] = Math.max(timestamps[i], other.timestamps[i]);
    }
  }

  public synchronized String showClock() {
    return Arrays.toString(timestamps);
  }

  // TODO:
  // For Task 2.2
  // Check if a message can be delivered or has to be buffered
  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {

     /* if (senderClock.timestamps[senderId] != this.timestamps[senderId] + 1) {
          return false;
      }

      for (int i = 0; i < timestamps.length; i++) {
          if (i == senderId) continue;
          if (senderClock.timestamps[i] > this.timestamps[i]) {
              return false;
          }
      }
      return true;
  }}*/


    boolean acceptMessage = true;
    for (int i = 0; i < timestamps.length; i++) {
      if (timestamps[i] == 0 && senderClock.timestamps[i] > 0) {
        acceptMessage = false;
      }
      if (senderClock.timestamps[i] > timestamps[i] + 1){
        acceptMessage = false;
      }
    }
    return acceptMessage;
  }
}
