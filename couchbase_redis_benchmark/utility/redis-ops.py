import os
import subprocess

"""
 You need to change hosts with IP. For security reasons i have removed this
 This code is required to instantaneous_ops_per_sec from each redis-server
"""
hosts = [host1, host2, host3]
ports = range(6379, 6379+12)

def count_keys():
    for host in hosts:
        print "hosts" , host
        dbsize = 0
        for port in ports:
            cmd  = "redis-cli -h  {} -p {} info | grep instantaneous_ops_per_sec".format(host, port)
            val = os.popen(cmd).read()
            val = val.split(':')
            ops = int(val[1].strip())
            db_ops += ops
        print "ops is ", db_ops
count_keys()
