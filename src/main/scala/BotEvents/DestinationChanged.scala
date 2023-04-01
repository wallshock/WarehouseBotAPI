package BotEvents

import Traits.BotEvent

case class DestinationChanged(
  eventId: String,
  timestamp: String,
  warehouseId: String,
  eventName: String = "DestinationChanged",
  botId: String,
  x: Int,
  y: Int ) extends BotEvent
