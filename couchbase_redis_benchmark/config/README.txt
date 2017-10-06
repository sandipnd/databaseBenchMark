Two files are provided to start Redis-server. We start 12 redis-servers at 3 nodes.
Total number of instances for all master set up is 36.

Total number is instance for master-replica setup is 48. 24 master and 24 replicas.

Data is persisted on /data. /data is using SSD.

We start the redis servers using ansible. Before we start redis-server, we make sure cocuhbase-server is not running.
pip install -r requirements.txt
ansible -i redis.ini redis -m shell -a "service couchbase-server stop; pkill -9 redis; rm -rf /data/*; python /root/Redis-cluster/redis_12_node.py; sleep 10"
