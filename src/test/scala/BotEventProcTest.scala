package BotEvents

import BotStatus.BotStatusManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import Traits.{BotEvent, BotEventProcessor}

import java.time.{Duration, Instant, LocalDateTime}

class BotEventProcSpec extends AnyFlatSpec with MockFactory {

  "BotEventProc" should "ignore an event older than 5 minutes" in {
    val arr:Array[String] = Array("avc","avg")
    val botStatusManager = BotStatusManager(arr)
    val botEventProc = new BotEventProc(botStatusManager)

    val oldTimestamp = Instant.now().minus(Duration.ofMinutes(6)).toString
    val newTimestamp = Instant.now().minus(Duration.ofMinutes(4)).toString
    val event = PositionChanged("avc", oldTimestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    val event2 = PositionChanged("avc", newTimestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    botEventProc.process(event)
    botEventProc.process(event2)

    (botStatusManager.updatePosition _).expects("bot1", 0, 0).once()
    (botStatusManager.putBotLog _).expects(*, *).once()
  }

  it should "ignore a duplicate event" in {
    val arr: Array[String] = Array("avc", "avg")
    val botStatusManager = BotStatusManager(arr)
    val botEventProc = new BotEventProc(botStatusManager)

    val timestamp = Instant.now().toString
    val event1 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    val event2 = PositionChanged("avc", timestamp, "WR4A", botId = "bot1", x = 1, y = 1)

    botEventProc.process(event1)
    botEventProc.process(event2)

    (botStatusManager.updatePosition _).expects("bot1", 0, 0).once()
    (botStatusManager.putBotLog _).expects(*, *).once()
  }

  it should "update the bot position if the timestamp is newer" in {
    val arr: Array[String] = Array("avc", "avg")
    val botStatusManager = BotStatusManager(arr)
    val botEventProc = new BotEventProc(botStatusManager)

    val oldTimestamp = Instant.now().minus(Duration.ofMinutes(1)).toString
    val newTimestamp = Instant.now().toString

    val event1 = PositionChanged("avc", oldTimestamp, "WR4A", botId = "bot1", x = 0, y = 0)
    val event2 = PositionChanged("avc1", newTimestamp, "WR4A", botId = "bot1", x = 2, y = 2)

    (botStatusManager.updatePosition _).expects("bot1", 1, 1).returning(true)
    (botStatusManager.putBotLog _).expects("bot1", event1).once()
    (botStatusManager.updatePosition _).expects("bot1", 2, 2).returning(true)
    (botStatusManager.putBotLog _).expects("bot1", event2).once()

    botEventProc.process(event1)
    botEventProc.process(event2)
  }
}