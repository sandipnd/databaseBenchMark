 bin/ycsb run cassandra-cql -s -P /root/perfrunner-ycsb/YCSB_0/workloads/workloade  -p hosts="172.23.100.191" -threads 35 -p recordcount=20000000 -p maxexecutiontime=1200 -p operationcount=100000000  -p cassandra.keyspace=ycsb  -p exportfile=/tmp/ycsb_cassandra_log.txt  -p hdrhistogram.percentiles=80,99,95,50
