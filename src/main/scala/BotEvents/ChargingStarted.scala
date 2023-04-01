package BotEvents

import Traits.BotEvent

case class ChargingStarted(
                            eventId: String,
                            timestamp: String,
                            warehouseId: String,
                            eventName: String = "ChargingStarted",
                            botId: String,
                            chargerId:String ) extends BotEvent
