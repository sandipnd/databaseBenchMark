
cqlsh:ycsb> SELECT * FROM system_schema.keyspaces;

 keyspace_name      | durable_writes | replication
--------------------+----------------+-------------------------------------------------------------------------------------
               ycsb |           True | {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1'}
        system_auth |           True | {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1'}
      system_schema |           True |                             {'class': 'org.apache.cassandra.locator.LocalStrategy'}
          keyspace1 |           True | {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1'}

4 node cluster created

nodetool status
Datacenter: datacenter1
=======================
Status=Up/Down
|/ State=Normal/Leaving/Joining/Moving
--  Address         Load       Tokens       Owns (effective)  Host ID                               Rack
UN  172.23.100.193  5.32 GB    256          27.3%             5cbb65a9-75b3-4568-b5a9-05e1aae2c991  rack1
UN  172.23.100.192  4.91 GB    256          25.6%             2b47c42c-44e4-4883-a1c0-7aee03f722b9  rack1
UN  172.23.100.191  4.5 GB     256          23.4%             411e3d21-78ab-4a36-977e-f0852bb544c6  rack1
UN  172.23.100.190  4.5 GB     256          23.7%             7ba52c1a-7f4f-41f3-b1da-d12ea7934b36  rack1

nodetool version
ReleaseVersion: 3.0.9

# Ref http://narendrasharma.blogspot.com/2011/04/cassandra-07x-understanding-output-of.html
#https://wiki.apache.org/cassandra/NodeTool

ycsb/usertable histograms
Percentile  SSTables     Write Latency      Read Latency    Partition Size        Cell Count
                              (micros)          (micros)           (bytes)
50%             0.00             35.43              0.00              1109                10
75%             0.00             42.51              0.00              1109                10
95%             0.00             73.46              0.00              1109                10
98%             0.00             88.15              0.00              1109                10
99%             0.00            126.93              0.00              1109                10
Min             0.00              8.24              0.00               925                 9
Max             0.00          12108.97              0.00              1109                10








