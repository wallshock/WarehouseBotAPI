package BotEvents

import Traits.BotEvent

case class ContainerPickedUp(
  eventId: String,
  timestamp: String,
  warehouseId: String,
  eventName: String = "ContainerPickedUp",
  botId: String,
  containerId:String) extends BotEvent
