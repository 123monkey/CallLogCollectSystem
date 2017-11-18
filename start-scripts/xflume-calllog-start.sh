#!/bin/bash
cd /home/hadoop/soft/flume-1.7.0/conf
flume-ng agent -f calllog.conf -n a1 &