set -x
workload=workloada
records=20000000
exec_time=600
host=172.23.100.204
port=6379
threads=50
log_path=/root/redis-ycsb/logs


cd /root/redis-ycsb/YCSB_0
/root/redis-ycsb/YCSB_0/bin/ycsb load redis -s -P workloads/${workload}  -p redis.host=${host} -p redis.port=${port} -p recordcount=${records} -threads ${threads}
