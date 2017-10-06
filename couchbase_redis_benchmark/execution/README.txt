This benchmark test is performed on different number of ( 1,2,3,4) bare metal nodes.
Ansible is used to run the test on each node.
Each node will have own YCSB repo and ycsb-redis.py . Each test will aggregate YCSB results and return to master.
Master node is the one from where ansible is launched.

Test is run for 600 seconds.
We use 120 threads per node, for 4 nodes it will 480 threads. 
To execute this sh run-redis.sh <logpath> 

Server side stats are collected through Influxdb, Graphana

DataLoad:
The following are details of Data Loading.
You can change the IP address, document count, ops count , thread count in the script.

Loading the data to Redis is performed using load-ycsb-redis.sh
Run the command as

sh load-ycsb-redis.sh

Loading the data to couchbase is performed using load-cb.sh
Run the command as

sh load-cb.sh
=======
Loading the data to couchbase is performed using load-cb.sh
