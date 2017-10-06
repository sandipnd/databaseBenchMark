import os

"""
port 6378
cluster-enabled yes
cluster-config-file nodes9.conf
cluster-node-timeout 5000
protected-mode no

"""

for i in range(1,13):
   filename = "redis-master{}.conf".format(i)
   fp =  open(filename, 'w')
   fp.write("port {} \n".format(6378+i))
   fp.write("cluster-enabled yes\n")
   fp.write("cluster-config-file nodes{}.conf\n".format(i))
   fp.write("cluster-node-timeout 5000\n")
   fp.write("protected-mode no\n")
   fp.write("logfile /data/nodes{}.log \n".format(i))
   fp.write("dir /data \n")
   fp.write("appendfsync everysec\n")
   fp.write("appendonly yes\n")
   fp.write("appendfilename db_{}.aof\n".format(i))
   fp.close()
   cmd = " redis-server redis-master{}.conf &".format(i)
   print cmd
   os.system(cmd)
