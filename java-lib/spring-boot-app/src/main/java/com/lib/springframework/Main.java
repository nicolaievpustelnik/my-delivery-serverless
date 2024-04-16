package com.lib.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "${app.base-package}")
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
