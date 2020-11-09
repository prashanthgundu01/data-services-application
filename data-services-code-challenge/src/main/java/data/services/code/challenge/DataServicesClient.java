package data.services.code.challenge;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is Client class for sending generated random numbers to server
 * @author Prashanth Gundu
 *
 */
public class DataServicesClient {
	private Socket clientSocket;
	private ObjectOutputStream objectOutputStream;
	/**
	 * 
	 * @param ipaddress
	 * @param port
	 */
	public DataServicesClient(String ipaddress, int port) {
		try {
			clientSocket = new Socket(ipaddress, port);
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is to send generated random numbers to server 
	 */
	public void sendGeneratedRandomNumbersToServer() {
		List<String> numbers = new ArrayList<String>();
		for(int i=0;i<200000;i++) {
			numbers.add(String.format("%09d", ThreadLocalRandom.current().nextInt(1000000000)));
		}
		try {
			objectOutputStream.writeObject(numbers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void terminate() {
		List<String> terminateStrList = new ArrayList<String>();
		terminateStrList.add("terminate");
		try {
			objectOutputStream.writeObject(terminateStrList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void stopConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error when closing the connection");
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		DataServicesClient client2 = new DataServicesClient("127.0.0.1", 4000);
		client2.sendGeneratedRandomNumbersToServer();
		client2.stopConnection();
	}
}
