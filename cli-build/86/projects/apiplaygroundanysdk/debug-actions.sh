#!/bin/bash
if [ -e log.txt ]; then
    rm log.txt
fi
adb logcat -c
printf "Do what is causing crash on phone, push ctrl-c on pc, then 'less log.txt' on pc."
adb logcat > log.txt

