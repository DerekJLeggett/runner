package com.leggett.runner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.bonigarcia.wdm.WebDriverManager;
import jp.vmi.selenium.selenese.Parser;
import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.result.Result;
import static jp.vmi.selenium.selenese.result.Unexecuted.*;

@SpringBootApplication
public class RunnerApplication implements CommandLineRunner {

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

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("se:recordVideo", true);
        chromeOptions.setCapability("se:screenResolution", "1920x1080");
        chromeOptions.setCapability("se:timeZone", "US/Arizona");
        WebDriver driver;
        Runner runner = new Runner();
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
            // driver = new ChromeDriver();
            driver.manage().window().maximize();
            runner.setDriver(driver);
            runner.setSpeed(100);
            runner.setHtmlResultDir("target\\test-output");
            // runner.run("src\\main\\resources\\Leggett.side");
            List<TestSuite> testSuiteList = new ArrayList<>();
            Selenese selenese = Parser.parse("src\\main\\resources\\Leggett.side", runner.getCommandFactory());
            testSuiteList.add((TestSuite) selenese);
            Result totalResult = UNEXECUTED;
            Result result = null;
            
            for (TestSuite testSuite : testSuiteList) {
                try {
                    result = runner.execute(testSuite);
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
                totalResult = totalResult.updateWithChildResult(testSuite, result);
            }
            driver.close();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

    }
}
