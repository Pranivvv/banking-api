package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OnlineBankingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBankingSystemApplication.class, args);
    }
}

