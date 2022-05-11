package com.scala.study.akka.instruction.java;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldMain extends AbstractBehavior<HelloWorldMain.SayHello> {

    private final ActorRef<HelloWorld.Greet> greeter;

    public HelloWorldMain(ActorContext<SayHello> context) {
        super(context);
        greeter = context.spawn(HelloWorld.create(), "greeter");
    }

    public static Behavior<SayHello> create() {
        return Behaviors.setup(HelloWorldMain::new);
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder()
                .onMessage(
                        SayHello.class,
                        this::onStart
                )
                .build();
    }

    private Behavior<SayHello> onStart(SayHello command) {
        ActorRef<HelloWorld.Greeted> replyTo = getContext().spawn(HelloWorldBot.create(3), command.name);
        greeter.tell(new HelloWorld.Greet(command.name, replyTo));
        return this;
    }


    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        final ActorSystem<SayHello> system = ActorSystem.create(HelloWorldMain.create(), "hello");
        system.tell(new HelloWorldMain.SayHello("World"));

    }
}
