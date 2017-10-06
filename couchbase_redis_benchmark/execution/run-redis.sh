ansible -i  redis_client.ini redis -m shell -a "python ycsb-redis.py $1"
 ansible -i  redis_client.ini redis -m shell -a 'cat /root/redis-ycsb/ycsb_log.txt'
