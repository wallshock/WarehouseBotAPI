package BotStatus

class BotStatus(
                 var currentPos: (Int, Int),
                 var batteryLevel: Int,
                 var isCharging: Boolean,
                 var carriedContainer: Option[String],
                 var currentTarget: Option[(Int, Int)]
               )