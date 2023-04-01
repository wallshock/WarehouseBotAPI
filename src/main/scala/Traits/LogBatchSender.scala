package Traits

import Traits.LogBatchSender.BotLogBatch

// Use, someone else will implement it later
trait LogBatchSender {
  def sendLogBatch(botId: String, logBatch: BotLogBatch): Unit
}

object LogBatchSender {
  case class BotLogBatch(events: List[BotEvent])
}