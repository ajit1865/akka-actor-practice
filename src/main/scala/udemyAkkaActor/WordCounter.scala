package udemyAkkaActor

import akka.actor.{Actor, ActorSystem, Props}

class CountingWordsExample extends Actor {
  private var totalWords = 0

  override def receive: PartialFunction[Any, Unit] = {
    case message: String => totalWords += message.split(" ").length
      println(s"I have received a message $message and total words are $totalWords")
    case msg => println(s"I don't understand $msg")
  }
}

object WordCounter extends App {
  val actorSystem = ActorSystem("WordCounting")
  val actor = actorSystem.actorOf(Props[CountingWordsExample], "countingwordsExample")
  actor ! "Hi! my name is Karan and i am 24 years old"
}
