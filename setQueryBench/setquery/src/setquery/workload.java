package setquery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;


/*
 A COUNT of records with a single exact match condition, known as query Q1:
Q1: SELECT COUNT(*) FROM BENCH
WHERE KN = 2;
(Here and in later queries, KN stands for any member of a set of columns. Here, KN e
{KSEQ, K100K,..., K4, K2}. The measurements are reported separately for each of these cases.)

 A COUNT of records from a conjunction of two exact match condition, query Q2A:
Q2A: SELECT COUNT(*) FROM BENCH
WHERE K2 = 2 AND KN = 3;
For each KN e {KSEQ, K100K,..., K4, K2}

Q4: SELECT KSEQ, K500K FROM BENCH
WHERE constraint with (3 or 5) conditions ;

 */


public class workload {
    
	public static final  Map<String, String> qlist;
	public static final  Map<String, DBObject> mongoqlist;
	
	static {
		Map<String, String> tmpqlist = new HashMap<String, String>();
		tmpqlist.put("q101", "select count(*) from `sqlsetquery` where TO_NUMBER(k250k) = 2");
		tmpqlist.put("q102", "select count(*) from `sqlsetquery` where TO_NUMBER(k100k) = 2");
		tmpqlist.put("q103", "select count(*) from `sqlsetquery` where TO_NUMBER(k10k) = 2");
		tmpqlist.put("q104", "select count(*) from `sqlsetquery` where TO_NUMBER(k1k) = 2");
		tmpqlist.put("q105", "select count(*) from `sqlsetquery` where TO_NUMBER(k100) = 2");
		tmpqlist.put("q106", "select count(*) from `sqlsetquery` where TO_NUMBER(k25) = 2");
		tmpqlist.put("q107", "select count(*) from `sqlsetquery` where TO_NUMBER(k10) = 2");
		tmpqlist.put("q108", "select count(*) from `sqlsetquery` where TO_NUMBER(k5) = 2");
		tmpqlist.put("q109", "select count(*) from `sqlsetquery` where TO_NUMBER(k4) = 2");
		tmpqlist.put("q110", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2");
		//tmpqlist.put("q201", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(kseq) = 3");
		tmpqlist.put("q202", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k100) = 3");
		tmpqlist.put("q203", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k10k) = 3");
		tmpqlist.put("q204", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k1k) = 3");
		tmpqlist.put("q205", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k100) = 3");
		tmpqlist.put("q206", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k25) = 3");
		tmpqlist.put("q207", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k10) = 3");
		tmpqlist.put("q208", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k5) = 3");
		tmpqlist.put("q209", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k4) = 3");
		//tmpqlist.put("q210", "select count(*) from sq11knum where TO_NUMBER(k2) = 2 and TO_NUMBER(kseq) <> 3;");
		
		/*tmpqlist.put("q211", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k100k) <> 3;");
		tmpqlist.put("q212", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k10k) <> 3;");
		tmpqlist.put("q213", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k1k) <> 3");
		tmpqlist.put("q214", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k100) <> 3");
		tmpqlist.put("q215", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k25) <> 3");
		tmpqlist.put("q216", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k10) <> 3");
		*/
		tmpqlist.put("q216", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k10) <> 3");
		tmpqlist.put("q217", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(k5) <> 3");
		
		/*
		tmpqlist.put("q301", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k100k) = 3;");
		tmpqlist.put("q302", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k10k) = 3;");
		tmpqlist.put("q303", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k1k) = 3;");
		tmpqlist.put("q304", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k100) = 3;");
		tmpqlist.put("q305", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k25) = 3;");
		tmpqlist.put("q306", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k10) = 3;");
		tmpqlist.put("q307", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k5) = 3;");
		tmpqlist.put("q308", "select sum(TO_NUMBER(k1k)) from `sqlsetquery` where TO_NUMBER(k4) = 3;");
		*/
		//tmpqlist.put("q210", "select count(*) from `sqlsetquery` where TO_NUMBER(k2) = 2 and TO_NUMBER(kseq) <> 3");
		qlist = Collections.unmodifiableMap(tmpqlist);
	}
	
	static {
		//https://www.mkyong.com/mongodb/java-mongodb-query-document/
		Map<String, DBObject> tmpmonqlist = new HashMap<String, DBObject>();
		//tmpmonqlist.put("q101", new BasicDBObject("k250k", new BasicDBObject("$eq", "2")));
		tmpmonqlist.put("q102", new BasicDBObject("k100k", new BasicDBObject("$eq", 2)));
		tmpmonqlist.put("q103", new BasicDBObject("k10k", new BasicDBObject("$eq", 2)) );
		tmpmonqlist.put("q104", new BasicDBObject("k1k", new BasicDBObject("$eq", 2)) );
		tmpmonqlist.put("q105", new BasicDBObject("k100", new BasicDBObject("$eq", 2)) );
		tmpmonqlist.put("q106", new BasicDBObject("k25", new BasicDBObject("$eq", 2)) );
		tmpmonqlist.put("q107", new BasicDBObject("k10", new BasicDBObject("$eq", 2)) );
		tmpmonqlist.put("q110", new BasicDBObject("k2", new BasicDBObject("$eq", 2)) );
		
		 
		tmpmonqlist.put("q203", compoundFind("k2", 2, "k10k", 3, "$eq","$eq"));
		tmpmonqlist.put("q204", compoundFind("k2", 2, "k1k", 3,"$eq", "$eq"));  
		
		tmpmonqlist.put("q205", compoundFind("k2", 2, "k100", 3, "$eq","$eq"));
		tmpmonqlist.put("q206", compoundFind("k2", 2,"k25", 3,"$eq", "$eq"));
		tmpmonqlist.put("q207", compoundFind("k2", 2, "k10", 3, "$eq","$eq"));
		
		tmpmonqlist.put("q216", compoundFind("k2", 2, "k10", 3,"$eq", "$ne"));
		tmpmonqlist.put("q217", compoundFind("k2", 2, "k5", 3, "$eq","$ne"));
		
		/*
		tmpmonqlist.put("q302", new BasicDBObject("k10k", new BasicDBObject("$eq", 3)));
		tmpmonqlist.put("q303", new BasicDBObject("k1k", new BasicDBObject("$eq", 3)) );
		tmpmonqlist.put("q304", new BasicDBObject("k100", new BasicDBObject("$eq", 3)) );
		tmpmonqlist.put("q305", new BasicDBObject("k25", new BasicDBObject("$eq", 3)) );
		*/
		 
		mongoqlist = Collections.unmodifiableMap(tmpmonqlist);
	}
	
	static BasicDBObject compoundFind(String first,int first_value, String sec, int second_value, String op1, String op2) {
		
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject(first, new BasicDBObject(op1,first_value)));
		obj.add(new BasicDBObject(sec, new BasicDBObject(op2,second_value)));
		andQuery.put("$and", obj);
		return andQuery;	
	}
}
