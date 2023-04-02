import BotStatus.BotStatus
import Traits.{BotEventParser, BotEventProcessor, BotStatusReporter}

class BotController(eventParser: BotEventParser, eventProcessor: BotEventProcessor, statusReporter: BotStatusReporter) {

  def receiveRawEventData(rawData: String): Unit = {
    eventParser.parse(rawData) match {
      case Right(botEvent) =>
        eventProcessor.process(botEvent)
      case Left(errorMsg) =>
        println(s"Error parsing event data: $errorMsg")
    }
  }

  def getBotStatus(botId: String): Option[BotStatus] = {
    statusReporter.getBotStatus(botId)
  }

}