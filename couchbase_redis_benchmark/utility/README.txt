To create a cluster  without replication , we need to run these in sequence

python redis-flush.py 
python redis-add-slot.py 
python redis-cluster.py

flush.py will reset the cluster.
add-slot.py will allocate slots
cluster.py will create clusters
