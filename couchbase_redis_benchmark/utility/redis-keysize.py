import os
import subprocess

hosts = [host1, host2, host3]
ports = range(6379, 6379+12)

def count_keys():
    dbsize = 0
    for host in hosts:
        for port in ports:
            cmd  = "redis-cli -h  {} -p {} dbsize".format(host, port)
            val = os.popen(cmd).read()
            dbsize += int(val)
            print " host = {} , port = {}, keys = {} ".format(host,port,val)
            cmd  = "redis-cli -h  {} -p {} config resetstat".format(host, port)
            os.system(cmd)
    print "dbsize is ", dbsize
count_keys()
