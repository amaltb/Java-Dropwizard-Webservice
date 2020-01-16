#!/bin/bash
java -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=1098 -Dapplication.name=doppler-metastore-service -Dapplication.home=. -Dapplication.environment=dev -jar service/target/doppler-metastore-service.jar server service/target/classes/config/dev.yml
