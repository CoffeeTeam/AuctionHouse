#!/bin/bash
TOMCAT_PATH=/home/mozzie/apache-tomcat

#Init resources
cp resources/context.xml    $TOMCAT_PATH/conf/;
cp resources/web.xml        $TOMCAT_PATH/webapps/axis/WEB-INF/;
cp mysql-connector-java-5.1.7-bin.jar $TOMCAT_PATH/lib;

