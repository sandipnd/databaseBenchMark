package setquery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import static com.couchbase.client.java.query.dsl.Expression.x;

public class cbutil extends database {
	String host = "";
    Bucket bucket = null;
    String bucketname = "sqlsetquery";
    Cluster cluster = null;
    int dataSize = 0;
 
	public cbutil(String host, int dataSize) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.dataSize = dataSize;
	}

	public  void connect() {
		cluster = CouchbaseCluster.create(host);
        bucket = cluster.openBucket(bucketname, "password");
	}
	
	public Bucket getBucket() {
		return bucket;
	}
	
	public void createIndex() {
		for( String field1: fields) {
			bucket.bucketManager().createN1qlIndex("c_index_"+field1, true, false, x("TO_NUMBER("+field1+")"));
		for( String field2: fields) { 
			try {
			    if (field2.equals(field1))
			    	continue;
				//bucket.bucketManager().createN1qlIndex("c_index_"+fieldName, true, false, fieldName);
				bucket.bucketManager().createN1qlIndex("c_index_"+field1+"_"+field2, true, false, x("TO_NUMBER("+field1+")"), x("TO_NUMBER("+field2+")"));	
			} catch( Exception e) {
				
			}
			finally {
				continue;
			}
		 }
		}
	}
	
	public void loadData() {
		datagen dt  = new datagen(1, dataSize);
		Iterator<HashMap<String, String>> it = dt.iterator();
		JsonObject content; 
		
		while( it.hasNext()) {
			//System.out.println(it.next());
			String id = UUID.randomUUID().toString();
			content =  JsonObject.from(it.next());
			JsonDocument document = JsonDocument.create(id, content);
			JsonDocument inserted = bucket.insert(document);
		}
	}
	
	public void disConnect() {
		cluster.disconnect();
	}
}
