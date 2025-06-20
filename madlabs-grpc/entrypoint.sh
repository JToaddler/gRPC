#!/bin/bash

shutdown(){
	echo "Shuting down... "
	PID = `ps -ef | grep -v grep | grep app.jar | awk '{print $2}'`
	if [[ -n "$PID" ]]
	then
		echo "killing PID "
		kill $PID
		sleep 5
	fi
	
}

trap 'shutdown' SIGTERM

echo "Starting gRPC App... "
exec java -jar ./app/madlabs-grpc.jar

./startProxy.sh & 

wait $!