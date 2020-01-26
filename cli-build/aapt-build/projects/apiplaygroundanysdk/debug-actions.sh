#!/bin/bash

# if log.txt exists, remove it.
if [ -e log.txt ]; then
    rm log.txt
fi

if [ -z $1 ]
  then
    printf "Usage: debug-actions.sh DEVICE_SERIAL\n"
    exit
fi
adb -s $1 logcat -c
printf "Do what is causing crash on phone, push ctrl-c on pc, then 'less log.txt' on pc.\n"
adb -s $1 logcat > log.txt
