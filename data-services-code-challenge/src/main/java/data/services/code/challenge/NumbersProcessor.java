package data.services.code.challenge;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 
 * @author prgundu
 *
 */
public class NumbersProcessor implements Runnable {
	
	Socket clientSocket;
	CopyOnWriteArrayList<String> mainQueue = new CopyOnWriteArrayList<String>();
	ObjectInputStream objectInputStream;
	CopyOnWriteArrayList<String> uniques = new CopyOnWriteArrayList<String>();
	Timer timer;
	FileWriter fileWriter;
	static int total=0;
	public NumbersProcessor(Socket clientSocket, FileWriter fileWriter) {
		this.clientSocket = clientSocket;
		this.fileWriter = fileWriter;
		timer = new Timer();
		timer.schedule(new ReportTask(), 0, 10000);
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
    			mainQueue.addAll(numbersList);
    			uniques = mainQueue.stream() 
                        .distinct()
                        .collect(Collectors.toCollection(CopyOnWriteArrayList::new)); 
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
	
	class ReportTask extends TimerTask {
		int previousUniqueSize = 0;
		int previousDuplicateSize = 0;
		int uniqueDifferenceSize = 0;
		int duplicateDifferenceSize = 0;
		int totalUniqueSize=0;
		int duplicatesSize = 0;
		int uniqueSize = 0;
		@Override
		public void run() {
			if (uniques != null) {
				uniqueSize = uniques.size();
				if (uniqueSize > 0) 
					duplicatesSize = mainQueue.size() - uniqueSize;
				uniques.forEach(i-> {
				try {
					fileWriter.write(i + System.lineSeparator());
					fileWriter.flush();
					mainQueue.remove(i);
					uniques.remove(i);
			    } catch (IOException e1) {
					e1.printStackTrace();
				}
				});
				
				uniqueDifferenceSize = uniqueSize - previousUniqueSize;
			    if(uniqueDifferenceSize <= 0) uniqueDifferenceSize=0;
			    duplicateDifferenceSize = duplicatesSize - previousDuplicateSize;
		    	if(duplicateDifferenceSize <= 0) duplicateDifferenceSize=0;
		    	previousUniqueSize = uniqueSize;
		    	previousDuplicateSize = duplicatesSize;
		    	total = total + uniqueDifferenceSize+duplicateDifferenceSize;
				String output = "Received " + uniqueDifferenceSize + " unique numbers, " + duplicateDifferenceSize + " duplicates. Unique total:" + total;
				System.out.println(output);
			}
		}
	}
}
