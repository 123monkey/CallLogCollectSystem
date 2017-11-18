#!/bin/bash
pids=`jps | grep $1 | awk '{print $1}'`
for pid in $pids ; do
    kill -9 $pid
done