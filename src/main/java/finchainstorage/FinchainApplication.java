package finchainstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FinchainApplication {

	public static void main(String[] args){
		SpringApplication.run(FinchainApplication.class, args);
		System.out.println("Server is running...");

	}
}
