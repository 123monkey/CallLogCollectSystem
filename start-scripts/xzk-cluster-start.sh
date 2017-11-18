#!/bin/bash
servers="mini1 mini2 mini3"
for s in $servers ; do
        ssh $s "source /etc/profile ; zkServer.sh start"
done