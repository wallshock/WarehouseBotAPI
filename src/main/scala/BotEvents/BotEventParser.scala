package BotEvents

import Traits.{BotEvent, BotEventParser}

object BotEventParser extends BotEventParser {
  def parse(rawData: String): Either[String, BotEvent] = {
    val warehouseid = "WR4A"
    rawData.split(",").toList match {
      case eventId :: timestamp :: `warehouseid` :: eventName :: rest if rest.nonEmpty =>
        parseEventDetails(eventId, timestamp, warehouseid, eventName, rest)
      case _ =>
        Left("Invalid input")
    }
  }

  def parseEventDetails(eventId: String, timestamp: String, warehouseid: String, eventName: String, rest: List[String]): Either[String, BotEvent] = {
    eventName match {
      case "PositionChanged" => parsePositionChanged(eventId, timestamp, warehouseid, rest)
      case "DestinationChanged" => parseDestinationChanged(eventId, timestamp, warehouseid, rest)
      case "ContainerPickedUp" => parseContainerPickedUp(eventId, timestamp, warehouseid, rest)
      case "ContainerDroppedOff" => parseContainerDroppedOff(eventId, timestamp, warehouseid, rest)
      case "ChargingEnded" => parseChargingEnded(eventId, timestamp, warehouseid, rest)
      case "ChargingStarted" => parseChargingStarted(eventId, timestamp, warehouseid, rest)
      case "BatteryLevelChanged" => parseBatteryLevelChanged(eventId, timestamp, warehouseid, rest)
      case _ => Left(s"Unknown event name: $eventName")
    }
  }

  def parsePositionChanged(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, BotEvent] = {
    if (rest.size == 3) {
      val botid = rest.head
      val pos_X = rest(1).toInt
      val pos_Y = rest(2).toInt
      if (pos_X >= 0 && pos_Y >= 0)
        Right(PositionChanged(eventId, timestamp, warehouseid, botId = botid, x = pos_X, y = pos_Y))
      else
        Left("PositionChanged event coordinates must be non-negative")
    } else
      Left("PositionChanged incorrect num of arguments")
  }

  def parseDestinationChanged(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, BotEvent] = {
    if (rest.size == 3) {
      val botid = rest.head
      val pos_X = rest(1).toInt
      val pos_Y = rest(2).toInt
      if (pos_X >= 0 && pos_Y >= 0)
        Right(DestinationChanged(eventId, timestamp, warehouseid, botId = botid, x = pos_X, y = pos_Y))
      else
        Left("DestinationChanged event coordinates must be non-negative")
    } else
      Left("DestinationChanged incorrect num of arguments")
  }

  def parseContainerPickedUp(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, BotEvent] = {
    if (rest.size == 2) {
      Right(ContainerPickedUp(eventId, timestamp, warehouseid, botId = rest.head, containerId = rest(1)))
    } else
      Left("ContainerPickedUp incorrect num of arguments")
  }

  def parseContainerDroppedOff(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, BotEvent] = {
    if (rest.size == 2) {
      Right(ContainerDroppedOff(eventId, timestamp, warehouseid, botId = rest.head, containerId = rest(1)))
    } else
      Left("ContainerDroppedOff incorrect num of arguments")
  }

  def parseChargingEnded(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, ChargingEnded] = {
    if (rest.size == 2)
      Right(ChargingEnded(eventId, timestamp, warehouseid, botId = rest.head, chargerId = rest(1)))
    else
      Left("ChargingEnded incorrect num of arguments")
  }

  def parseChargingStarted(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, ChargingStarted] = {
    if (rest.size == 2)
      Right(ChargingStarted(eventId, timestamp, warehouseid, botId = rest.head, chargerId = rest(1)))
    else
      Left("ChargingStarted incorrect num of arguments")
  }

  def parseBatteryLevelChanged(eventId: String, timestamp: String, warehouseid: String, rest: List[String]): Either[String, BatteryLevelChanged] = {
    if (rest.size == 2) {
      val batterylevel = rest(1).toInt
      if (batterylevel <= 100 && batterylevel >= 0)
        Right(BatteryLevelChanged(eventId, timestamp, warehouseid, botId = rest.head, batteryLevel = batterylevel))
      else
        Left("Battery level must be in range between 0-100")
    }else
      Left("BatterylevelChanged incorrect num of arguments")
  }
}