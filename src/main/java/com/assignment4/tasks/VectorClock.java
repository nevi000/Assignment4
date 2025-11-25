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

  // For Task 2.2
  // Check if a message can be delivered or has to be buffered
  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {
      boolean acceptMessage = true;
      int s = senderId - 1;

      if (senderClock.timestamps[s] != timestamps[s] + 1) {
          acceptMessage = false;
          return acceptMessage;
      }

      for (int i = 0; i < timestamps.length; i++) {
          if (i == s) continue;
          if (senderClock.timestamps[i] > timestamps[i]) {
              acceptMessage = false;
              return acceptMessage;
          }
      }
    return acceptMessage;
  }
}
