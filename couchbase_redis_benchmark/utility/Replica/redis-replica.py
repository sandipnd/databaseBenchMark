import os
import itertools

inst = 6
hosts = [host1, host2, host3]
ports = range(6379, 6379+24)

import commands
hcycle = itertools.cycle(hosts[:2])

c = 1

hnext = hcycle.next()
for p in ports:
        if p > 6390 and c == 1:
          hnext = hcycle.next()
          c  = 0
        print "host is ", hnext
        if p > 6390:
           pm = p - 12

        else:
           pm = p

        comm = "redis-cli -h {} -p {} cluster nodes | grep myself".format(hnext, pm)
        print comm
        val = commands.getstatusoutput(comm)

        val = val[1].split()
        val = val[0]
        cmd = " redis-cli -h {} -p {} cluster replicate {}".format(hosts[2], p,val)
        print cmd
        os.system(cmd)
