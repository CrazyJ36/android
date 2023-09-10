#!/bin/bash
export DISPLAY=":1"
vncserver -kill :1
vncserver -geometry 730x600 -localhost
fluxbox-generate_menu
fluxbox &
