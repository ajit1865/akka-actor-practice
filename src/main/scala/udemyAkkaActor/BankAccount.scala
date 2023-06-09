package udemyAkkaActor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.slf4j.LoggerFactory
import BankAccount._
import scala.language.postfixOps

object BankAccount {
  case class Deposit(amount: Int)
  case class Withdraw(amount: Int)
  case object Statement
}
case class BankAccTag(x: String)

// Sample Child Actor Class
class BankAccount extends Actor{
import BankAccount._
  private var netAmount = 0
  private type StatementTagger = String => String
  private var transactionSummary: StatementTagger = _

  override def preStart() = {
    transactionSummary = (x: String) => x + "@tagged" // this takes a few seconds to complete
    Thread.sleep(2000) // simulate time taken to initialize Tagger
  }
   def receive: Receive = {
     case BankAccTag(text) => sender ! transactionSummary(text)
     case Deposit(amount) =>
      if(amount < 0) sender ! transactionSummary("Less amount Deposit Error")
      else {
        netAmount += amount
        sender ! transactionSummary(s"Amount $amount added successfully")
      }
     case Withdraw(amount) =>
      if(amount < 0 || amount > netAmount) sender ! transactionSummary ("Sorry can't withdraw amount")
      else {
        netAmount -= amount
        sender ! transactionSummary(s"$amount is deducted from account")
      }
     case Statement => sender ! s"Your balance is $netAmount"
  }
}

// Sample Parent Actor Class
class ParentActor(var parent: ActorRef) extends Actor {
  private val log = LoggerFactory.getLogger("com.service.BankAccountSystem")
  log.info("======== START Logging ParentActor =========")

  parent = context.actorOf(Props[BankAccount], name = "bankAccount")
  def receive: Receive = {
    case message: String => println(s"Received Message: $message")
  }
  parent ! BankAccTag("This is sample akka bank account app.")
  parent ! Deposit(10000)
  parent ! Withdraw(5000)
  parent ! Statement

  log.info("========= END Logging ParentActor ==========")
}

// Main App.
object BankAccountApplication extends App {
  implicit val system = ActorSystem("BankAccountSystem")
  private val bankAccount = system.actorOf(Props[BankAccount], "bankAccount")
  system.actorOf(Props(classOf[ParentActor], bankAccount), "ParentActor")
}

/*
  OUTPUT:
        [2023-06-09 13:45:48,356] [INFO] [com.service.BankAccountSystem] [] [BankAccountSystem-akka.actor.default-dispatcher-6] - ======== START Logging ParentActor ========= MDC: {}
        [2023-06-09 13:45:48,360] [INFO] [com.service.BankAccountSystem] [] [BankAccountSystem-akka.actor.default-dispatcher-6] - ========= END Logging ParentActor ========== MDC: {}
        Received Message: This is sample akka bank account app.@tagged
        Received Message: Amount 10000 added successfully@tagged
        Received Message: 5000 is deducted from account@tagged
        Received Message: Your balance is 5000
 */

