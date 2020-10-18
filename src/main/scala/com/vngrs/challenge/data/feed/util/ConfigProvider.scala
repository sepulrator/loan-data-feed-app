package com.vngrs.challenge.data.feed.util

import com.typesafe.config.ConfigFactory

trait ConfigProvider {

  val config = ConfigProvider.config
  val loanDataPath = config.getString("loan.data.file.path")
  val deliveryStreamName = config.getString("loan.data.firehose.delivery.stream")
  val deliveryBatchSize = config.getInt("loan.data.max.batch.size")

}

object ConfigProvider {
  val config = ConfigFactory.load()
}
