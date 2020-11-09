package data.services.code.challenge;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author prgundu 
 *
 */
public class NumbersProcessor implements Runnable {
	
	Socket clientSocket;
	ObjectInputStream objectInputStream;
	BlockingQueue<String> queue;
	
	public NumbersProcessor(Socket clientSocket, BlockingQueue<String> queue) {
		this.clientSocket = clientSocket;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try
        {
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			List<String> numbersList = (ArrayList<String>) objectInputStream.readObject();
			if (numbersList != null) {
    			if (numbersList.size() == 1) {
    				if (numbersList.get(0).equalsIgnoreCase("terminate")) System.exit(0);
    			}
    			queue.addAll(numbersList);
        	}
        } 
        catch(Exception exception) 
        { 
        	exception.printStackTrace();
        }
		finally {
            try {
				clientSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
