import os

hosts = [host1, host2, host3]
ports = range(6379, 6379+12)

hashes = 16384
nodes =  len(ports) * len(hosts)
lastcount = count  = 0

each = hashes / nodes
for h in hosts:
  for p in ports:
        cmd = "redis-cli -h {} -p {} flushall".format(h, p)
        os.system(cmd)
        cmd = "redis-cli -h {} -p {} cluster reset hard".format(h, p)
        os.system(cmd)
