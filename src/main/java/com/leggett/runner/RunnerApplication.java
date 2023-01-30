package com.leggett.runner;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.bonigarcia.wdm.WebDriverManager;
import jp.vmi.selenium.selenese.Runner;

@SpringBootApplication
public class RunnerApplication implements CommandLineRunner{

	private static Logger LOG = LoggerFactory
      .getLogger(RunnerApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(RunnerApplication.class, args);
        LOG.info("APPLICATION FINISHED");
	}

	@Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
 
        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
        
        //WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        Runner runner = new Runner();
        runner.setDriver(driver);
        runner.setSpeed(100);
        runner.run("src/main/resources/Leggett.side");
    }
}
