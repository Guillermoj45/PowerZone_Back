package com.actividad_10.powerzone_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PowerZoneBackApplication {

    public static void main(String[] args) {
        System.out.println("hola: " + args.length);
        for (String arg : args) {
            System.out.println("hola :" + arg);
        }
        System.out.println("hola" + System.getenv());
        SpringApplication.run(PowerZoneBackApplication.class, args);
    }

}
