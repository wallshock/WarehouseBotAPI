package Traits

trait BotStatusReporter {
  def getBotStatus(botId: String): Option[BotStatus]
}
