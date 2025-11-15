package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// This Class handles the continuous listening for incoming messages from the server
public class LTClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final LamportTimestamp lc;
  byte[] receiveData = new byte[1024];

  public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
    this.clientSocket = clientSocket;
    this.lc = lc;
  }

  @Override
  public void run() {
      while (!Thread.currentThread().isInterrupted() && !clientSocket.isClosed()) {
          try {
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
              clientSocket.receive(receivePacket);

              String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
              // Format: message:timestamp:id
              String[] parts = msg.split(":", 3);

              if (parts.length < 3) continue;

              String messageBody = parts[0];
              int receivedTs = Integer.parseInt(parts[1]);
              int senderId = Integer.parseInt(parts[2]);

              // Update Lamport Clock
              lc.updateClock(receivedTs);
              int localClock = lc.getCurrentTimestamp();

              System.out.println("Client " + senderId + ": " + messageBody + " (ts=" + receivedTs + ")");
              System.out.println("Current clock: " + localClock);

          } catch (IOException e) {
              if (clientSocket.isClosed()) break;
              e.printStackTrace();
          }
      }
  }
}