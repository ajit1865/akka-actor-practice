package com.knoldus

import akka.actor.{Actor, ActorSystem, Props}

class PreStart extends Actor{
  override def receive: Receive = {
    case message => println("Hi! "+ message)
  }

  override def preStart(): Unit = {
    println("preStart() is invoked...")
  }
}

object PreStartExample extends App {
  val system = ActorSystem("ActorLifeCycle")
  val actor = system.actorOf(Props[PreStart], "preStartMessge")
  actor ! "Ajit"
}
