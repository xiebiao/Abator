#!/bin/sh

export CLASSPATH=$CLASSPATH:../libs/*:../conf/config.properties
echo $CLASSPATH
java -classpath $CLASSPATH com.github.abator.Main 
