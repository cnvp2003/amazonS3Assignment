import S3Main.{AWS_ACCESS_KEY, AWS_SECRET_KEY, BUCKET_NAME}
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, S3Object}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable
import scala.io.Source

object S3DataTransform {

  def transform(inFile:String, outFile:String)={
    val s3Client = getS3Client
    val s3Objects =s3Client.listObjectsV2(BUCKET_NAME)
    val s3ObjectsSummary = s3Objects.getObjectSummaries.toList

    val newValueMap = mutable.Map[String, Int]()
    def processFile(s3Object:S3Object, isCsv:Boolean):Unit= {
      Source.fromInputStream(s3Object.getObjectContent).getLines().map{ line =>
        val stringArray: Array[String] = if (isCsv) line.split(",") else line.split("\t")
        val key: String = stringArray(0)
        val value: Int = if (stringArray(1) == "") 0 else stringArray(1).toInt

        if (newValueMap.contains(key)) {
          newValueMap(key) = newValueMap(key) + value
        } else {
          newValueMap += (key -> value)
        }
      }
    }

    s3ObjectsSummary.map { s3ObjectSummary =>
      val s3Object: S3Object = s3Client.getObject(s3ObjectSummary.getBucketName, s3ObjectSummary.getKey)
      val isCsv: Boolean = s3Object.getKey.endsWith(".csv")
      processFile(s3Object, isCsv)
    }

    val oddValues= newValueMap.filter(_._2 != 0).toList
    val outputContent = oddValues.map(pair => s"${pair._1}, ${pair._2}").mkString("\n")

    val putObjectRequest1 = new PutObjectRequest(BUCKET_NAME, outFile, outputContent)
    s3Client.putObject(putObjectRequest1)
  }

  def getS3Client: AmazonS3Client = {
    val awsCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    new AmazonS3Client(awsCredentials)
  }

}

