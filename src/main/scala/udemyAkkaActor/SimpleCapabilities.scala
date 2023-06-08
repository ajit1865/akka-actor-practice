package udemyAkkaActor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object SimpleCapabilities extends App {

  private class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi!" => sender() ! "Hello there!"
      case message: String => println(s"I have received a message: $message")
      case number: Int => println(s"I have received a Number: $number")
      case AnotherMessage(content) => println(s"I found: $content")
      case AnotherMessageActor(anotherMessage) =>
        self ! anotherMessage
      case SayHiTo(ref) => ref ! "Hi!"
      case WirelessPhoneMessage(content, ref) => ref forward (content + "s")
    }
  }

  val actorSystem = ActorSystem("ActorCapabilities")
  val actor = actorSystem.actorOf(Props[SimpleActor], "SimpleActor")
  actor ! "How are you"
  actor ! 54

  private case class AnotherMessage(name: String)

  actor ! AnotherMessage("Vikram Aditya")

  private case class AnotherMessageActor(myMessage: String)

  actor ! AnotherMessageActor("Hi! I am holding a beautiful Message...")

  val alice = actorSystem.actorOf(Props[SimpleActor], "alice")
  val bob = actorSystem.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)

  alice ! SayHiTo(bob)

  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("HI", bob)
}
