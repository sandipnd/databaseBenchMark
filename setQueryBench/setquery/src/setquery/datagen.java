package setquery;

import java.text.NumberFormat;
import java.util.Random;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Iterator;
import com.couchbase.client.java.document.json.*;

/*
 * The schema looks like 
 * https://jimgray.azurewebsites.net/BenchmarkHandbook/chapter6.pdf
 * Check FIgure 3.1 for datagen Algorithm
 * 
 * 
 */
public class datagen implements Iterable<HashMap<String, String>> {

	private int start, end, cursor;

    public datagen(int start, int end) {
        this.start = start;
        this.end = end;
        this.cursor = start;
    }
    
	private HashMap<String, String> dataload() {
		
		HashMap<String, String> data = new HashMap<String, String>();
		Random rand = new Random();
		data.put("sortbin", "12345678");
		data.put("sortpack", "12345678");
		data.put("sortchar", "12345678");
		data.put("s4","123456789012345678901234567890"); 
		data.put("s5", "123456789012345678901234567890"); 
		data.put("s6","123456789012345678901234567890"); 
		data.put("s7" ,"12345678901234567890");
		data.put("s8","1234567890123456789012"); 
		
		Integer kpart;
		
		String dkeys[] = { "k500k", "k250k", "k100k", "k40k", "k10k", "k1k", "k100", 
				"k25", "k10", "k5", "k4", "k2"	
		};
		Integer maxValCol[] = {500000,250000,100000,40000,10000,1000,100,25,10,5,4,2};
		double val = Math.pow(7, 5);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		rand = new Random(System.currentTimeMillis());
			for(int j = 0; j < 12; j++) {
				long seed = (long) (val * rand.nextLong() %  2147483647);
				data.put(dkeys[j], Long.toString(Math.abs(seed % maxValCol[j]) + 1));
			}
		return data;
	}
	
	@Override
	public Iterator<HashMap<String, String>> iterator() {
		Iterator<HashMap<String, String>> it = new Iterator<HashMap<String, String>>() {

            private int currentIndex = 0;
            @Override
            public boolean hasNext() {
                return cursor < end;
            }
            @Override
            public HashMap<String, String> next() {
                if(this.hasNext()) {
                	cursor ++;
                	return dataload();
                }
                throw new NoSuchElementException();
            }
		};
        return it;
    }
}
	
