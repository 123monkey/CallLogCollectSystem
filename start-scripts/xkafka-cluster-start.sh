#!/bin/bash
servers="mini2 mini3 mini4"
for s in $servers ; do
        ssh $s "source /etc/profile ; kafka-server-start.sh -daemon /home/hadoop/soft/kafka-2.11/config/server.properties"
done