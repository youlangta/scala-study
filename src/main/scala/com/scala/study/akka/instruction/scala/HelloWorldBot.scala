package com.scala.study.akka.instruction.scala

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object HelloWorld{
  final case class Greet(whom:String,replyTo : ActorRef[Greeted])
  final case class Greeted(whom:String,from : ActorRef[Greet])

  def apply() : Behavior[Greet] = Behaviors.receive {
    (context,message) =>
      context.log.info("hello {} !",message.whom)
      message.replyTo ! Greeted(message.whom,context.self)
      Behaviors.same
  }
}


object HelloWorldBot {
  def apply(max : Int) : Behavior[HelloWorld.Greeted] = {
    bot(0,max)
  }

  private def bot(greetingCounter: Int,max : Int) : Behavior[HelloWorld.Greeted] = {
    Behaviors.receive{
      (context,message) =>
        val n = greetingCounter + 1;
        context.log.info("Greeting {} for {}",n,message.whom)
        if(n == max){
          Behaviors.stopped
        }else{
          message.from ! HelloWorld.Greet(message.whom,context.self)
          bot(n,max)
        }
    }
  }
}

object HelloWorldMain{
  final case class SayHello(name:String)
  def apply():Behavior[SayHello] = {
    Behaviors.setup{
      context =>
        val greeter = context.spawn(HelloWorld(),"greeter")
        Behaviors.receiveMessage{
          message =>
            context.log.info("HelloWorldMain message is  {}",message)
            val replyTo = context.spawn(HelloWorldBot(max = 3),message.name)
            greeter ! HelloWorld.Greet(message.name,replyTo)
            Behaviors.same
        }
    }
  }
}


object ActorSystemMain {
  def main(args: Array[String]): Unit = {
    val system : ActorSystem[HelloWorldMain.SayHello] = ActorSystem(HelloWorldMain(),"hello")
    system ! HelloWorldMain.SayHello("world")
  }
}