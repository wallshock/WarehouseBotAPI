package Traits
import BotEvents._

trait BotEventParser {
  def parse(rawData: String): Either[String, BotEvent]
}

