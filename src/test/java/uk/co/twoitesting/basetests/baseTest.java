/*This is where I'll implement a Set-up and Tear-down, etc
 */

package uk.co.twoitesting.basetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;


public class baseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUP() throws URISyntaxException, MalformedURLException {
        String browser = System.getProperty("browser");
        if(browser==null){
            throw new RuntimeException("browser must be set on command line");
        }

        //mvn test -Dbrowser=chrome
        switch (browser){
            case "chrome" -> driver = new ChromeDriver();
            case "firefox" -> driver = new FirefoxDriver();
            case "edgeLinux" -> {
                EdgeOptions options = new EdgeOptions();
                options.setPlatformName("Linux");
                driver = new RemoteWebDriver(new URI("http://172.19.31.211:4444/").toURL(), options);
            }
            default -> throw new RuntimeException("Browser not supported");
        }
        driver.manage().window().maximize();

        login();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
    }

    @AfterEach
    public void tearDown(){
        driver.quit();


    }

    private void login(){
        System.out.println("Starting test...");
        driver.get("https://www.edgewordstraining.co.uk/demo-site");
        driver.findElement(By.id("menu-item-46")).click();
        driver.findElement(By.cssSelector("#username")).sendKeys("chloe.urquhart@2itesting.com");
        driver.findElement(By.cssSelector("#password")).sendKeys("EdgewordsTraining123!!");
        driver.findElement(By.linkText("Dismiss")).click();
        driver.findElement(By.cssSelector("button[value='Log in']")).click();
        System.out.println("logged in successfully");

    }



}