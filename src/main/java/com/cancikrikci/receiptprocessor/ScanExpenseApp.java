package com.cancikrikci.receiptprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ScanExpenseApp {
	public static void main(String[] args)
	{
		SpringApplication.run(ScanExpenseApp.class, args);
	}

}
