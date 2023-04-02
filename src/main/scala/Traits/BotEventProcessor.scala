package Traits
import BotEvents._
import BotStatus.BotStatusManager
trait BotEventProcessor {
  def process(botEvent: BotEvent): Unit
}

object BotEventProcessor extends BotEventProcessor{
  override def process(botEvent: BotEvent): Unit = {
    botEvent match {
      case PositionChanged(_,_, _, _, botId, x, y) =>
        botStat[botId.updatePosition(botId, x, y)
      case DestinationChanged(_,_,_, _, botId, x, y) =>
        botStatus.updateDestination(botId, x, y)
      case ContainerPickedUp(_,_, _, _, botId, containerId) =>
        botStatus.pickupContainer(botId, containerId)
      case ContainerDroppedOff(_,_,_, _, botId, containerId) =>
        botStatus.dropoffContainer(botId, containerId)
      case ChargingStarted(_,_,_, _, botId, chargerId) =>
        botStatus.startCharging(botId, chargerId)
      case ChargingEnded(_, _,_, _, botId, chargerId) =>
        botStatus.endCharging(botId, chargerId)
      case BatteryLevelChanged(_, _,_, _, botId, batteryLevel) =>
        botStatus.updateBatteryLevel(botId, batteryLevel)
      case _ =>
    }
  }
}
