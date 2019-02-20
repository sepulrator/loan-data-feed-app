package com.vngrs.challenge.data.feed.util

import com.typesafe.config.ConfigFactory

trait ConfigProvider {

  val config = ConfigFactory.load()
  val loanDataPath = config.getString("loan.data.file.path")
  val deliveryStreamName = config.getString("loan.data.firehose.delivery.stream")

}
