package BotEvents

import BotStatus.BotStatusManager
import Traits.{BotEvent, BotEventProcessor}

import java.time.{Instant, LocalDateTime,Duration}

class BotEventProc(botStatusManager: BotStatusManager) extends BotEventProcessor {

  var timestampBatteryLevel: String = _
  var timestampCharging: String = _
  var timestampPosition: String = _
  var timestampContainer: String = _
  var timestampDestination: String = _

  def process(botEvent: BotEvent): Unit = {
    if (isOlderThan5Minutes(botEvent.timestamp)) {
      //we just ignore it
    } else {
      botEvent match {
        case PositionChanged(_, timestamp, _, _, botId, x, y) =>
          if (isNewerTimestamp(timestamp, timestampPosition)) { //if we receive the same event twice then it wont be processed because it wont have newer timestamp
            if (botStatusManager.updatePosition(botId, x, y)) {
              botStatusManager.putBotLog(botId, botEvent)
              timestampPosition = timestamp
            }
          }
          //if something went wrong, we can find it in the logs in the botStatusManager.
        case DestinationChanged(_, timestamp, _, _, botId, x, y) =>
          if (isNewerTimestamp(timestamp, timestampDestination)) {
            if (botStatusManager.updateDestination(botId, x, y))
              botStatusManager.putBotLog(botId, botEvent)
            timestampDestination = timestamp
          }
        case ContainerPickedUp(_, timestamp, _, _, botId, containerId) =>
          if (isNewerTimestamp(timestamp, timestampContainer)) {
            if (botStatusManager.pickupContainer(botId, containerId))
              botStatusManager.putBotLog(botId, botEvent)
            timestampContainer = timestamp
          }
        case ContainerDroppedOff(_, timestamp, _, _, botId, containerId) =>
          if (isNewerTimestamp(timestamp, timestampContainer)) {
            if (botStatusManager.dropoffContainer(botId, containerId))
              botStatusManager.putBotLog(botId, botEvent)
            timestampContainer = timestamp
          }
        case ChargingStarted(_, timestamp, _, _, botId, chargerId) =>
          if (isNewerTimestamp(timestamp, timestampCharging)) {
            if (botStatusManager.startCharging(botId, chargerId)) {
              botStatusManager.putBotLog(botId, botEvent)
              timestampCharging = timestamp
              botStatusManager.sendLogs(botId)
            }
          }

        case ChargingEnded(_, timestamp, _, _, botId, chargerId) =>
          if (isNewerTimestamp(timestamp, timestampCharging)) {
            if (botStatusManager.endCharging(botId, chargerId))
              botStatusManager.putBotLog(botId, botEvent)
            timestampCharging = timestamp
          }

        case BatteryLevelChanged(_, timestamp, _, _, botId, batteryLevel) =>
          if (isNewerTimestamp(timestamp, timestampBatteryLevel)) {
            if (botStatusManager.updateBatteryLevel(botId, batteryLevel))
              botStatusManager.putBotLog(botId, botEvent)
            timestampBatteryLevel = timestamp
          }

        case _ =>
      }
    }

    def isNewerTimestamp(newTimestamp: String, oldTimestamp: String): Boolean = {
      if (oldTimestamp==null) true
      else {
        val newDateTime = LocalDateTime.parse(newTimestamp)
        val oldDateTime = LocalDateTime.parse(oldTimestamp)
        newDateTime.isAfter(oldDateTime)
      }
    }

    def isOlderThan5Minutes(timestamp: String): Boolean = {
      val parsedTimestamp = Instant.parse(timestamp)
      val now = Instant.now()
      now.isAfter(parsedTimestamp.plus(Duration.ofMinutes(5)))
    }
  }
}
