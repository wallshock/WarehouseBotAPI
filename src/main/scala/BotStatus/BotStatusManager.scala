package BotStatus

import scala.collection.mutable.HashMap
import org.slf4j.LoggerFactory

class BotStatusManager(id: Array[String]) {
  private val logger = LoggerFactory.getLogger(classOf[BotStatusManager])
  private val botStatuses = HashMap[String, BotStatus]()
  for (botId <- id) {
    botStatuses += (botId -> new BotStatus((0, 0), 100, false, None, None))
  }

  def updateBatteryLevel(botId: String, batterylevel: Int): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.batteryLevel = batterylevel
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

  def endCharging(botId: String, chargerId: String): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.isCharging = false;
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

  def startCharging(botId: String, chargerId: String): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.isCharging = false;
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

  def dropoffContainer(botId: String, containerId: String): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.carriedContainer = None
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

    def pickupContainer(botId: String, containerid: String): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.carriedContainer = Some(containerid)
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

  def updateDestination(botId: String, x: Int, y: Int): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.currentTarget = Some((x, y))
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }
  def updatePosition(botId: String, x: Int, y: Int): Unit = {
    botStatuses.get(botId) match {
      case Some(botStatus) => botStatus.currentPos = (x,y)
      case None =>
        val errorMsg = s"Error: Bot with ID $botId not found."
        logger.error(errorMsg)
    }
  }

}
