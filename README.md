# open_tcp IoT - Service

Simple multi threaded tcp service. Can configure multiple listener,dynamic parser, send mail notification, http post   

features
mulltiple tcp listener
dynamic data packet parser
fetching packet parsing infirmation from rest api auto refresh
support email
support rest api post
inbuild logs,config
used jdk8

add custome listener and parser:

1.create class implements Listener interface.
2. override onMessage method. 
3.create constructor initialize own data parser.


Project Link - https://

main function : com.iot.service.main.IoTService.java

## How to run this project?
```
$ git clone https://github.com/muru-c/open_tcp.git
$ cd oppen_tcp
$ mvn package 
$ java -jar target/java-project-1.0-SNAPSHOT.jar
```
Output
```
--
```

# Author : Muruganandam C
# Email  : murugasrmmca@gmail.com