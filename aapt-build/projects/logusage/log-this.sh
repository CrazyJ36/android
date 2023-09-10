#!/bin/bash

# This shows data only when adb logcat detects your log messages for this app.
adb logcat | grep LOGUSAGETAG
