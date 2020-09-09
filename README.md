# avro-examples

This repository includes multiple tests related to the [Apache Avro framework](https://avro.apache.org/) and its inner workings. Apache Avro is a popular data serialization library that is closely related to Apache's Hadoop environmnent.

Currently, our code contains the following experiments:

* [Test 1: Schema evolution](#-test-1:-schema-evolution)

* [Test 2: Inter-process communication: Null treatment](#-test-2:-inter-process-communication:-null-treatment)

* [Test 3: Inter-process communication: Stress test](#-test-3:-inter-process-communication:-stress-test)

## Test 1: Schema evolution

Avro allows us to define data schemas for easy data serialization. These schemas can later be modified and updated without losing backward or forward compatibility between different versions of readers and writers. To achieve that, Avro follows a set of rules that determine whether two versions of a schema are compatible. The Apache Avro project provides a definition of all these rules in [the documentation pages](https://avro.apache.org/docs/current/spec.html#Schema+Resolution).

We ran a group of tests to evaluate how these rules apply to different schemas.

We check for compatibility on the following Avro schema types: primitive, unions, arrays, maps, enums, records and fixed types. When it comes to the primitive types, we used the following: INT, LONG, FLOAT, STRING, BYTES and NULL. 
 
For each test, we provided a writer schema, used to serialize data to a file, and a reader one, which will try to deserialize the given object. We make sure that the object can be correctly serialized, deserialized and that the final object has the expected values. This repository contains the following tests:

* `arrays.java`: Arrays may vary in item types. These types may or may not be promotable.

* `maps.java`: Maps may vary in item values. These items may or may not be promotable.

* `fixed.java`: Fixed schemas may have different sizes or names.

* `enums.java`: Enumerations may have different length, item names and item types. 
	*  Item values, they may or not be promotable.
	*  Item names may vary of be in a different order.
	*  THe reader or writer schemas can have different lengths. 

* `records.java`: Records may have different number of fields and fields with different names, order or type.
	*  Fields can have different names.
	*  Some fields can have different types that may or may not be promotable.
	*  Fields might be ordered differently.
	*  The reader or writer schemas can have different lengths.  
	*  Field types may be primitive or complex.

* `union.java`: Elements in the unions may or may not intersect, have different values or order. We also tried to convert from an union type to a primitive type and *vice versa*.
 	* The type of the elements inside the union may be the same, promotable or completely different.
 	* The set elements between both schemas always intersect (except for the baseline test) and the object we are trying to serialize may or may not be present in each schema.
 	* The order of the elements inside the union may vary.
 	* We tried to promote union schemas to primitive ones and the other way around.

* `primitive.java`: We check for the promotion rules to apply when using primitive types alone. All the other test scripts include instances when these types are inside a more complex schema (union, array, etc) and need to be promoted.
 
In addition, every Avro schema type contains a test where the writer and reader schemas are the same to serve as a baseline that the library is working properly.

## Test 2: Inter-process communication: Null treatment

Avro provides an Inter-Process Communication (IPC) framework that facilitates the conversation between two different processes. It allows us to define different communication protocols that define which serialized objects we wish send/receive.

We assumed that the schema evolution rules apply to these examples too, as we had already tested them in [Test 1]. However, we wanted to see how the framework would handle unexpected objects (particularly `null`s) during a conversation.

We tested the communication between clients and server using two popular services: Jetty and Netty. The former, Jetty, is a Java HTTP server, and the latter, Netty, can deal with asynchronous calls. For the purpose of these experiments, we only used synchronous communications. Avro provides methods to easily interact with both of them in different `org.apache.avro.ipc` subpackages.

We ran the following tests for each client/server framework:

* Send and receive an object that does not contain a `null` value. These objects are as defined in the communication protocols.

* Send and receive an object that does contain `null` values or are `null`s themselves. These objects are as defined in the communication protocols, i.e. the client/server expects these `null` values.

* Send and receive `null` values that are not specified in the communication protocols, i.e. the client/server expects a different object. With these tests we simulate a possible error happening in any the client of the server.

We assessed the behaviour of the Avro framework using both Plain Old Java Object (POJO) with multiple parameters (defined as complex Avro records) and primitive values (defined as primitive Avro objects). We used both "one-way" and "two-ways" communications.


## Test 3: Inter-process communication: Stress test

While analyzing one-to-one communications can help us to understand how both client and server behave, one server can deal with multiple clients at the same time. We wanted to see what would happen if a server cannot attend all requests immediately and needs to queue them.

We designed a test where an multiple clients request an expensive operation to the server at the same time. If the number of clients is big enough, they take over all the threads the server can raise. We wanted to see if the server would block any new incoming requests after that point or how it would handle the communications.

Again, we used Jetty and Netty as in [Test 2]().

## Execution

The easiest way to run this project is by using Maven:

```
mvn clean generate-resources install

cd target/

java -jar -Dlog4j.configuration=file:///PATH/TO/log4j.properties avro-examples-0.1-jar-with-dependencies.jar OPTION
```

Where the `OPTION` parameter can be one of the following:

*  `schemas`, for [Test 1: Schema evolution](#-test-1:-schema-evolution).

*  `communication`, for [Test 2: Inter-process communication: Null treatment](#-test-2:-inter-process-communication:-null-treatment).

*  `stress`, for [Test 3: Inter-process communication: Stress test](#-test-3:-inter-process-communication:-stress-test).


The user should know the following:

*  [Test 1: Schema evolution](#-test-1:-schema-evolution) needs the schema definitions, that are not included inside the `.jar`. They need to be accessible from the CLASSPATH. The easiest way to achieve this is to always have the `avro` folder (generated during installation) in the same directory as the `.jar`.

*  Our library heavily relies on the SLF4J logging system to communicate results to the user. When building the `.jar`, Maven will place a `log4j.properties` file in the same folder. This file is in the `src/main/resouces` location in the source code. The user if free to use this configuration file or any other he/she might deem appropriate.

*  To better visualize the results of each test, the default logging configuration creates a file `avro-examples.log` with only the log messages of our tests. 

## Conclusions

### Test 1

*  We found no major violations of the aforementioned rules for the schema resolution.

*  Even if two versions of a schema are not compatible, Avro will not raise an error until the conditions for that incompatibility are found. E.g.: An enumeration schema with two values `["Option_1", "Option_2"]` can be used to read records where the writer had a schema such as `["Option_1", "Option_2", "Option_3"]`. We will not see an error until the reader finds an `Option_3` value that cannot process. This affects both enumerations and unions. We demonstrate this in the enumerations test number 3 and the unions test number 3.

### Test 2

* Communication between processes happens without any problem with and without `null` objects as long as those objects are specified on the communication protocols.  

* When sending an object that is not specified in the protocol (whether it be `null` or any other object), the process that is trying to send the message will raise an exception. Avro's servers and transceiver handle these errors so they do not end in the catastrophic failure of the application.

### Test 3

* Servers will not drop any incoming requests if they cannot deal with them the moment they receive them. Instead, they'll include them in a First-In First-Out(FIFO) queue. 

* Unless specified, clients will wait indefinitely for a response from the server.

* By default, Avro's Jetty configuration allows it to handle many more requests than Netty's, which limits the thread pool for answering responses to only 16.

## Other information

*  Java version: 1.8

*  Avro version: 1.10

## TO DO

* Make it an JUnit test

* Clean some TODOs
