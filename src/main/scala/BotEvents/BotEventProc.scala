package BotEvents

import BotStatus.BotStatusManager
import Traits.{BotEvent, BotEventProcessor}

class BotEventProc(botStatusManager: BotStatusManager) extends BotEventProcessor {
  def process(botEvent: BotEvent): Unit = {
    botEvent match {
      case PositionChanged(_,_, _, _, botId, x, y) =>
        botStatusManager.updatePosition(botId, x, y)

      case DestinationChanged(_,_,_, _, botId, x, y) =>
        botStatusManager.updateDestination(botId, x, y)

      case ContainerPickedUp(_,_, _, _, botId, containerId) =>
        botStatusManager.pickupContainer(botId, containerId)

      case ContainerDroppedOff(_,_,_, _, botId, containerId) =>
        botStatusManager.dropoffContainer(botId, containerId)

      case ChargingStarted(_,_,_, _, botId, chargerId) =>
        botStatusManager.startCharging(botId, chargerId)

      case ChargingEnded(_, _,_, _, botId, chargerId) =>
        botStatusManager.endCharging(botId, chargerId)

      case BatteryLevelChanged(_, _,_, _, botId, batteryLevel) =>
        botStatusManager.updateBatteryLevel(botId, batteryLevel)

      case _ =>
    }
  }
}
