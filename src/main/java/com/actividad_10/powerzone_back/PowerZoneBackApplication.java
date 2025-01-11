package com.actividad_10.powerzone_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PowerZoneBackApplication {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("hola :" + arg);
        }
        SpringApplication.run(PowerZoneBackApplication.class, args);
    }

}
