**Spring Batch Example**

 This example performs the below tasks:

 - Read a csv file (name, breed, age in months)
 - Homologate breed values 
 - Write new values into the DB
 
**Notes:** 
One batch can contains 1,* step
One step can contains:
 - 1 reader
 - 1 writer
 - 1 processor (optional)