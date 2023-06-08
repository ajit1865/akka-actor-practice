package udemyAkkaActor

import akka.actor.{Actor, ActorSystem, Props}

class Person(name: String) extends Actor {
  override def receive: Receive = {
    case "hi" => println(s"Hi! my name is $name")
    case _ =>
  }
}
object Person{
  def props(name: String): Props = Props(new Person(name))
}
object PersonMain extends App {
  val actorSystem = ActorSystem("PersonActor")
  val actor = actorSystem.actorOf(Person.props("Bob"))
  actor ! "hi"
}
