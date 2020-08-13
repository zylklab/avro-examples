# avro-examples

Avro allows us to define data schemas for easy data serialization. These schemas can later be modified and updated without losing backward or forward compatibility between different versions of readers and writers. To achieve that, Avro follows a set of rules that determine whether two versions of a schema are compatible. The Apache Avro project provides a definition of all these rules in [the documentation pages](https://avro.apache.org/docs/current/spec.html#Schema+Resolution).

We ran a group of tests to evaluate how these rules apply to different schemas.


## Tests


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

## Execution

The easiest way to run this project is by using Maven:

```
mvn clean install && mvn clean compile assembly:single

java -jar target/avro-examples-0.1-jar-with-dependencies.jar
```

## Conclusions

*  We found no major violations of the aforementioned rules for the schema resolution.

*  Even if two versions of a schema are not compatible, Avro will not raise an error until the conditions for that incompatibility are found. E.g.: An enumeration schema with two values `["Option_1", "Option_2"]` can be used to read records where the writer had a schema such as `["Option_1", "Option_2", "Option_3"]`. We will not see an error until the reader finds an `Option_3` value that cannot process. This affects both enumerations and unions.

*  Although not a violation of any rule, we do find counter-intuitive that when dealing with enumerations, the default value of the reader's schema may not exist in the writer's one. However, the default value of a reader's union schema must match a type (or a promotion of a type) that exists in the writer's schema.

## Other information

*  Java version: 1.8

*  Avro version: 1.10

## TO DO

* Include test for remote invocation with Avro IPC
