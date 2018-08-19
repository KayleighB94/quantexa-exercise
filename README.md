# quantexa-exercise

This exercise is to take the transaction data and build statistics functions in order to calculate the following:
* The total amount of transaction per day
* The average amount of transaction per ID and Category
* The last 5 days statistics (maximum, average, total for category AA, CC and FF) for each account ID and day.

# Running the package
There are two ways to get the statistics from the TransactionStatisitcs function, to have it output to the console or
to have them written to files. This is decide by adding an argument at the end of the run command.

If you want it to print to console just run the run command

```sbt shell
run
```
If you want it to write them to files, add files to the end
```sbt shell
run files
```

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