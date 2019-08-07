package com.homeaway.saas.frequencyManager;

import com.homeaway.saas.frequencyManager.service.StartupRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class FrequencyManagerApplication {

	private static final Logger LOG =
			LoggerFactory.getLogger(FrequencyManagerApplication.class);

	public static void main(String[] args) {
		LOG.info("Starting the application");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(FrequencyManagerApplication.class, args);
		try {
			applicationContext.getBean(StartupRunner.class).startListeningForNewFiles();
		} catch (IOException e) {
			LOG.error("IOException while listening to the directory");
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOG.error("InterruptedException while listening to the directory");
			e.printStackTrace();
		}

	}

}
