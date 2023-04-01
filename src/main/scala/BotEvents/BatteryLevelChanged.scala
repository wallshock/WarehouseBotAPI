package BotEvents

import Traits.BotEvent

case class BatteryLevelChanged(
  eventId: String,
  timestamp: String,
  warehouseId: String,
  eventName: String = "BatteryLevelChanged",
  botId: String,
  batteryLevel: Int) extends BotEvent

