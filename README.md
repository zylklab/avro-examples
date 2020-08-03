# avro-examples

Avro allows us to define data schemas for easy data serialization. These schemas can later be modified and updated without losing backward or forward compatibility between different versions of readers and writers. To achieve that, Avro follows a set of rules that determine whether two versions of a schema are compatible. The reader can find a definition of all these rules at [Avro's Apache documentation](https://avro.apache.org/docs/current/spec.html#Schema+Resolution).

We would like to test how these rules apply to different schemas.


## Tests


We check for full compatibility on the following Avro schema types: primitive types, unions, arrays, maps, enums, records and fixed. This means that each schema should, at least, provide backward and forward compatibility with the immediate previous or next version. As such, each test contains at least two versions of an schema. We use three versions when dealing with the primitive type INT, as they can be promoted to many other types.

For the purpose of this project, we use the following primitive types: INT, LONG, FLOAT, STRING, BYTES and NULL. 
 
This repository contains the following tests:

⋅⋅* `arrays.java`: Arrays may vary in item types. These different types may or may not have share rules of promotion. Two versions of the schema per test.

⋅⋅* `maps.java`: Maps may vary in item values. Two versions of the schema per test. 

⋅⋅* `enums.java`: Enumerations may have different legnth or item names. Two versions of the schema per test. 

⋅⋅* `fixed.java`: Fixed schemas may have different sizes or names. Two versions of the schema per test. 

⋅⋅* `records.java`: Records may have different number of fields (with default values and without them), and fields with different names or different order. Two versions of the schema per test. 

⋅⋅* `union.java`: Elements in the unions may or may not intersect. We pay special attention to the type NULL, as it can only be used inside unions. Two versions of the schema per test. 

⋅⋅* `primitive.java`: We check for the promotion rules to apply when using primitive types alone or when these types are inside a more complex schema (union, array, etc). Two or three versions of the schema per test, depending on the primitive type.
 


## Execution

TODO

## Conclusiones

TODO