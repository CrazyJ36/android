#!/bin/bash

# In Android Terminal:
#su
#setprop service.adb.tcp.port 5555 
#stop adbd
#start adbd

#ToDo:Create proper if 
#else'check if running properly. If not, then:
#adb kill-server
#adb start-server

# configure if needed

adb tcpip 5555

#connect if needed

adb connect 192.168.0.2

# start shell with command

adb shell su -c 'am start "intent:#Intent;action=android.intent.action.MAIN;launchFlags=0x14000000;component=com.abcOrganizer/.lite.shortcut.LabelShortcut;l.com.abcOrganizer.shortcut.LabelShortcut=12;end"'

