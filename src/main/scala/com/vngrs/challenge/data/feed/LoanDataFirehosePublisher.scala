package com.vngrs.challenge.data.feed

import com.typesafe.scalalogging.LazyLogging
import com.vngrs.challenge.data.feed.util.ConfigProvider
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.core.internal.retry.SdkDefaultRetrySetting
import software.amazon.awssdk.core.retry.RetryPolicy
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy
import software.amazon.awssdk.core.retry.conditions.RetryCondition
import software.amazon.awssdk.services.firehose.model.{PutRecordBatchRequest, Record}
import software.amazon.awssdk.services.firehose.{FirehoseAsyncClient, FirehoseClient}

import scala.collection.JavaConverters._
import scala.io.Source

object LoanDataFirehosePublisher
  extends App
    with ConfigProvider
    with LazyLogging {

  val newLine = System.lineSeparator

  val client = FirehoseClient.builder()
    .overrideConfiguration(
      ClientOverrideConfiguration
        .builder()
        .retryPolicy(
          RetryPolicy.builder
            .backoffStrategy(BackoffStrategy.defaultStrategy)
            .throttlingBackoffStrategy(BackoffStrategy.defaultThrottlingStrategy)
            .numRetries(SdkDefaultRetrySetting.DEFAULT_MAX_RETRIES)
            .retryCondition(RetryCondition.defaultRetryCondition)
            .build
        ).build()
    )
    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
    .build()

  var count = 0
  var failed = 0
  val startTime = System.currentTimeMillis()
  val responses = Source.fromFile(loanDataPath, "UTF-8")
    .getLines
    .drop(1) // drop header line since firehouse does not guarantee orders of data
    .grouped(deliveryBatchSize)
    .map { lines =>
      val records = lines.map(toFireHoseRecord(_)).asJavaCollection
      val request = PutRecordBatchRequest.builder()
        .deliveryStreamName(deliveryStreamName)
        .records(records)
        .build()

      val response = client.putRecordBatch(request)
      count += lines.size
      failed += response.failedPutCount().toInt
    }.foreach { _ =>
      logger.info(s"count: $count, failed: $failed, elapsed-time: ${System.currentTimeMillis() - startTime} ms")
    }

  logger.info("loan data feed is finished")
  System.exit(0)

  def toFireHoseRecord(str: String) = Record.builder().data(SdkBytes.fromUtf8String(str + newLine)).build()


}
