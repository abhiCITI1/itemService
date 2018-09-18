A Restful webservice built with below technologies - 

•	Java 1.8
•	Vert.x (core, web)
•	SpringBoot
•	Jackson
•	Jersey
•	IntelliJ Idea

The restful webservice does the below functionalities - 

HTTP POST /items , 
request body { item:{ id: 123, timestamp: 2016-01-01T23:01:01.000Z } } 

should return 201 created


HTTP GET /items 
should return the list of items POSTed in the last 2 seconds or the list of last 100 POSTed items, whichever greater. 
[ {item: {id: 123, timestamp: 2016-01-01T23:01:01.000Z} },  {item: {id: 124, timestamp: 2016-01-01T23:01:01.001Z} },…]


•	The service provides high throughput and low latency feature by using async, non-blocking, polygot platform of vert.x framework.
•	The service uses in-memory cache at application level to improve latency.
•	The implementation runs on jvm with minimized heap footprint by setting below jvm paramters at the time of running the spring boot application
java -Xmx1024m -Xms512m -jar target/itemService-0.0.1-SNAPSHOT.jar

The above command sets the max(1024 MB) and min(512 MB) heap size before starting the application.
