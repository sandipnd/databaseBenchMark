package setquery;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;
import com.couchbase.client.java.*;
import com.couchbase.client.java.query.*;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


public class heavylift implements Runnable {

	private final CountDownLatch completeLatch;
	private int opcount;
	private long targetOpsTickNs;
	private String queryName;
	private int opsdone;
	private Object dbObj;
	private String bucketname;
	private float avg;
	private long sum;
	private long latency;
	private int dataSize;
	private String host;
	private String database;
	volatile static boolean instance = true;
	PriorityQueue<Long> maxheap;
	database cb = null;
	
	
	static class PQsort implements Comparator<Long> {
		 
		public long compare(long one, long two) {
			return two - one;
		}

		@Override
		public int compare(Long o1, Long o2) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public heavylift(int opCount, CountDownLatch completeLatch, String queryName, String host, int dataSize, String database) {
		// TODO Auto-generated constructor stub
		this.completeLatch = completeLatch;
		this.opcount = opCount;
        this.queryName = queryName;
        this.database = database;
        this.dataSize = dataSize;
        this.host = host;
	}
	
	public Object Init() {
		if (database.equals("couchbase")) {
			cb = new cbutil("http://"+host,dataSize);
			cb.connect();
			return ((cbutil) cb).getBucket();
		} 
		else if (database.equals("mongodb")) {
			cb = new mongoutil("mongodb://"+host,dataSize);
			cb.connect();
			return ((mongoutil) cb).getcollection();
		}
		return null;
	}
	
	@Override
	public void run() {
		PQsort pqs = new PQsort();
		maxheap = new PriorityQueue<Long>(10,pqs);
		sum = 0;
		opsdone = 0;
		
		Object dbObj = Init();
			
		try {
			//run n1ql query
			for(int i = 0; i < opcount; i++) {
				long start = System.currentTimeMillis();
				if (dbObj instanceof Bucket) {
					N1qlQueryResult result = ((Bucket)dbObj).query(N1qlQuery.simple(workload.qlist.get(queryName)));
					for (N1qlQueryRow row : result) {
					    System.out.println(row.value());
					}
				}
				else if (dbObj instanceof DBCollection ) {
					long count  = ((DBCollection)dbObj).find(workload.mongoqlist.get(queryName)).count();
			        System.out.println(count);
				}
			   long end = System.currentTimeMillis();
			   sum +=  ( end-start);
			   maxheap.add(( end-start));
	           opsdone ++;
			}
		    } catch (Exception e) {
		      e.printStackTrace();
		      e.printStackTrace(System.out);
		      return;
		    } finally {
		    	cb.disConnect();
		    }
		
	}

	public int getOpsDone() {
	    return opsdone;
	  }
	
	public long getAvg() {
	    return sum/opsdone;
	  }
	
	public long getMedian() {
	    return maxheap.peek();
	  }
}

