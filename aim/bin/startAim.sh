#!/bin/sh

CATALINA_HOME=/opt/aim/default-aim

export CATALINA_PID=$CATALINA_HOME/logs/.pid

PROC_ID=$$
#export JAVA_OPTS=" -server -Dapplication.name=cha-Default -Xms128m -Xmx500m "
export JAVA_OPTS=" -server -Dapplication.name=INSTANCE_NAME -Xmx500m -Dfile.encoding=UTF-8 "
export JAVA_OPTS=" $JAVA_OPTS -Dprocess.id=$PROC_ID -Djava.util.logging.config.file=$CATALINA_HOME/conf/logging.properties "
export JAVA_OPTS=" $JAVA_OPTS -Dconfig.dir=$CATALINA_HOME/config -Dtpm.url=$TPM_URI -Dprocess.id.file=$CATALINA_PID -Dtpm.template.dir=$CATALINA_HOME/tpmtemplates "
export JAVA_OPTS=" $JAVA_OPTS -Dno.snmp.traps=true "
export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=@JMX_PORT@ -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
export JAVA_OPTS="$JAVA_OPTS -XX:+UseParallelGC -Xbootclasspath/a:$CATALINA_HOME/lib/tamboot.jar:$CATALINA_HOME/lib/xercesImpl.jar:$CATALINA_HOME/lib/xml-apis.jar:$CATALINA_HOME/lib/agentxapi.jar:$CATALINA_HOME/lib/snmp.jar:$CATALINA_HOME/config"


cd $CATALINA_HOME

bin/catalina.sh "$@" >> $CATALINA_HOME/logs/aim.log 2>&1
