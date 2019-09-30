#!/bin/sh

echo updating code...
git pull

mvn install -f common/pom.xml
mvn package -f app/pom.xml
mvn package -f auth/pom.xml
mvn package -f demo/pom.xml
mvn package -f discovery/pom.xml
mvn package -f gateway/pom.xml
mvn package -f usercenter/pom.xml

cp app/target/app-*.jar target
cp auth/target/auth-*.jar target
cp demo/target/demo-*.jar target
cp discovery/target/discovery-*.jar target
cp gateway/target/gateway-*.jar target
cp usercenter/target/usercenter-*.jar target

