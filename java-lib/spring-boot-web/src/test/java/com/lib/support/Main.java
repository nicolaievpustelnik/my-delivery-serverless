package com.lib.support;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

  @Value("#{'${app.api.crud.suffix-path:}' + '/{id}'}")
  String value;

  public static void main(String[] args){
    SpringApplication.run(Main.class, args);
  }

  @PostConstruct
  void post() {
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + value);
  }
}
