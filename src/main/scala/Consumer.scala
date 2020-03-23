import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import test.Person
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.sql.{DataFrame, SparkSession, functions => F}
import scalapb.spark.ProtoSQL



object Consumer {


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().
      setMaster("local").
      setAppName("ScalaSparkConsumer")

    val streamingContext = new StreamingContext(conf, Seconds(30))

    val kafkaParams = Map[String, Object](
      "group.id" -> "test",
      "bootstrap.servers" -> "yourkafkainstance:9093",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[ByteArrayDeserializer],
      //will consume from the earliest point of kafka retention period
      "auto.offset.reset" -> "earliest"

    )

    val topics = Array("testTopic")

    val consumerStrategy = ConsumerStrategies.Subscribe[String, Array[Byte]](topics, kafkaParams)
    val stream = KafkaUtils.createDirectStream[String, Array[Byte]](
      streamingContext, LocationStrategies.PreferConsistent, consumerStrategy)

    //Map the stream to get the Array of bytes from the field value
    val ds1: DStream[Person] = stream.map{
      (cr: ConsumerRecord[String,Array[Byte]]) => Person.parseFrom(cr.value())
    }

    // Example of printing a ds1 = [info] Person(ab,18,111 Noname Street)
    ds1.print()

    // converting each RDD into a Dataframe in the period of Streaming context (30 sec)
    ds1.foreachRDD{
      rdd=>
        val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
        //Important to import scalapb.spark.Implicits._ instead of Implicits from Spark
        import scalapb.spark.Implicits._
        if (! rdd.isEmpty()){
          val protoDF: DataFrame = ProtoSQL.protoToDataFrame(spark, rdd)
          protoDF.printSchema()
          protoDF.show()
        }
        else
          println("*********************** RDD EMPTY ************* ")
    }


    streamingContext.start()
    streamingContext.awaitTermination()
  }
}