#!/bin/bash

#Create proper if else'
#check if running properly, if not

adb kill-server
adb start-server

# configure if needed

adb tcpip 5555

#connect if needed

adb connect 192.168.0.6

# start shell with command

adb shell su -c 'am start "intent:#Intent;action=android.intent.action.MAIN;launchFlags=0x14000000;component=com.abcOrganizer/.lite.shortcut.LabelShortcut;l.com.abcOrganizer.shortcut.LabelShortcut=12;end"'

