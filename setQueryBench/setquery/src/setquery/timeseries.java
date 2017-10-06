package setquery;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBImpl;

public class timeseries  {

	public timeseries() { }
		// TODO Auto-generated constructor stub
	private void test()  throws InterruptedException, IOException {
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
		String dbName = "aTimeSeries";
		influxDB.createDatabase(dbName);
		
		for (int i = 0; i < 100; i++) {
		BatchPoints batchPoints = BatchPoints
						.database(dbName)
						.tag("async", "true")
						.retentionPolicy("autogen")
						.consistency(ConsistencyLevel.ALL)
						.build();
		Point point1 = Point.measurement("cpu")
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.addField("idle", 90L)
							.addField("user", 9L)
							.addField("system", 1L)
							.build();
		Point point2 = Point.measurement("disk")
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.addField("used", 80L)
							.addField("free", 1L)
							.build();
		
		batchPoints.point(point1);
		batchPoints.point(point2);
		influxDB.write(batchPoints);
		}
		Query query = new Query("SELECT idle FROM cpu ", dbName);
		QueryResult result = influxDB.query(query);
		influxDB.deleteDatabase(dbName);
		influxDB.close();
	}
	
	public static void main(String args[]) throws InterruptedException, IOException {
		timeseries ts = new timeseries();
		ts.test();
	}

}
