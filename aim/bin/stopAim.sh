#!/bin/sh

CATALINA_HOME=/opt/aim/default-aim

export CATALINA_PID=$CATALINA_HOME/logs/.pid

cd $CATALINA_HOME

bin/shutdown.sh -force
