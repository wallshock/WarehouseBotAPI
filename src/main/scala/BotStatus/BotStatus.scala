package BotStatus

class BotStatus(
                 var currentPos: Option[(Int, Int)],
                 var batteryLevel: Option[Int],
                 var isCharging: Option[Boolean],
                 var carriedContainer: Option[String],
                 var currentTarget: Option[(Int, Int)]
               )