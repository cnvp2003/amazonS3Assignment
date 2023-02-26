import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client

object S3Main extends App {

  val BUCKET_NAME = "testbucket"
  val INPUT_FILE_PATH = "/Users/chandrashekhar/CP/practice/practiceScalaAkka/src/main/scala/Test.scala"
  val INPUT_FILE_NAME = "TestInput.csv"
  val OUTPUT_FILE_PATH = "/Users/chandrashekhar/CP/practice/practiceScalaAkka/src/main/scala/Test.scala"
  val OUTPUT_FILE_NAME = "TestOutput.tsv"
  val AWS_ACCESS_KEY = "someaccesskey"
  val AWS_SECRET_KEY = "somesecretkey"

  S3DataTransform.transform(INPUT_FILE_PATH, OUTPUT_FILE_PATH)

}
