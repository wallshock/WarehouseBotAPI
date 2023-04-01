package Traits

trait BotEventParser {
  def parse(rawData: String): Either[String, BotEvent]
}
