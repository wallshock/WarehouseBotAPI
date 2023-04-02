import BotEvents._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
class BotEventParserTest extends AnyWordSpec with Matchers {

  "BotEventParser" must {

    "parse a PositionChanged event" in {
      val input = "abc,2023-03-31T16:08:33.040Z,WR4A,PositionChanged,b1,0,0"
      val expectedOutput = PositionChanged("abc", "2023-03-31T16:08:33.040Z", "WR4A", botId = "b1", x = 0, y = 0)
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a DestinationChanged event" in {
      val input = "awd,2023-03-31T16:08:33.041Z,WR4A,DestinationChanged,b1,1,2"
      val expectedOutput = DestinationChanged("awd", "2023-03-31T16:08:33.041Z", "WR4A", botId = "b1", x = 1, y = 2)
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a ContainerPickedUp event" in {
      val input = "xyz,2023-03-31T16:08:33.042Z,WR4A,ContainerPickedUp,b1,c1"
      val expectedOutput = ContainerPickedUp("xyz", "2023-03-31T16:08:33.042Z", "WR4A", botId = "b1", containerId = "c1")
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a ContainerDroppedOff event" in {
      val input = "4,2023-03-31T16:08:33.043Z,WR4A,ContainerDroppedOff,b1,c1"
      val expectedOutput = ContainerDroppedOff("4", "2023-03-31T16:08:33.043Z", "WR4A", botId = "b1", containerId = "c1")
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a ChargingEnded event" in {
      val input = "5,2023-03-31T16:08:33.044Z,WR4A,ChargingEnded,b1,ch1"
      val expectedOutput = ChargingEnded("5", "2023-03-31T16:08:33.044Z", "WR4A", botId = "b1", chargerId = "ch1")
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a ChargingStarted event" in {
      val input = "6,2023-03-31T16:08:33.045Z,WR4A,ChargingStarted,b1,ch1"
      val expectedOutput = ChargingStarted("6", "2023-03-31T16:08:33.045Z", "WR4A", botId = "b1", chargerId = "ch1")
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }

    "parse a BatteryLevelChanged event" in {
      val input = "7,2023-03-31T16:08:33.046Z,WR4A,BatteryLevelChanged,b1,50"
      val expectedOutput = BatteryLevelChanged("7", "2023-03-31T16:08:33.046Z", "WR4A", botId = "b1", batteryLevel = 50)
      BotEventParser.parse(input) mustBe Right(expectedOutput)
    }
  }

  "BotEventParser" must {
    "handle invalid input" in {
      assert(BotEventParser.parse("").isLeft)
      assert(BotEventParser.parse("WR4A").isLeft)
      assert(BotEventParser.parse("WR4A, ,").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,3").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,3,4,5").isLeft)
    }


    "handle invalid event names" in {
      assert(BotEventParser.parse("WR4A,1,2,UnknownEvent,3,4,5").isLeft)
    }

    "handle PositionChanged event with negative coordinates" in {
      assert(BotEventParser.parse("WR4A,1,2,PositionChanged,bot1,-1,2").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,PositionChanged,bot1,2,-3").isLeft)
    }

    "handle invalid number of arguments for PositionChanged event" in {
      assert(BotEventParser.parse("WR4A,1,2,PositionChanged,bot1,1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,PositionChanged,bot1,1,2,3").isLeft)
    }

    "handle DestinationChanged event with negative coordinates" in {
      assert(BotEventParser.parse("WR4A,1,2,DestinationChanged,bot1,-1,2").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,DestinationChanged,bot1,2,-3").isLeft)
    }

    "handle invalid number of arguments for DestinationChanged event" in {
      assert(BotEventParser.parse("WR4A,1,2,DestinationChanged,bot1,1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,DestinationChanged,bot1,1,2,3").isLeft)
    }

    "handle invalid number of arguments for ContainerPickedUp event" in {
      assert(BotEventParser.parse("WR4A,1,2,ContainerPickedUp,bot1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,ContainerPickedUp,bot1,container1,extra").isLeft)
    }

    "handle invalid number of arguments for ContainerDroppedOff event" in {
      assert(BotEventParser.parse("WR4A,1,2,ContainerDroppedOff,bot1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,ContainerDroppedOff,bot1,container1,extra").isLeft)
    }

    "handle invalid number of arguments for ChargingEnded event" in {
      assert(BotEventParser.parse("WR4A,1,2,ChargingEnded,bot1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,ChargingEnded,bot1,charger1,extra").isLeft)
    }

    "handle invalid number of arguments for ChargingStarted event" in {
      assert(BotEventParser.parse("WR4A,1,2,ChargingStarted,bot1").isLeft)
      assert(BotEventParser.parse("WR4A,1,2,ChargingStarted,bot1,charger1,extra").isLeft)
    }
    "handle other warehouses" in {
      assert(BotEventParser.parse("5,2023-03-31T16:08:33.044Z,WRA,ChargingEnded,b1,ch1").isLeft)
      assert(BotEventParser.parse("6,2023-03-31T16:08:33.045Z,WR14A,ChargingStarted,b1,ch1").isLeft)
    }
  }
}
