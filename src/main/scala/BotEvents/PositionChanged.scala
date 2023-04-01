package BotEvents

import Traits.BotEvent

case class PositionChanged(
                            eventId: String,
                            timestamp: String,
                            warehouseId: String,
                            eventName: String = "PositionChanged",
                            botId: String,
                            x: Int,
                            y: Int ) extends BotEvent