package BotEvents

import BotStatus.{BotStatus, BotStatusManager}
import Traits.{BotEvent, BotEventProcessor}

import java.time.format.DateTimeFormatter
import java.time.{Duration, Instant, LocalDateTime, ZoneId}
import scala.collection.mutable

class BotEventProc(botStatusManager: BotStatusManager) extends BotEventProcessor {

  private val botTimestamps = mutable.HashMap[String, BotEventTimestamps]()

  var timestampPosition:String = _
  var  timestampDestination:String = _
  var timestampContainer:String = _
  var timestampCharging:String = _
  var timestampBatteryLevel:String = _

  def process(botEvent: BotEvent): Unit = {

    if (isOlderThan5Minutes(botEvent.timestamp)) {
      //we just ignore it
    } else {
      botEvent match {
        case PositionChanged(_, timestamp, _, _, botId, x, y) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampPosition)) { //if we receive the same event twice then it wont be processed because it wont have newer timestamp
            if (botStatusManager.updatePosition(botId, x, y)) {
              botStatusManager.putBotLog(botId, botEvent)
              updateTimestampPos(botId,timestampPosition)
            }
          }
        //if something went wrong, we can find it in the logs in the botStatusManager.
        case DestinationChanged(_, timestamp, _, _, botId, x, y) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampDestination)) {
            if (botStatusManager.updateDestination(botId, x, y))
              botStatusManager.putBotLog(botId, botEvent)
            updateTimestampDest(botId,timestampDestination)
          }
        case ContainerPickedUp(_, timestamp, _, _, botId, containerId) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampContainer)) {
            if (botStatusManager.pickupContainer(botId, containerId))
              botStatusManager.putBotLog(botId, botEvent)
            updateTimestampCont(botId,timestampContainer)
          }
        case ContainerDroppedOff(_, timestamp, _, _, botId, containerId) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampContainer)) {
            if (botStatusManager.dropoffContainer(botId, containerId))
              botStatusManager.putBotLog(botId, botEvent)
            updateTimestampCont(botId,timestampContainer)
          }
        case ChargingStarted(_, timestamp, _, _, botId, chargerId) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampCharging)) {
            if (botStatusManager.startCharging(botId, chargerId)) {
              botStatusManager.putBotLog(botId, botEvent)
              updateTimestampCharge(botId,timestampCharging)
              botStatusManager.sendLogs(botId)
            }
          }

        case ChargingEnded(_, timestamp, _, _, botId, chargerId) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampCharging)) {
            if (botStatusManager.endCharging(botId, chargerId))
              botStatusManager.putBotLog(botId, botEvent)
            updateTimestampCharge(botId,timestampCharging)
          }

        case BatteryLevelChanged(_, timestamp, _, _, botId, batteryLevel) =>
          addBotTimestamp(botId)
          setTimestamps(botId)
          if (isNewerTimestamp(timestamp, timestampBatteryLevel)) {
            if (botStatusManager.updateBatteryLevel(botId, batteryLevel))
              botStatusManager.putBotLog(botId, botEvent)
            updateTimestampBattery(botId,timestampBatteryLevel)
          }

        case _ =>
      }
    }

    def isNewerTimestamp(newTimestamp: String, oldTimestamp: String): Boolean = {
      if (oldTimestamp == null) true
      else {
        val formatter = DateTimeFormatter.ISO_INSTANT
        val newDateTime = Instant.from(formatter.parse(newTimestamp)).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val oldDateTime = Instant.from(formatter.parse(oldTimestamp)).atZone(ZoneId.systemDefault()).toLocalDateTime()
        newDateTime.isAfter(oldDateTime)
      }
    }

    def isOlderThan5Minutes(timestamp: String): Boolean = {
      val formatter = DateTimeFormatter.ISO_INSTANT
      val parsedTimestamp = Instant.from(formatter.parse(timestamp)).atZone(ZoneId.systemDefault()).toLocalDateTime()
      val now = LocalDateTime.now()
      now.isAfter(parsedTimestamp.plus(Duration.ofMinutes(5)))
    }
  }

  def setTimestamps(botId:String):Unit={
    botTimestamps.get(botId) match {
      case Some(botTimestamp) =>
        timestampPosition=botTimestamp.timestampPosition
        timestampDestination=botTimestamp.timestampDestination
        timestampContainer=botTimestamp.timestampContainer
        timestampCharging=botTimestamp.timestampCharging
        timestampBatteryLevel=botTimestamp.timestampBatteryLevel
    }
  }
  def addBotTimestamp(botId: String): Unit =
    if (!botTimestamps.contains(botId)) {
      val botEventTimestamps = new BotEventTimestamps()
      botTimestamps.put(botId,botEventTimestamps)
    }

  def updateTimestampPos(botId: String, timestampPosition: String):Unit= {
    botTimestamps.get(botId) match {
      case Some(botTimestamp) =>
        botTimestamp.timestampPosition = timestampPosition
    }
  }

  def updateTimestampDest(botId: String, timestampDest: String):Unit= {
    botTimestamps.get(botId) match {
      case Some(botTimestamp) =>
        botTimestamp.timestampDestination = timestampDest
    }
  }

  def updateTimestampCont(botId: String, timestampCont: String):Unit= {
    botTimestamps.get(botId) match {
      case Some(botTimestamp) =>
        botTimestamp.timestampContainer = timestampCont
    }
  }

    def updateTimestampCharge(botId: String, timestampCharg: String): Unit = {
      botTimestamps.get(botId) match {
        case Some(botTimestamp) =>
          botTimestamp.timestampCharging = timestampCharg
      }
    }

      def updateTimestampBattery(botId: String, timestampBat: String): Unit = {
        botTimestamps.get(botId) match {
          case Some(botTimestamp) =>
            botTimestamp.timestampBatteryLevel = timestampBat
        }
      }
}
