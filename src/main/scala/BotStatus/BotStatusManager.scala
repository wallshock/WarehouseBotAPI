package BotStatus
import Traits.LogBatchSender.BotLogBatch
import Traits.{BotEvent, BotStatusReporter, LogBatchSender}
import org.slf4j.LoggerFactory

import scala.collection.mutable

object BotStatusManager {
  def apply(id: Array[String]): BotStatusManager = {
    val botStatuses = mutable.HashMap[String, BotStatus]()
    for (botId <- id) {
      botStatuses += (botId -> new BotStatus(None,None,None,None,None))
    }
    val eventMap = mutable.HashMap[String,List[BotEvent]]()
    for (botId <- id) {
      eventMap += (botId -> List.empty[BotEvent])
    }
    new BotStatusManager(botStatuses,eventMap)
  }


}

class BotStatusManager(botStatuses: mutable.HashMap[String, BotStatus], eventMap: mutable.HashMap[String, List[BotEvent]]) extends BotStatusReporter{
  private val logger = LoggerFactory.getLogger(classOf[BotStatusManager])
  override def getBotStatus(botId: String): Option[BotStatus] = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        val hasMissingInfo = botStatus.currentPos.isEmpty ||
          botStatus.batteryLevel.isEmpty ||
          botStatus.isCharging.isEmpty ||
          botStatus.currentTarget.isEmpty
        if (hasMissingInfo) {
          None
        } else {
          Some(botStatus)
        }
      case None =>
        logger.error(s"Error with getBotStatus: Bot with ID $botId not found.")
        None
    }
  }

  def sendLogs(botId: String): Unit = {
    eventMap.get(botId) match {
      case Some(logList) =>
        //LogBatchSender.sendLogBatch(botId, BotLogBatch(logList))
        eventMap.update(botId, List.empty[BotEvent])
      case None =>
      //send some error message
    }
  }

  def putBotLog(botId:String,newEvent: BotEvent): Unit = {
    eventMap.get(botId) match {
      case Some(eventList) =>
        eventMap.put(botId, eventList :+ newEvent)
      case None =>
        eventMap.put(botId, List(newEvent))
    }
  }
  def updateBatteryLevel(botId: String, batterylevel: Int): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.batteryLevel = Some(batterylevel)
        true
      case None =>
        val errorMsg = s"Error with UpdateBatteryLevel: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }


  def endCharging(botId: String, chargerId: String): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.isCharging = Some(false)
        true
      case None =>
        val errorMsg = s"Error with endCharging: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }

  def startCharging(botId: String, chargerId: String): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.isCharging = Some(true)
        true
      case None =>
        val errorMsg = s"Error with startCharging: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }

  def dropoffContainer(botId: String, containerId: String): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.carriedContainer = None
        true
      case None =>
        val errorMsg = s"Error with dropoffContainer: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }

    def pickupContainer(botId: String, containerid: String): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.carriedContainer = Some(containerid)
        true
      case None =>
        val errorMsg = s"Error with pickupContainer: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }

  def updateDestination(botId: String, x: Int, y: Int): Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.currentTarget = Some((x, y))
        true
      case None =>
        val errorMsg = s"Error with updateDestination: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }
  def updatePosition(botId: String, x: Int, y: Int):Boolean = {
    botStatuses.get(botId) match {
      case Some(botStatus) =>
        botStatus.currentPos = Some((x,y))
        true
      case None =>
        val errorMsg = s"Error with updatePosition: Bot with ID $botId not found."
        logger.error(errorMsg)
        false
    }
  }
}
