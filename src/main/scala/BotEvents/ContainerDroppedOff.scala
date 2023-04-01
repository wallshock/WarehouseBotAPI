package BotEvents

import Traits.BotEvent

case class ContainerDroppedOff(
                                eventId: String,
                                timestamp: String,
                                warehouseId: String,
                                eventName: String = "ContainerDroppedOff",
                                botId: String,
                                containerId:String) extends BotEvent