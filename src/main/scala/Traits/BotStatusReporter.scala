package Traits
import BotStatus.BotStatus
trait BotStatusReporter {
  def getBotStatus(botId: String): Option[BotStatus]
}


