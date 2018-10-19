#!/bin/bash

# File edited to suit by CrazyJ36
# using method from http://www.hanshq.net/command-line-android.html

# Instructions: this expects $JAVA_HOME to be set and the JDK tools to be in $PATH.
# appname (second argument) should be the same as in ./java/com/crazyj36/name

# adding libs like support libraries seems possible after "javac -cp ${PLATFORM}/android.jar:${PLATFORM}/android-support-v4.jar"
# and "aapt -j ${PATH}/to/android-support-v4.jar"
# But this caused crash in app as 'NoClassDefError' for this lib.
# Maybe this is just because I obtain the lib file awkwardly.

# Ideas: catch any problems before script gets to "success.."

printf "\nRun this program as:\nbuild-android.sh SourceDir AppName KeyStorePass\n"

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  printf "Wrong number of arguments.\n"
  exit
else

  workdir=$1
  appname=$2
  mpass=$3

  SDK="${HOME}/development/android/cli-build/86/sdk-21"
  BUILD_TOOLS="${SDK}/build-tools"
  PLATFORM="${SDK}/platforms/android-p"
  PLATFORM_TOOLS="${SDK}/platform-tools"

  mkdir -p $workdir/build/gen $workdir/build/obj $workdir/build/apk
  printf "\nbuild directories created...\n"

  "${BUILD_TOOLS}/aapt" package -f -m -J $workdir/build/gen/ -S $workdir/res \
  -M $workdir/AndroidManifest.xml -I "${PLATFORM}/android.jar"
  printf "aapt success...\n"

  javac -source 1.7 -target 1.7 -bootclasspath "${JAVA_HOME}/jre/lib/rt.jar" \
  -classpath "${PLATFORM}/android.jar" -d $workdir/build/obj \
  $workdir/build/gen/com/crazyj36/$appname/R.java $workdir/java/com/crazyj36/$appname/*.java

  if [ ! $? == 0 ]; then
    printf "Couldn't build java source files.\n"
    exit
  else
    printf "javac R.java built...\n"

    "${BUILD_TOOLS}/dx" --dex --output=$workdir/build/apk/classes.dex $workdir/build/obj/
    printf "classes.dex built...\n"

    "${BUILD_TOOLS}/aapt" package -f -M $workdir/AndroidManifest.xml -S $workdir/res/ -I "${PLATFORM}/android.jar" \
    -F $workdir/build/$appname-packaged.apk $workdir/build/apk/
    printf "aapt success...\n"

    ## apksigner should be available directly in newer sdks. If you have it use here "${BUILD_TOOLS}/apksigner" instead of "apksigner"
    apksigner sign --v1-signing-enabled true --v2-signing-enabled false \
    --ks $HOME/development/android/devkey/keystore.jks \
    --ks-key-alias CrazyJ36DevKey --ks-pass pass:$mpass --key-pass pass:$mpass \
    --out $workdir/build/$appname-signed.apk $workdir/build/$appname-packaged.apk

    #jarsigner -keystore $HOME/development/android/devkey/keystore.jks \
    #-storepass $mpass -keypass $mpass $workdir/build/$appname.aligned.apk CrazyJ36DevKey
    printf "apk signed...\n"

    "${BUILD_TOOLS}/zipalign" -f 4 $workdir/build/$appname-signed.apk $workdir/build/$appname.apk
    printf "zipalign success...\n"

    "${PLATFORM_TOOLS}/adb" install -r $workdir/build/$appname.apk
    printf "apk installed...\n"

    "${PLATFORM_TOOLS}/adb"  shell am start -n com.crazyj36.$appname/.MainActivity

    rm $workdir/build/$appname-packaged.apk $workdir/build/$appname-signed.apk
  fi

fi

