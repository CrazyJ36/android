#!/data/data/com.termux/files/usr/bin/bash

# run javafile.class file in termux, first install depends:
# pkg install dx ecj
# compile java text file with:
# ecj file.java
# run-java.sh className

file=$1.class
dx --dex --output out.dex $file
file=$1
dalvikvm -cp out.dex $file
