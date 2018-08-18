import java.io.{BufferedWriter, File, FileWriter}

import scala.io.Source

object TransactionStatistics {

  def main(args: Array[String]): Unit = {

    val fileName: String = "./src/main/resources/transactions.txt"
    // Read data
    val data: List[Transaction] = csvReader(fileName)

    val totalAmountPerDay:Map[Int, Double] = groupDaySumAmount(data)
    val averagePerAccountCat:Map[String, Map[String, Double]] = groupIDCatAvgAmount(data)
    val lastFiveStatistics:List[AccountStats] = lastFiveDayStatistics(data)

    if(args(0) == "files"){
      writeListToFile(lastFiveStatistics, "AccountStats.txt")
    }else{
      printingToConsole(totalAmountPerDay, averagePerAccountCat, lastFiveStatistics)
    }
  }


  def writeListToFile(data:List[AccountStats], fileOutputName:String = "AccountStats.txt"): Unit={
    val file = new File(fileOutputName)
    val writer = new BufferedWriter(new FileWriter(fileOutputName))
    data.foreach(writer.write)
    writer.close()


  }

  /**
    * This function will take the transaction data and print the outputs from three different functions:
    * groupDaySumAmount
    * groupIDCatAvgAmount
    * lastFiveDayStatistics
    *
    * @param data List[Transaction] - A List which holds transaction data
    */
  def printingToConsole(totalAmountPerDay:Map[Int, Double], averagePerAccountCat:Map[String, Map[String, Double]],
                        lastFiveStatistics:List[AccountStats]):Unit={
    println("Calculating the Total number of transactions per Day:")
    totalAmountPerDay.foreach(println)

    println("Calculating the Average number of Transactions per Account ID and Category:")
    averagePerAccountCat.foreach(println)

    println("Calculating Statistics based on Account ID and the previous five days:")
    lastFiveStatistics.foreach(println)
  }


  /**
    * This function will read in a csv from the file path and turn it into a List of transactions.
    *
    * @param fileName String - The file path to the csv
    * @return List[Transaction]
    */
  def csvReader(fileName:String): List[Transaction]  ={
    val transactionsLines = Source.fromFile(fileName).getLines().drop(1)

    //Here we split each line up by commas and construct Transactions
    transactionsLines.map { line =>
      val split = line.split(',')
      Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
    }.toList

  }

  /**
    * This function will take the transaction and group by the day then sum all the transaction amounts within
    * that day.
    *
    * @param transactions List[Transaction] - A List which holds transaction data
    * @return Map[Int, Double]
    */
  def groupDaySumAmount(transactions: List[Transaction]): Map[Int, Double]={
    // grouping by the transaction day then mapping back to the column names, then summing the transaction amounts
    transactions.groupBy(_.transactionDay).mapValues(_.map(_.transactionAmount).sum)
  }

  /**
    * This function will group by the account ID and Catergory then calculate the avgerage of the transaction amount.
    *
    * @param transactions List[Transaction] - A List which holds transaction data
    * @return Map[String, Map[String, Double]
    */
  def groupIDCatAvgAmount(transactions: List[Transaction]): Map[String, Map[String, Double]]={
    // Grouping by the account ID, then in the second column, mapping again with a group by category and calculating
    // the average of the transaction amount
    transactions.groupBy(_.accountId)
        .mapValues(_.groupBy(_.category)
          .mapValues(_.map(_.transactionAmount))
            .mapValues(v => calculateAvg(v))
        )
  }

  /**
    * This function calculates the average of a list by dividing the sum by its size.
    *
    * @param x List[Double] - Holds the double values which will be used to calculate the average
    * @return Double
    */
  def calculateAvg(x: List[Double]): Double = x.sum/x.size

  /**
    * This function will take in a list of transactions and calculate a number of statistics on to based on the accounts
    * past five days from its current day. It returns a new list with the account ID, day and the following statistics:
    * Maximum transaction amount
    * Average transaction amount
    * Category AA Total
    * Category CC Total
    * Category FF Total
    *
    * @param transactions List[Transaction] - A List which holds transaction data
    * @return List[AccountStats]
    */
  def lastFiveDayStatistics(transactions: List[Transaction]): List[AccountStats] ={
    // partitioning the data into sections of the last 5 days of data
    // first partitioning by the account ID
    val window = transactions.groupBy(_.accountId)
      // ordering the transaction values by the day to make the latest day (eg. 10) at the top
      .mapValues(_.sortWith(_.transactionDay > _.transactionDay))
      // partitioning by 6 which includes the current one
      .mapValues(_.sliding(6, 1).toList)


    window.flatMap { group =>
      // _2 gets the transactions list per 5 day row
      group._2.map { t =>
        val currentDay: Int = t.head.transactionDay
        // removing the current row so that's not counted within the statistics for that row, only the previous five
        val lastFiveDays: List[Transaction] = t.drop(1)
        // getting the maximum amount
        val maximum: Double = lastFiveDays.map(_.transactionAmount).max
        // getting the average amount of the last 5 days
        val average: Double = calculateAvg(lastFiveDays.map(_.transactionAmount))
        // getting total of those with the category AA
        val aaTotal: Double = lastFiveDays.filter(_.category == "AA").map(_.transactionAmount).sum
        // getting total of those with the category CC
        val ccTotal: Double = lastFiveDays.filter(_.category == "CC").map(_.transactionAmount).sum
        // getting total of those with the category FF
        val ffTotal: Double = lastFiveDays.filter(_.category == "FF").map(_.transactionAmount).sum

        // Saving the above values as a Account Stats case
        // group._1 is the account ID
        AccountStats(currentDay, group._1, maximum, average, aaTotal, ccTotal, ffTotal)
      }
    }.toList

  }

  //Define a case class Transaction which represents a transaction
  case class Transaction(
                          transactionId: String,
                          accountId: String,
                          transactionDay: Int,
                          category: String,
                          transactionAmount: Double)
  //Defining a case class AccountStats which represents statistic based on account ID's and days
  case class AccountStats(
                          day: Int,
                          accountId: String,
                          maximum: Double,
                          average: Double,
                          aaTotal: Double,
                          ccTotal: Double,
                          ffTotal: Double)

}
