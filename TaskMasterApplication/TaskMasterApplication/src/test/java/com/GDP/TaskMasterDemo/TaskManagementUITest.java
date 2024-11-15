package com.GDP.TaskMasterDemo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class TaskManagementUITest {

    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        // Set path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/tasks");
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
    
   
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    

    @Test
    public void testFilteringAndSearchingFunctionality() throws InterruptedException {
        // Enter a keyword in the search bar and verify the search functionality
        WebElement searchBar = driver.findElement(By.id("query"));
        searchBar.sendKeys("document");
        Thread.sleep(3000);
        WebElement searchButton = driver.findElement(By.id("searchbtn"));
        searchButton.click();
        WebElement search= driver.findElement(By.linkText("Collect briefing document"));
        System.out.println(search.getText());
        
        assertTrue("Collect briefing document", search.isDisplayed());
    }
}
