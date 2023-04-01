package Traits
import BotEvents._
import scala.util.Try

trait BotEventParser {
  def parse(rawData: String): Either[String, BotEvent]
}

object BotEventParser extends BotEventParser {
  override def parse(rawData: String): Either[String, BotEvent] = {
    rawData.split(",").toList match {
      case eventId :: timestamp :: warehouseId :: eventName :: rest if rest.nonEmpty =>
        eventName match {
          case "PositionChanged" => if(rest.size == 3){
            val botid = rest.head
            val pos_X = rest(1).toInt
            val pos_Y = rest(2).toInt
            if (pos_X >= 0 && pos_Y >= 0)
              Right(PositionChanged(eventId, timestamp, warehouseId, botId = botid, x = pos_X, y = pos_Y))
            else
              Left("PositionChanged event coordinates must be non-negative")
          }else
            Left("PositionChanged incorrect num of arguments")


          case "DestinationChanged" => if(rest.size == 3) {
            val botid = rest.head
            val pos_X = rest(1).toInt
            val pos_Y = rest(2).toInt
            if (pos_X >= 0 && pos_Y >= 0)
              Right(DestinationChanged(eventId, timestamp, warehouseId, botId = botid, x = pos_X, y = pos_Y))
            else
              Left("DestinationChanged event coordinates must be non-negative")
          } else
            Left("DestinationChanged incorrect num of arguments")

          case "ContainerPickedUp" => if(rest.size == 2) {
            Right(ContainerPickedUp(eventId, timestamp, warehouseId, botId = rest.head, containerId = rest(1)))
          } else
            Left("ContainerPickedUp incorrect num of arguments")

          case "ContainerDroppedOff" => if(rest.size == 2) {
            Right(ContainerDroppedOff(eventId, timestamp, warehouseId, botId = rest.head, containerId = rest(1)))
          } else
            Left("ContainerDroppedOff incorrect num of arguments")


          case "ChargingEnded" => if(rest.size == 2) {
            Right(ChargingEnded(eventId, timestamp, warehouseId, botId=rest.head, chargerId=rest(1)))
          } else
            Left("ChargingEnded incorrect num of arguments")

          case "ChargingStarted" => if(rest.size == 2) {
            Right(ChargingEnded(eventId, timestamp, warehouseId, botId = rest.head, chargerId = rest(1)))
          } else
            Left("ChargingStarted incorrect num of arguments")

          case "BatteryLevelChanged" => if(rest.size == 2) {
            val batterylevel = rest(1).toInt
            if (batterylevel <= 100 && batterylevel >= 0)
              Right(BatteryLevelChanged(eventId, timestamp, warehouseId, botId = rest.head, batteryLevel = batterylevel))
            else
              Left("Battery level must be in range between 0-100")
          } else
            Left("BatteryLevelChanged incorrect num of arguments")
          case _ =>
            Left(s"Unknown event name: $eventName")
        }
      case _ =>
        Left("Invalid input")
    }
  }
}