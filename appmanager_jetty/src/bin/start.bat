rem echo off
set java= "%JAVA_HOME%"\bin\java
set port=80

set cp=.
set cp=%cp%;..\lib\appmanager_jetty-1.0-SNAPSHOT.jar
set cp=%cp%;..\lib\appmanager_web-1.0-SNAPSHOT.war
set cp=%cp%;..\lib\hamcrest-core-1.3.jar
set cp=%cp%;..\lib\javax.servlet-api-3.1.0.jar
set cp=%cp%;..\lib\jetty-http-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-io-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-security-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-server-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-servlet-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-util-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-webapp-9.3.15.v20161220.jar
set cp=%cp%;..\lib\jetty-xml-9.3.15.v20161220.jar
set cp=%cp%;..\lib\junit-4.12.jar

explorer http://localhost:%port%
%java% -cp %cp% com.tendyron.routewifi.appmanager.jetty.App %port% "..\lib\appmanager_web-1.0-SNAPSHOT.war"