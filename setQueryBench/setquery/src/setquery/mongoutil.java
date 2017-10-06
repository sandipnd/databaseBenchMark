package setquery;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import java.util.HashMap;
import java.util.Iterator;


public class mongoutil extends database {
	
	MongoClient mongo;
	DB db;
	String host = "";
    int dataSize = 0;
    DBCollection col = null;
	
	public mongoutil(String host, int dataSize) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.dataSize = dataSize;
	}

	
	
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		 MongoClientURI mgURI = new MongoClientURI(host);
	    mongo = new MongoClient(mgURI);
		//mongo = new MongoClient(host);
		db = mongo.getDB("ycsb");
		col = db.getCollection("sqlsetquery");
		//mongo.setWriteConcern(WriteConcern.JOURNALED);
	}

	@Override
	public void createIndex() {
		for( String f1: fields) {
			col.createIndex(new BasicDBObject(f1, 1));
			for( String f2: fields) {
				if(f1.equals(f2))
					continue;
				col.createIndex(new BasicDBObject(f1, 1).append(f2, -1));

			}
		}
	}

	@Override
	public void loadData() {
		datagen dt  = new datagen(1, dataSize);
		Iterator<HashMap<String, String>> it = dt.iterator();
		while( it.hasNext()) {
			col.insert(new BasicDBObject(it.next()));
		}
	}

	public DBCollection getcollection() {
		return col;
	}
	
	public void doQuery() {
		
		
	}
	
	@Override
	public void disConnect() {
		// TODO Auto-generated method stub
		mongo.close();
		
	}

	
}
