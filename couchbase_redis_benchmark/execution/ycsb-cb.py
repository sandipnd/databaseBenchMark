import multiprocessing
import os
import sys

records=20000000
ops=2000000000
exec_time=3600
host="172.23.100.204"
port=6379
threads= 30
no_procs =  4
log_path=sys.argv[1]
ycsb_result = dict({key: [] for key in ['Throughput', 'READ_95', 'UPDATE_95', 'INSERT_95', 'SCAN_95']})

def worker(count):
    cmd = "cd /root/redis-ycsb/REDIS_12/YCSB_{} && /root/redis-ycsb/REDIS_12/YCSB_{}/bin/ycsb run couchbase2 -s -P /root/redis-ycsb/REDIS_12/YCSB_0/workloads/workloada -jvm-args=-Dcom.couchbase.connectTimeout=300000 -jvm-args=-Dcom.couchbase.kvTimeout=60000 -p couchbase.bucket=bucket-1 -p couchbase.host=172.23.100.204 -threads {} -p recordcount=20000000 -p couchbase.password=password -p maxexecutiontime={} -p operationcount={}  -p exportfile={}/ycsb_log_{}.txt  -p hdrhistogram.percentiles=80,99,95,50 ".format(count, count, threads, exec_time, ops,log_path,count)
    os.system(cmd)


def pattern(line):
        ttype, measure, value = map(str.strip, line.split(','))
        key = ''
        if ttype == "[OVERALL]" and measure == "Throughput(ops/sec)":
            key = 'Throughput'
        elif ttype == "[READ]" and measure == "95thPercentileLatency(us)":
            key = 'READ_95'
        elif ttype == "[UPDATE]" and measure == "95thPercentileLatency(us)":
            key = 'UPDATE_95'
        elif ttype == "[INSERT]" and measure == "95thPercentileLatency(us)":
            key = 'INSERT_95'
        elif ttype == "[SCAN]" and measure == "95thPercentileLatency(us)":
            key = 'SCAN_95'
        else:
            return
        ycsb_result[key] += [round(float(value))]

def parse_work(mypid):
     with open(filename, "r") as txt:
          for line in txt:
              pattern(line)

def parse_log():
    global log_path
    for i in range(no_procs):
      log_pth = "{}/ycsb_log_{}.txt".format(log_path, i)
      with open(log_pth, "r") as txt:
            for line in txt:
                pattern(line)

if __name__ == '__main__':
    jobs = []
    for i in range(no_procs):
        p = multiprocessing.Process(target=worker, args =(i,))
        jobs.append(p)
        p.start()

    import json    
    f = open('/root/redis-ycsb/ycsb_cb_log.txt','w')
    [job.join() for job in jobs]
    parse_log()
    output = {}
    for key in ycsb_result:
        value = sum(ycsb_result[key])
        if key == "READ_95" or key == "UPDATE_95":
           value = value / len(ycsb_result[key])
           value = float(value) / float(1000)
        output[key] = value

    f.write(json.dumps(output))
    f.close()
