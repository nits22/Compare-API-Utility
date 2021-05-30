# Compare-API-Utility

• Compare-api-utility is scalable, standalone spring-boot based
application for comparing API responses.

• Response can be of type JSON, XML or even plain text.

# Features:

1. This uses Apache Kafka as distributed event streaming platform 
at the backend which is High-throughput and fault-tolerant.
2. Apache Kafka is able to handle these messages with very low latency
of the range of milliseconds.
3. Kafka Publish JSON/String message as a request object which is then
handle by ThreadPoolExecutor by running multiple threads.
4. ApiUtility for getting API response for different request URL.
5. CompareJSON &amp; CompareXML classes are used to compare two JSON
and XML responses. Both can compare JSON and XML ignoring
different order of key values, tags, attributes and whitespaces.
6. Two separate files has been created to provide APIs. “EventListener”
pushes all data to Kafka queue which is then consumed by Kafka
consumer, comparing API responses through ThreadPoolExecutor.
7. Application is run in multiple threads and can cater to million
streams/events and is fault tolerant by creating replicas and
optimizing partions.

**Tools &amp; Languages**

• Spring-boot
• Kafka
• Maven
• Java
• TestNG
• Logback
• Extent Reports
• GSON

# Usage

**Download Kafka**
https://kafka.apache.org/downloads

**Start Zookeeper:**
bin/zookeeper-server-start.sh config/zookeeper.properties

**Start Kafka Server:**
bin/kafka-server-start.sh config/server.properties

**Create Kafka Topic:**
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-
factor 1 --partitions 1 --topic Kafka_Example

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-
factor 1 --partitions 1 --topic Kafka_json

# Running Application
Go to compare-api-utility directory and run below command:

mvn clean spring-boot:run

# Running Tests

• Two files File 3 and File 4 are placed in src/main/resources.

• Extent report will be generated under “compare-api-utility/result-
files/extent-reports” with current timestamp as file name.

• For running tests, run below command:

mvn clean test

# Extent Report

<img src=https://github.com/nits22/Compare-API-Utility/blob/master/image.png width="400" height="200"/>

**Assumptions:**
1. If Both response are null, then APIs are equal
2. If Both response are empty, then APIs are equal
3. Both have same error message response, then APIs are equal
4. Both have same response but different response status, then APIs are equal.
