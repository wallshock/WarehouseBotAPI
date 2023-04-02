package BotEvents

import BotStatus.BotStatusManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.{Duration, Instant, LocalDateTime}

class BotEventProcTest extends AnyWordSpec with MockFactory {

  "BotEventProc" must {
    "ignore an event older than 5 minutes and correctly process events" in {
      val arr: Array[String] = Array("bot1", "bot2")
      val botStatusManager = new BotStatusManager(arr)
      val botEventProc = new BotEventProc(botStatusManager)

      val oldTimestamp = Instant.now().minus(Duration.ofMinutes(6)).toString
      val newTimestamp = Instant.now().minus(Duration.ofMinutes(4)).toString
      val event = PositionChanged("avc", newTimestamp, "WR4A", botId = "bot1", x = 0, y = 0)
      val event1 = BatteryLevelChanged("avc", newTimestamp, "WR4A", botId = "bot1", batteryLevel = 100)
      val event2 = ChargingEnded("avc", newTimestamp, "WR4A", botId = "bot1", chargerId = "abc")
      val event3 = PositionChanged("avc", oldTimestamp, "WR4A", botId = "bot1", x = 1, y = 0)
      val event4 = DestinationChanged("avc", newTimestamp, "WR4A", botId = "bot1", x = 0, y = 0)
      botEventProc.process(event)
      botEventProc.process(event1)
      botEventProc.process(event2)
      botEventProc.process(event3)
      botEventProc.process(event4)

      assert(botStatusManager.getBotStatus("bot1").nonEmpty)
      assert(botStatusManager.getBotStatus("bot1").get.currentPos.contains((0, 0)))
      assert(botStatusManager.getBotStatus("bot1").get.batteryLevel.contains(100))
      assert(botStatusManager.getBotStatus("bot1").get.currentTarget.contains((0, 0)))
    }
  }

  "ignore a duplicate event" in {
    val arr: Array[String] = Array("bot1", "bot2")
    val botStatusManager = new BotStatusManager(arr)
    val botEventProc = new BotEventProc(botStatusManager)

    val timestamp = Instant.now().toString
    val event1 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    val event2 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 1, y = 1)

    botEventProc.process(event1)
    botEventProc.process(event2)

    assert(botStatusManager.getBotStatus("bot1").get.currentPos.contains((0, 0)))
    assert(botStatusManager.getLogs("bot1").contains(event1))
    assert(!botStatusManager.getLogs("bot1").contains(event2))
  }

  "update Timestamps correctly" in {
    val arr: Array[String] = Array("bot1", "bot2")
    val botStatusManager = new BotStatusManager(arr)
    val botEventProc = new BotEventProc(botStatusManager)

    val event1 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    val event2 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 1, y = 1)

    val event1 = PositionChanged("avc", timestamp, "WR4A", botId = "bot2", x = 0, y = 0)
    val event2 = PositionChanged("avc", timestamp, "WR4A", botId = "bot2", x = 1, y = 1)

    botEventProc.process(event1)
    botEventProc.process(event2)

    assert(botStatusManager.getBotStatus("bot1").get.currentPos.contains((0, 0)))
    assert(botStatusManager.getLogs("bot1").contains(event1))
    assert(!botStatusManager.getLogs("bot1").contains(event2))
  }


}
