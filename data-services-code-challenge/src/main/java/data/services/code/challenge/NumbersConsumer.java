package data.services.code.challenge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 
 * @author prgundu
 *
 */
public class NumbersConsumer implements Runnable {
	private BlockingQueue<String> blockingQueue;
	private Timer timer;
	CopyOnWriteArrayList<String> uniques = new CopyOnWriteArrayList<String>();
	static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
	FileWriter fileWriter;
	static int total=0;
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	public NumbersConsumer(BlockingQueue<String> queue, String fileName) {
		this.blockingQueue = queue;
		timer = new Timer();
		timer.schedule(new ReportTask(), 0, 10000);
		try {
			fileWriter = new FileWriter(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true) {
			if (!blockingQueue.isEmpty()) {
				list.addAll(blockingQueue);
				blockingQueue.removeAll(list);
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
			uniques = list.stream()
                    .distinct()
                    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
			if (uniques != null) {
				uniqueSize = uniques.size();
				if (uniqueSize > 0) 
					duplicatesSize = list.size() - uniqueSize;
				uniques.forEach(i-> {
				try {
					fileWriter.write(i + System.lineSeparator());
					fileWriter.flush();
					list.remove(i);
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
