package com.example.myeventsourcing.helloworld.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrador on 10/03/2016.
 */

@RestController
public class HelloWorldController {

    /*@RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "HELLO WORLD!!!!!!!!";
    }*/

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
}
