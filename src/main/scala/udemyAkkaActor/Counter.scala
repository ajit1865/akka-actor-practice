package udemyAkkaActor

import akka.actor.{Actor, ActorSystem, Props}
import udemyAkkaActor.Counter.{Decrement, Increment, Print}

object Counter {
  case object Increment
  case object Decrement
  case object Print
}

class Counter extends Actor {
  import Counter._
  var count = 0
  override def receive: Receive = {
    case Increment => count += 1
    case Decrement => count -= 1
    case Print => println(s"Current value of Count = $count")
  }
}

object CounterMain extends App {
  val system = ActorSystem("CounterActor")
  val actor = system.actorOf(Props[Counter], "Counter")
  (1 to 5).foreach(_ => actor ! Increment)
  (1 to 2).foreach(_ => actor ! Decrement)
  actor ! Print
}
