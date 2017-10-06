import multiprocessing
import os
records=10000000
ops=100000000
exec_time=1200
host="172.23.100.192"
port=6379
threads= 30
no_procs =  [0,1,2,4]
import sys
log_path=sys.argv[1]
ycsb_result = dict({key: [] for key in ['Throughput', 'READ_95', 'UPDATE_95', 'INSERT_95', 'SCAN_95']})


def setup(count):
    os.system('cd /root/CASSANDRA-YCSB/; rm -rf YCSB_{0}; git clone https://github.com/brianfrankcooper/YCSB.git; mv YCSB YCSB_{1};'.format(count, count))

def worker(count):
    cmd = "cd /root/CASSANDRA-YCSB/YCSB_{} &&  mvn -pl com.yahoo.ycsb:mongodb-binding  -am clean package &&  /root/CASSANDRA-YCSB/YCSB_{}/bin/ycsb run mongodb -s -P /root/CASSANDRA-YCSB/YCSB_0/workloads/workloade  -p mongodb.url=mongodb://{}:27017/ycsb?w=0 -threads {} -p recordcount=10000000 -p maxexecutiontime={} -p operationcount={}  -p exportfile={}/ycsb_log_{}.txt  -p hdrhistogram.percentiles=80,99,95,50 ".format(count, count, host, threads, exec_time, ops,log_path,count)
    print cmd
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
    for i in no_procs:
      log_pth = "{}/ycsb_log_{}.txt".format(log_path, i)
      with open(log_pth, "r") as txt:
            for line in txt:
                pattern(line)


if __name__ == '__main__':
    jobs = []
    for i in no_procs:
        setup(i)
        
    for i in no_procs:
        p = multiprocessing.Process(target=worker, args =(i,))
        jobs.append(p)
        p.start()

    import json 
    os.system("mkdir -p {0}".format(log_path))   
    f = open('{0}/ycsb_mongodb_log.txt'.format(log_path),'w')
    [job.join() for job in jobs]
    parse_log()
    output = {}
    for key in ycsb_result:
        value = sum(ycsb_result[key])
        if key == "SCAN_95" or key == "INSERT_95":
           value = value / len(ycsb_result[key])
           value = float(value) / float(1000)
        output[key] = value

    f.write(json.dumps(output))
    f.close()
