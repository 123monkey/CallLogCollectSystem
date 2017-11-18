#!/bin/bash
params=$@
i=1
for (( i=1 ; i <= 6 ; i = $i + 1 )) ; do
    tput setaf 2
    echo ============= mini$i =============
    tput setaf 7
    ssh -4 mini$i "source /etc/profile ; $params"
done