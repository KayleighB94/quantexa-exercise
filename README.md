# quantexa-exercise

This exercise is to take the transaction data and build statistics functions in order to calculate the following:
* The total amount of transaction per day
* The average amount of transaction per ID and Category
* The last 5 days statistics (maximum, average, total for category AA, CC and FF) for each account ID and day.

# Running the package

```shell
sbt run
```
[More n running SBT commands](https://www.scala-sbt.org/1.x/docs/Running.html)

# Running the tests
The test created for the functions within the TransactionStatistics object were written using scala's scalatest 3.0.5
library.
In order to run the tests, you need to navigate to the project directory then run the command below in sbt shell.
```shell
sbt test
```

#Improvements
* Change to Scala Spark
* Stored the output as files
* More unit test
* Add more error handling into the methods