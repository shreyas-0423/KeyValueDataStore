# KeyValueDataStore
Build a file based key-value datastore that supports the basic CRD(Create, Read, Delete) operations which is built to be used as a local storage by one single process on one laptop. The data store is exposed as a library to clients that can instantiate a class and can work eith the data Store.

# Functional Requirements:
1. It can be initialized using an optinal file path. If one is not provided it will create itself in a reasonable location on the laptop.
2. A new key-value pair can be added to the data storeusing the create operation. The key is always a String - capped at 32chars. The value is always a JSON object - capped at 16KB. If anyone of these constraints is violated then exceptions will be thrown correspondingly.
3. If create s invoked for an existing key the, DuplicateKeyException will occur.
4. A read operation on a key can be performed by providing the key, the response is the value returned as a JSONObject.
5. A delete operation can be performed by providing the key.
6. Every key supports setting a Tine-To-Live property when it is created. This property is optional, in case it is provided it is treated as an integer defining the number of seconds the key must be retained in the datastore. Once the TTL has expired the key will no longer be available for read or delete operations.
7. Appropriate error responses are returned or exceptions are thrown if the client uses the datastore in unexpected manner or breaches any constraints.

# Non-Functional Requirements:
1. the size of the file storing data never exceeds 1GB.
2. More than one client process cannot use the same file for the datastore simultaneously, this is acheived by aquiring OS level lock on the file.
3. A client process can access the dataStore using multiple threads, the dataStore is thread-safe as it is using ConcurrentHashMap and BufferedReader which are synchronized in nature.
4. The complete key-value pair is kept in the form of a Hashmap for deriving maximum performance also the data is stored as Base64 encoded to use as less memory as possible, also due to the use of BufferedReader and BufferedWriter, the file never comes in-memory as a complete data chunk.
