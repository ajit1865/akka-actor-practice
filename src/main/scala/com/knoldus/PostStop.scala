package com.knoldus

import akka.actor.{Actor, ActorSystem, Props}

class PostStopExample extends Actor {
  override def receive: Receive = {
    case message => println("Hi! " + message)
  }

  override def postStop(): Unit = {
    println("postStop() method is called...")
  }
}

object PostStop extends App {
  val system = ActorSystem("Actor")
  val actor = system.actorOf(Props[PostStopExample], "postStop")
  actor ! "AkkaActor"
  system.stop(actor)
}
