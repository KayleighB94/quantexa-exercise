import TransactionStatistics.{Transaction, csvReader}
import org.scalatest.FunSuite
import TransactionStatistics._

class TransactionStatisticsTest extends FunSuite {

  test("testCalculateAvg") {
    val values: List[Double] = List(10.5, 7.8, 2.4, 7.5, 8.7)
    val average = calculateAvg(values)

    assert(average == 7.38)
  }

  test("testGroupDaySumAmount") {
    val inputdata: List[Transaction] = csvReader("./src/test/resources/test_data.txt")
    val output:Map[Int, Double] = groupDaySumAmount(inputdata)

    val expected = Map((1, 55.2), (2, 23.8), (3, 103.2))

    assert(output == expected)
  }

  test("testCsvReader") {
    val fileName: String = "./src/test/resources/test_data.txt"
    // Read data
    val outputdata: List[Transaction] = csvReader(fileName:String)
    val expected: List[Transaction] = List(
      Transaction("T1","a",1,"GG",10.5),
      Transaction("T2","b",1,"BB",23.4),
      Transaction("T3","c",1,"DD",21.3),
      Transaction("T4","d",2,"DD",23.8),
      Transaction("T5","e",3,"BB",14.4),
      Transaction("T5","d",3,"AA",54.6),
      Transaction("T5","a",3,"GG",34.2))

    assert(outputdata == expected)
  }

  test("testGroupIDCatAvgAmount") {
    val inputdata: List[Transaction] = csvReader("./src/test/resources/test_data.txt")
    val output: Map[String, Map[String, Double]] = groupIDCatAvgAmount(inputdata)
    val expected:Map[String, Map[String, Double]] = Map(
      ("a", Map(("GG", 22.35))),
      ("b", Map(("BB", 23.4))),
      ("c", Map(("DD", 21.3))),
      ("d", Map(("DD", 23.8), ("AA", 54.6))),
      ("e", Map(("BB", 14.4)))
    )

    assert(output == expected)
  }

  test("testLastFiveDayStatistics") {

  }

}
