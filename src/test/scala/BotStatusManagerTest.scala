package BotEvents
import BotStatus.BotStatusManager
import Traits.BotEvent
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BotStatusManagerTest extends AnyWordSpec with Matchers {
  "A BotStatusManager" should {
    val botIds = Array("bot1", "bot2", "bot3")
    val botStatusManager = new BotStatusManager(botIds)

    "correctly update battery level" in {
      botStatusManager.updateBatteryLevel("bot1", 80) mustBe true
      botStatusManager.getBotStatus("bot1").get.batteryLevel mustBe Some(80)
    }

    "correctly start charging" in {
      botStatusManager.startCharging("bot1", "charger1") mustBe true
      botStatusManager.getBotStatus("bot1").get.isCharging mustBe Some(true)
    }

    "correctly end charging" in {
      botStatusManager.endCharging("bot1", "charger1") mustBe true
      botStatusManager.getBotStatus("bot1").get.isCharging mustBe Some(false)
    }

    "correctly pickup container" in {
      botStatusManager.pickupContainer("bot1", "container1") mustBe true
      botStatusManager.getBotStatus("bot1").get.carriedContainer mustBe Some("container1")
    }

    "correctly dropoff container" in {
      botStatusManager.dropoffContainer("bot1", "container1") mustBe true
      botStatusManager.getBotStatus("bot1").get.carriedContainer mustBe None
    }

    "correctly update destination" in {
      botStatusManager.updateDestination("bot1", 10, 20) mustBe true
      botStatusManager.getBotStatus("bot1").get.currentTarget mustBe Some((10, 20))
    }

    "correctly update position" in {
      botStatusManager.updatePosition("bot1", 5, 5) mustBe true
      botStatusManager.getBotStatus("bot1").get.currentPos mustBe Some((5, 5))
    }

    "correctly put bot log" in {
      val botEvent = PositionChanged("abc", "2023-03-31T16:08:33.040Z", "WR4A", botId = "b1", x = 0, y = 0)
      botStatusManager.putBotLog("bot1", botEvent)
      botStatusManager.getLogs("bot1") mustBe List(botEvent)
    }

    "correctly send logs" in {
      botStatusManager.sendLogs("bot1")
      botStatusManager.getLogs("bot1") mustBe List.empty[BotEvent]
    }
  }
}