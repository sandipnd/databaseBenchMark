package setquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

class client {
	
	public static void dataPrepare(database obj) {
		obj.loadData();
		obj.createIndex();
	}
	
	public static void doTest(String host, int dataSize, String database, int threadC, String query) {
		final CountDownLatch completeLatch = new CountDownLatch(8);
		int threadCount = threadC;
		System.err.println("Starting test.");
		
		Set<String> querySet = new HashSet<String>();
		if ( query.equals("all"))
		    querySet =  workload.qlist.keySet();
		else
			querySet.add(query);
		for (String queryName : querySet) {
		int opCount = 10000;
		int opsDone  = 0;
		float avgtime = 0;
		System.out.println(" Test start for query " + queryName);
		List<heavylift> clients = null;
		
		clients = initLoad(opCount, completeLatch,  queryName, threadCount, host, dataSize, database);
		
		final Map<Thread, heavylift> threads = new HashMap<>(threadCount);
	      for (heavylift client : clients) {
	    	  threads.put(new Thread(client), client);
	      }
	    
	     long st = System.currentTimeMillis(); 
	     for (Thread t : threads.keySet()) {
	         t.start();
	       }
	     
	     for (Map.Entry<Thread, heavylift > entry : threads.entrySet()) {
	         try {
	           entry.getKey().join();
	           opsDone += entry.getValue().getOpsDone();
	           avgtime += entry.getValue().getAvg();
	         } catch (InterruptedException ignored) {
	           // ignored
	         }
	       }
		long end = System.currentTimeMillis();
		System.out.println(" time taken is " + ( end - st) + " :ops done: " + opsDone + " query " + queryName 
				+ " " + (avgtime/threadCount) );
		}
		
	}
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		if (args.length == 6 ) {
			String database =  args[0];
			String host = args[1];
			int dataSize = Integer.valueOf(args[2]);
			int thread = Integer.valueOf(args[3]);
			String  load = args[4];
			database cb = null;
			String query = args[5];
			if (database.equals("couchbase")) 
				cb = new cbutil("http://"+host,dataSize);
			else if (database.equals("mongodb")) 
				cb = new mongoutil("mongodb://"+host,dataSize);	
			if (load.equals("load")) {
			 cb.connect();
			 dataPrepare(cb);
			 cb.disConnect();
			} 
			if (load.equals("index")) {
			cb.connect();
			cb.createIndex();
			cb.disConnect();
			}
			 doTest(host,dataSize, database,thread, query);
	    } else {	
	    	System.out.println("run code as java setquery.jar database host datasize thread load");
	    }
	}
	
	
	private static List<heavylift> initLoad(int opCount, CountDownLatch completeLatch, String queryName, int threadCount, String host, int dataSize, String database ) {
		
		final List<heavylift> clients = new ArrayList<>(threadCount);
		for (int threadid = 0; threadid < threadCount; threadid++) {
			int threadopcount = opCount / threadCount;
			heavylift hlft = new heavylift(threadopcount, completeLatch, queryName, host, dataSize, database);
			clients.add(hlft);
		}
		return clients;
	}

}
