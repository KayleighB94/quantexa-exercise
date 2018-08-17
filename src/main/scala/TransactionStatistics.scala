import scala.io.Source

object TransactionStatistics {

  def main(): Unit = {

    val fileName: String = "./src/main/resources/transactions.txt"
    // Read data
    val data: List[Transaction] = csvReader(fileName)

  }

  /**
    * This function will read in a csv from the file path and turn it into a List of transactions.
    *
    * @param fileName String - The file path to the csv
    * @return List[Transaction]
    */
  def csvReader(fileName:String): List[Transaction]  ={
    val transactionslines = Source.fromFile(fileName).getLines().drop(1)

    //Here we split each line up by commas and construct Transactions
    transactionslines.map { line =>
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
    transactions.groupBy(_.transactionDay).mapValues(_.map(_.transactionAmount).sum)
  }

  /**
    * This function will group by the account ID and Catergory then calculate the avgerage of the transaction amount.
    *
    * @param transactions List[Transaction] - A List which holds transaction data
    * @return Map[String, Map[String, Double]
    */
  def groupIDCatAvgAmount(transactions: List[Transaction]): Map[String, Map[String, Double]]={
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
  def calculateAvg(x: List[Double]): Double ={
    x.sum/x.size
  }

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
  def lastFiveDayStatistics(transactions: List[Transaction])={
    val window = transactions.groupBy(_.accountId)
      .mapValues(_.groupBy(_.transactionDay)
          .mapValues(_.sortBy(_.transactionDay).reverse)
        .iterator.sliding(6, 1).toList
      )
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
                          aatotal: Double,
                          cctotal: Double,
                          fftotal: Double)

}
