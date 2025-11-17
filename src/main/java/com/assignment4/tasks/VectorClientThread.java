package com.assignment4.tasks;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class VectorClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final VectorClock vcl;
  private final int id;
  private final byte[] receiveData = new byte[1024]; // Buffer for incoming data
  private final List<Message> buffer = new ArrayList<>(); // This buffer can be used for Task 2.2

  public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {
    this.clientSocket = clientSocket;
    this.vcl = vcl;
    this.id = id;
  }

  @Override
  public void run() {

  /*
      Write your code here to continuously listen for incoming messages from the server
      You should first process the received message and then update the vector clock based on the received message (you can use .replaceAll("[\\[\\]]", "").split(",\\s*"); to split a received vector clock into its components)
      Then display the received message and its vector clock
  */
    while (!clientSocket.isClosed()) {
      try{
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] parts = receivedString.split(":");
        String messageBody = parts[0];
        String timeStampString = parts[1].replaceAll("[\\[\\]]", "");
        int senderId = Integer.parseInt(parts[2].trim());

        String[] timeStampParts = timeStampString.split(",");
        int[] receivedTimestamps = new int[timeStampParts.length];
        for (int i = 0; i < timeStampParts.length; i++) {
          receivedTimestamps[i] = Integer.parseInt(timeStampParts[i].trim());
      }
        VectorClock senderClock = new VectorClock(receivedTimestamps.length);
        for (int i = 0; i < receivedTimestamps.length; i++) {
            senderClock.setVectorClock(i, receivedTimestamps[i]);
        }

        displayMessage(new Message(messageBody, senderClock, senderId));

    } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

// TODO:
/*
    This method should print out the message (e.g. Client 1: Hello World!: [1, 0, 0]) and update
    the vector clock without ticking on receive. Then it should display the the updated vector clock.
    Example: Initial clock [0,0,0], updated clock after message from Client 1: [1, 0, 0]
*/
  private void displayMessage(Message message) {

      if (vcl.checkAcceptMessage(message.getSenderID() -1, message.getClock())){
          System.out.println("Client " + message.getSenderID() + ": " + message.getMessage() + ": " + message.getClock().showClock());

          vcl.updateClock(message.getClock());
          System.out.println("Current clock: " + vcl.showClock());

        } else {
          System.out.println("Buffered message " + message.getMessage() + " with Clock " + message.getClock().showClock());
            buffer.add(message);
      }

      boolean delivered = true;

      while (delivered) {
          delivered = false;

          for (int i = 0; i < buffer.size(); i++) {
                Message bufferedMessage = buffer.get(i);
                if (vcl.checkAcceptMessage(bufferedMessage.getSenderID() - 1, bufferedMessage.getClock())) {
                    System.out.println("Client " + bufferedMessage.getSenderID() + ": " + bufferedMessage.getMessage() + ": " + bufferedMessage.getClock().showClock());

                    vcl.updateClock(bufferedMessage.getClock());
                    System.out.println("Current clock: " + vcl.showClock());

                    buffer.remove(i);
                    delivered = true;
                    break; // Restart the loop after modifying the buffer
                }
          }
      }



  }
}
