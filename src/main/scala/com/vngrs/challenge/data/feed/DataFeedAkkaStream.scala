package com.vngrs.challenge.data.feed

import java.nio.ByteBuffer
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.kinesisfirehose.scaladsl.{KinesisFirehoseFlow, KinesisFirehoseSink}
import akka.stream.scaladsl.{FileIO, Framing, Sink}
import akka.util.ByteString
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsyncClientBuilder
import com.amazonaws.services.kinesisfirehose.model.Record
import com.vngrs.challenge.data.feed.util.ConfigProvider

object DataFeedAkkaStream extends App with ConfigProvider {

  implicit val system = ActorSystem("loan-data-feed-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  implicit val amazonKinesisFirehoseAsync = AmazonKinesisFirehoseAsyncClientBuilder.defaultClient

  val newLine = System.lineSeparator

  system.registerOnTermination(amazonKinesisFirehoseAsync.shutdown())

  val startTime = System.currentTimeMillis()
  val res = FileIO.fromPath(Paths.get(loanDataPath), 30*1000)
    .via(Framing.delimiter(ByteString(newLine), Int.MaxValue, true))
    .map(_.utf8String)
    .drop(1) // drop csv header line
//    .map(line => { println(line.substring(0,line.indexOf(","))); line})
    .map(line => new Record().withData(ByteBuffer.wrap((line + newLine).getBytes())))
    .via(KinesisFirehoseFlow(deliveryStreamName))
    .runWith(Sink.ignore)

  res.onComplete(_ => {
    println(s"""Elapsed time in ms : ${System.currentTimeMillis() - startTime}""")
    system.terminate()
  })

}