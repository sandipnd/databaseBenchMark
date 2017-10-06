import os
import subprocess

hosts = [host1, host2, host3]
ports = range(6379, 6379+12)

def count_keys():
    dbsize = 0
    for host in hosts:
        print "hosts" , host
        dbsize = 0
        for port in ports:
            cmd  = "redis-cli -h  {} -p {} info | grep used_cpu_user:".format(host, port)
            val = os.popen(cmd).read()
            val = val.split(':')

            ops = float(val[1].strip())
            dbsize += ops
        print "ops is ", dbsize/12
count_keys()
