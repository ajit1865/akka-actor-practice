package udemyAkkaActor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangingActorBehaviour extends App {

  object FussyKid {
    case object KidAccept
    case object KidReject
    val happy = "Happy"
    val sad = "Sad"
  }
  class FussyKid extends Actor {
    import FussyKid._
    import Mom._
    var state = "Happy"
    override def receive: Receive = {
      case Food(Vegetable) => state = sad
      case Food(Chocolate) => state = happy
      case Ask(_) =>
        if(state == happy) sender() ! KidAccept
        else
          sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String)
    val Vegetable = "Vegies"
    val Chocolate = "Chocos"
  }
  class Mom extends Actor {
    import FussyKid._
    import Mom._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(Vegetable)
        kidRef ! Ask("Do you want to play")
      case KidAccept => println("Kid want to play")
      case KidReject => println("Kid don't want to play")
    }
  }
  import Mom._
  val system = ActorSystem("ChangingBehaviour")
  val fussyKid = system.actorOf(Props[FussyKid], "Fussykid")
  val mom = system.actorOf(Props[Mom], "mom")
  mom ! MomStart(fussyKid)
}
