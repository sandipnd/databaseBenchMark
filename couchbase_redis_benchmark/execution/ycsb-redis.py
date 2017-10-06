import multiprocessing
import os
import json
import sys

records=20000000
ops=200000000
exec_time=600
host="172.23.100.204"
port=6379
threads= 30
no_procs =  4
log_path=sys.argv[1]
ycsb_result = dict({key: [] for key in ['Throughput', 'READ_95', 'UPDATE_95', 'INSERT_95', 'SCAN_95']})


def worker(count):
    cmd ="cd /root/redis-ycsb/YCSB_{} && ./bin/ycsb run redis -s -P workloads/workloada -p redis.host=172.23.100.204 -p recordcount=20000000  -p operationcount={} -p maxexecutiontime={} -p exportfile={}/ycsb_log_{}.txt  -threads {} -p hdrhistogram.percentiles=80,99,95,50".format(count,ops,exec_time, log_path,count, threads)
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


def parse_log():
    global log_path
    for i in range(no_procs):
        log1_path = "{}/ycsb_log_{}.txt".format(log_path, i)
        with open(log1_path, "r") as txt:
            for line in txt:
                pattern(line)

if __name__ == '__main__':
    jobs = []
    for i in range(no_procs):
        p = multiprocessing.Process(target=worker, args =(i,))
        jobs.append(p)
        p.start()
    f = open('/root/redis-ycsb/ycsb_log.txt','w')
    [job.join() for job in jobs]
    parse_log()
    output = {}
    for key in ycsb_result:
        value = sum(ycsb_result[key])
        if key == "READ_95" or key == "UPDATE_95":
            value = value/len(ycsb_result[key])
            value = float(value) / float(1000)
        output[key]= value

    f.write(json.dumps(output))
    f.close()
