#!/bin/bash
java -cp $AXISCLASSPATH org.apache.axis.client.AdminClient -lhttp://localhost:8080/axis/services/AdminService deploy.wsdd

