package BotEvents

import Traits.BotEvent

case class ChargingEnded(
  eventId: String,
  timestamp: String,
  warehouseId: String,
  eventName: String = "ChargingEnded",
  botId: String,
  chargerId:String) extends BotEvent
