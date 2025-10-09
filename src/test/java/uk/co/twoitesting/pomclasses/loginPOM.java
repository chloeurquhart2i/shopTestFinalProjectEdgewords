package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.By;
import uk.co.twoitesting.basetests.baseTest;

public class loginPOM extends baseTest {
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
