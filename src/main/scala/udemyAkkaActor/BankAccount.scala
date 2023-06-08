package udemyAkkaActor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object BankAccount {
  case class Deposit(amount: Int)
  case class Withdraw(amount: Int)
  case object Statement

  case class TransactionSuccess(message: String)
  case class TransactionFailure(failureMessage: String)
}

class BankAccount extends Actor{
import BankAccount._
  var netAmount = 0
  override def receive: Receive = {
    case Deposit(amount) =>
      if(amount < 0) sender() ! TransactionFailure("Less amount Deposit Error")
      else {
        netAmount += amount
        sender() ! TransactionSuccess(s"Amount $amount added successfully")
      }
    case Withdraw(amount) =>
      if(amount < 0 || amount > netAmount) sender() ! TransactionFailure ("Sorry can't withdraw amount")
      else {
        netAmount -= amount
        sender() ! TransactionSuccess(s"$amount is deducted from account")
      }
    case Statement => sender() ! s"Your balance is $netAmount"
  }

}
object BankAccountApplication extends App {
import BankAccount._
  import Person._
  val system = ActorSystem("BankAccountSystem")
  val bankAccount = system.actorOf(Props[BankAccount], "bankAccount")
  val person = system.actorOf(Props[Person], "person")

  object Person {
    case class LiveTheLife(account: ActorRef)
  }

  class Person extends Actor {

    override def receive: Receive = {
      case LiveTheLife(account) =>
        account ! Deposit(10000)
        account ! Withdraw(11000)
        account ! Withdraw(5500)
        account ! Statement
      case message: String => println(s"Received Message: $message")
    }
  }
  person ! LiveTheLife(bankAccount)
}