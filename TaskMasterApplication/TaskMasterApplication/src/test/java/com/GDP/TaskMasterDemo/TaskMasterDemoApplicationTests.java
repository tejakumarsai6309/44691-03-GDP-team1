package com.GDP.TaskMasterDemo;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskMasterDemoApplicationTests {

	private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        // Set path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/tasks");
        
    }
    
   
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFilteringAndSearchingFunctionality() throws InterruptedException {
    	WebElement usernameField = driver.findElement(By.id("email")); // Replace with actual username field ID
        WebElement passwordField = driver.findElement(By.id("password")); // Replace with actual password field ID
        usernameField.sendKeys("manager@mail.com");
        passwordField.sendKeys("112233");

        // Submit login form
        passwordField.submit();

        // Locate and click the "Tasks List" tab
        WebElement taskListTab = driver.findElement(By.linkText("Tasks List"));
        taskListTab.click();
        Thread.sleep(3000);
    }

}
