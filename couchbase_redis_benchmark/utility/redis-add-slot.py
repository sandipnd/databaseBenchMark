import os
"""
 This program is required to provide slots to the cluster
"""
instance= 12
hosts = [host1, host2, host3]
ports = range(6379, 6379+instance)

hashes = 16384
nodes =  len(ports) * len(hosts)
lastcount = count  = 0

each = hashes / nodes + 1
for h in hosts:
  for p in ports:
     lastcount += count
     count = 0
     while count != each:
        cmd = "redis-cli -h {} -p {} CLUSTER ADDSLOTS {}".format(h, p, lastcount + count)
        os.system(cmd)
        count += 1
        if lastcount + count == 16384:
           break
     print lastcount, count
