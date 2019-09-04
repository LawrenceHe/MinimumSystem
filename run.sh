#!/bin/sh

for jarname in `ls target`
do
pid=`ps aux | grep $jarname | grep -v grep | awk '{print $2}'`
echo kill $jarname pid:$pid
kill -9 $pid
done

sleep 2

for jarname in `ls target`
do
echo start $jarname 
nohup java -jar $jarname -server -Xms512m -Xmx512m > ./run.log 2>&1 &
done

