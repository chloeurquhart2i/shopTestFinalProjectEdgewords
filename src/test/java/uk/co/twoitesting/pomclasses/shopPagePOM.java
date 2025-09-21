package uk.co.twoitesting.pomclasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import uk.co.twoitesting.pomtests.pomTests;

public class shopPagePOM extends pomTests {
    public shopPagePOM(WebDriver driver){ this.driver = driver; }

    public shopPagePOM addFirstVisibleProductToCart() {
        driver.findElement(By.linkText("Add to cart")).click();
        return this;
    }

    public shopPagePOM addBeltToCartByButtonValue28() {
        driver.findElement(By.cssSelector("button[value='28']")).click();
        return this;
    }

    public cartPagePOM viewCartFromNotice() {
        driver.findElement(By.linkText("View cart")).click();
        return new cartPagePOM(driver);
    }
}