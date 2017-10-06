import os

inst = 12
hosts = [host1, host2, host3]
ports = range(6379, 6379+inst)

for h in hosts:
  for p in ports:
        cmd = " redis-cli -h {} -p 6379 cluster meet {} {}".format(h, h,p)
        os.system(cmd)

