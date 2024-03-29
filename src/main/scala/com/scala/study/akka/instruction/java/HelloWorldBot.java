package com.scala.study.akka.instruction.java;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldBot extends AbstractBehavior<HelloWorld.Greeted> {
    private final int max;
    private int greetingCounter;

    public HelloWorldBot(ActorContext<HelloWorld.Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    public static Behavior<HelloWorld.Greeted> create(int max) {
        return Behaviors.setup(context -> new HelloWorldBot(context, max));
    }


    @Override
    public Receive<HelloWorld.Greeted> createReceive() {
        return newReceiveBuilder()
                .onMessage(
                        HelloWorld.Greeted.class,
                        this::onGreeted
                )
                .build();
    }

    private Behavior<HelloWorld.Greeted> onGreeted(HelloWorld.Greeted message) {
        ++greetingCounter;
        getContext().getLog().info("Greeting {} for {} ", greetingCounter, message.whom);
        if (greetingCounter == max) {
            return Behaviors.stopped();
        } else {
            message.from.tell(new HelloWorld.Greet(message.whom, getContext().getSelf()));
            return this;
        }
    }
}
