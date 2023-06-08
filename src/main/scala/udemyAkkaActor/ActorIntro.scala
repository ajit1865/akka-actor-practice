package udemyAkkaActor

import akka.actor.ActorSystem

object ActorIntro extends App {

  private val actorSystem = ActorSystem("HelloIamActor")
  println(actorSystem.name)

}
