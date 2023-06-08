package com.knoldus

import akka.actor.{Actor, ActorSystem, Props}

case class WhoToGreet(who: String)

class Greeter extends Actor {
  override def receive: Receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}

object HelloAkka extends App {
  private val system = ActorSystem("Hello-Akka")

  private val greeter = system.actorOf(Props[Greeter], "Greeter")
  greeter ! WhoToGreet("NashTech")

}
